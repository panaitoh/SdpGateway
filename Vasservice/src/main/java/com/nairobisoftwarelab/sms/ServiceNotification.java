package com.nairobisoftwarelab.sms;

import com.google.gson.reflect.TypeToken;
import com.nairobisoftwarelab.Database.QueryRunner;
import com.nairobisoftwarelab.model.EndpointModel;
import com.nairobisoftwarelab.model.ServiceModel;
import com.nairobisoftwarelab.sms.Exceptions.CorrelatorException;
import com.nairobisoftwarelab.sms.Exceptions.SdpEndpointException;
import com.nairobisoftwarelab.util.*;
import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.soap.SOAPFactory;
import org.apache.axis2.AxisFault;
import org.apache.axis2.Constants;
import org.apache.axis2.client.Options;
import org.apache.axis2.client.ServiceClient;
import org.apache.axis2.databinding.types.URI;
import org.apache.axis2.databinding.types.URI.MalformedURIException;
import org.apache.axis2.transport.http.HTTPConstants;
import org.csapi.www.schema.parlayx.common.v2_1.SimpleReference;
import org.csapi.www.schema.parlayx.sms.notification_manager.v2_3.local.*;
import org.csapi.www.wsdl.parlayx.sms.notification_manager.v2_3.service.SmsNotificationManagerServiceStub;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import javax.xml.namespace.QName;
import java.lang.reflect.Type;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.List;

@DisallowConcurrentExecution
public class ServiceNotification extends DatabaseManager<ServiceModel> implements Job {
    private SmsNotificationManagerServiceStub notification_stub = null;
    private ILogManager logManager = new LogManager(this);
    private Type type = new TypeToken<List<ServiceModel>>() {
    }.getType();
    private ServiceClient sendSmsClient;

    /**
     * This method initiates start service notification by invoking sdp
     * startNotification method. This will allow an sms service to receive smses
     * and delivery reports online.
     */

    public void startServiceNotification() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        logManager.debug("Checking new services");
        Connection connection = DBConnection.getConnection();

        try {
            String query = "SELECT id, service_id, ssan, criteria,spid,password FROM activate_service_view";
            List<ServiceModel> services = new QueryRunner<ServiceModel>(connection, query).getList(type);
            PreparedStatement updateStatement =
                    connection.prepareStatement("update services set correlator =?, status =? WHERE id =?");

            List<EndpointModel> endpoints = Endpoints.getInstance.getEndPoints(connection);


            EndpointModel notificationEndpoint = endpoints.stream()
                    .filter(item -> item.getEndpointname().equals("notification")).findFirst().get();

            EndpointModel notifySmsReceptionEndpoint = endpoints.stream()
                    .filter(item -> item.getEndpointname().equals("notifysms")).findFirst().get();

            if (notificationEndpoint == null || notifySmsReceptionEndpoint == null) {
                throw new SdpEndpointException("Sdp endpoint missing");
            }

            notification_stub = new SmsNotificationManagerServiceStub(notificationEndpoint.getUrl().trim());
            sendSmsClient = notification_stub._getServiceClient();
            Options options = sendSmsClient.getOptions();
            options.setProperty(HTTPConstants.CHUNKED, Boolean.FALSE);
            options.setTransportInProtocol(Constants.TRANSPORT_HTTP);
            options.setProperty(Constants.Configuration.MESSAGE_TYPE,
                    HTTPConstants.MEDIA_TYPE_APPLICATION_ECHO_XML);
            options.setProperty(
                    Constants.Configuration.DISABLE_SOAP_ACTION,
                    Boolean.TRUE);
            sendSmsClient.setOptions(options);
            for (ServiceModel serv : services) {


                String time = new DateService().formattedTime();
                SOAPFactory factory = OMAbstractFactory.getSOAP11Factory();
                OMElement payload = factory.createOMElement(new QName(
                        "http://www.huawei.com.cn/schema/common/v2_1",
                        "RequestSOAPHeader", "v2"), null);
                OMElement myspid = factory.createOMElement(new QName("spId"));
                myspid.setText(serv.getSpid());
                payload.addChild(myspid);

                String pass = new TokenGenerator(serv.getSpid(), serv.getPassword(), time).getToken();
                OMElement spPassword = factory.createOMElement(new QName("spPassword"), null);
                spPassword.setText(pass);
                payload.addChild(spPassword);

                OMElement service_Id = factory.createOMElement(new QName("serviceId"), null);
                service_Id.setText(serv.getService_id());
                payload.addChild(service_Id);

                OMElement time_Stamp = factory.createOMElement(new QName("timeStamp"), null);
                time_Stamp.setText(time);
                payload.addChild(time_Stamp);

                sendSmsClient.addHeader(payload);

                StartSmsNotification start = new StartSmsNotification();
                String correlator = new Correlator(connection).getCorrelator();
                if (correlator == null) {
                    throw new CorrelatorException("Correlator missing");
                }


                SimpleReference simple = new SimpleReference();
                simple.setEndpoint(new URI(notifySmsReceptionEndpoint.getUrl().trim()));
                simple.setInterfaceName(notifySmsReceptionEndpoint.getInterfacename());
                simple.setCorrelator(correlator);
                start.setReference(simple);
                start.setSmsServiceActivationNumber(new URI(serv.getSsan()));

                if (serv.getCriteria() != null) {
                    start.setCriteria(serv.getCriteria());
                }

                StartSmsNotificationE startE = new StartSmsNotificationE();
                startE.setStartSmsNotification(start);

                StartSmsNotificationResponseE responseE = notification_stub
                        .startSmsNotification(startE);

                StartSmsNotificationResponse response = responseE
                        .getStartSmsNotificationResponse();

                updateStatement.setString(1, correlator);
                updateStatement.setString(2, Status.STATUS_ACTIVE.toString());
                //updateStatement.setString(3, df.format(new Date()));
                updateStatement.setInt(3, serv.getId());
                updateStatement.executeUpdate();

                logManager.debug(response.toString());
                logManager.debug("SERVICE : " + serv.getService_id() + "NOTIFICATION STARTED, CORRELATOR " + correlator);
            }

        } catch (AxisFault e) {
            e.printStackTrace();
            logManager.error(e.getMessage(), e);
        } catch (MalformedURIException e) {
            e.printStackTrace();
            logManager.error(e.getMessage(), e);
        } catch (RemoteException e) {
            e.printStackTrace();
            logManager.error(e.getMessage(), e);
        } catch (SQLException e) {
            e.printStackTrace();
            logManager.error(e.getMessage(), e);
        } catch (org.csapi.www.wsdl.parlayx.sms.notification_manager.v2_3.service.PolicyException e) {
            e.printStackTrace();
            logManager.error(e.getMessage(), e);
        } catch (org.csapi.www.wsdl.parlayx.sms.notification_manager.v2_3.service.ServiceException e) {
            logManager.error(e.getMessage(), e);
        } catch (SdpEndpointException e) {
            e.printStackTrace();
            logManager.error(e.getMessage(), e);
        } catch (CorrelatorException e) {
            e.printStackTrace();
            logManager.error(e.getMessage(), e);
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
    }


    public void stopServiceNotification() {
        logManager.debug("Stop service notification");
        Connection connection = DBConnection.getConnection();
        try {

            String query = "SELECT  id, service_id, ssan, criteria, spid, password  FROM deactivate_service_view";

            List<ServiceModel> services = new QueryRunner<ServiceModel>(connection, query).getList(type);
            List<EndpointModel> endpoints = Endpoints.getInstance.getEndPoints(connection);
            EndpointModel notificationEndpoint = endpoints.stream()
                    .filter(item -> item.getEndpointname().equals("notification")).findFirst().get();


            if (notificationEndpoint == null) {
                throw new SdpEndpointException("Sdp endpoint missing");
            }
            PreparedStatement updateStatetment = connection.prepareStatement("UPDATE services SET status =?, " +
                    "updated_at=? WHERE id= ?");

            for (ServiceModel serv : services) {
                notification_stub = new SmsNotificationManagerServiceStub(notificationEndpoint.getUrl());

                ServiceClient stopClient = notification_stub._getServiceClient();

                Options options = stopClient.getOptions();
                options.setProperty(HTTPConstants.CHUNKED, Boolean.FALSE);
                options.setTransportInProtocol(Constants.TRANSPORT_HTTP);
                options.setProperty(Constants.Configuration.MESSAGE_TYPE,
                        HTTPConstants.MEDIA_TYPE_APPLICATION_ECHO_XML);
                options.setProperty(
                        Constants.Configuration.DISABLE_SOAP_ACTION,
                        Boolean.TRUE);

                stopClient.setOptions(options);

                String time = new DateService().formattedTime();

                SOAPFactory factory = OMAbstractFactory.getSOAP11Factory();
                OMElement payload = factory.createOMElement(new QName(
                        "http://www.huawei.com.cn/schema/common/v2_1",
                        "RequestSOAPHeader", "v2"), null);
                OMElement myspid = factory.createOMElement(new QName("spId"));
                myspid.setText(serv.getSpid());
                payload.addChild(myspid);

                String pass = new TokenGenerator(serv.getSpid(), serv.getPassword(), time).getToken();

                OMElement spPassword = factory.createOMElement(new QName(
                        "spPassword"), null);
                spPassword.setText(pass);
                payload.addChild(spPassword);

                OMElement service_Id = factory.createOMElement(new QName(
                        "serviceId"), null);
                service_Id.setText(serv.getService_id());
                payload.addChild(service_Id);

                OMElement time_Stamp = factory.createOMElement(new QName(
                        "timeStamp"), null);
                time_Stamp.setText(time);
                payload.addChild(time_Stamp);

                stopClient.addHeader(payload);

                StopSmsNotification stop = new StopSmsNotification();
                stop.setCorrelator(serv.getCorrelator());

                StopSmsNotificationE stopE = new StopSmsNotificationE();
                stopE.setStopSmsNotification(stop);

                StopSmsNotificationResponseE responseE = notification_stub.stopSmsNotification(stopE);
                responseE.getStopSmsNotificationResponse();


                updateStatetment.setString(1, Status.STATUS_STOPPED.toString());
                updateStatetment.setString(2, new DateService().now());
                updateStatetment.setInt(3, serv.getId());

                updateStatetment.executeUpdate();
                logManager.debug("Service : " + serv.getService_id() + " has been deactivated:");

            }

        } catch (AxisFault e) {
            e.printStackTrace();
            logManager.error(e.getMessage(), e);
        } catch (RemoteException e) {
            e.printStackTrace();
            logManager.error(e.getMessage(), e);
        } catch (SQLException e) {
            e.printStackTrace();
            logManager.error(e.getMessage(), e);
        } catch (org.csapi.www.wsdl.parlayx.sms.notification_manager.v2_3.service.PolicyException e) {
            e.printStackTrace();
            logManager.error(e.getMessage(), e);
        } catch (org.csapi.www.wsdl.parlayx.sms.notification_manager.v2_3.service.ServiceException e) {
            e.printStackTrace();
            logManager.error(e.getMessage(), e);
        } catch (SdpEndpointException e) {
            e.printStackTrace();
            logManager.error(e.getMessage(), e);
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        startServiceNotification();
        stopServiceNotification();
    }


    public static void main(String[] args){
        ServiceNotification serv = new ServiceNotification();
        serv.startServiceNotification();
    }
}
