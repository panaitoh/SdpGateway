package com.nairobisoftwarelab.sms;

import com.nairobisoftwarelab.model.EndpointModel;
import com.nairobisoftwarelab.model.ServiceModel;
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
import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@DisallowConcurrentExecution
public class ServiceNotification extends DatabaseManager<ServiceModel> implements Job {
    private SmsNotificationManagerServiceStub notification_stub = null;
    private ILogManager logManager = new LogManager(this);

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
            String query = "SELECT s.id,s.serviceid,s.smsServiceActivationNumber,s.criteria,a.spid,a.password, " +
                    "s.correlator, s.status, s.dateCreated, s.dateActivated, s.dateDeactivated FROM services s " +
                    "INNER JOIN account a WHERE  s.accountid=a.id AND s.status=" + Status.STATUS_READY.getStatus() + " LIMIT 20";
            List<ServiceModel> services = getAll(connection, query);
            PreparedStatement updateStatement = connection.prepareStatement("update services set correlator =?, status =?,dateActivated=? WHERE serviceid =?");

            List<EndpointModel> endpoints = Endpoints.getInstance.getEndPoints(connection);


            EndpointModel notificationEndpoint = endpoints.stream()
                    .filter(item -> item.getEndpointname().equals("notification")).findFirst().get();

            EndpointModel notifySmsReceptionEndpoint = endpoints.stream()
                    .filter(item -> item.getEndpointname().equals("notifysms")).findFirst().get();

            if (notificationEndpoint == null || notifySmsReceptionEndpoint == null) {
                throw new SdpEndpointException("Sdp endpoint missing");
            }

            for (ServiceModel serv : services) {
                notification_stub = new SmsNotificationManagerServiceStub(notificationEndpoint.getUrl());
                ServiceClient startClient = notification_stub._getServiceClient();
                Options options = startClient.getOptions();
                options.setProperty(HTTPConstants.CHUNKED, Boolean.FALSE);
                options.setTransportInProtocol(Constants.TRANSPORT_HTTP);
                options.setProperty(Constants.Configuration.MESSAGE_TYPE,
                        HTTPConstants.MEDIA_TYPE_APPLICATION_ECHO_XML);
                options.setProperty(
                        Constants.Configuration.DISABLE_SOAP_ACTION,
                        Boolean.TRUE);
                startClient.setOptions(options);

                String time = DateService.instance.formattedTime();
                SOAPFactory factory = OMAbstractFactory.getSOAP11Factory();
                OMElement payload = factory.createOMElement(new QName(
                        "http://www.huawei.com.cn/schema/common/v2_1",
                        "RequestSOAPHeader", "v2"), null);
                OMElement myspid = factory.createOMElement(new QName("spId"));
                myspid.setText(serv.getSpid());
                payload.addChild(myspid);

                OMElement spPassword = factory.createOMElement(new QName("spPassword"), null);
                spPassword.setText(new TokenGenerator(serv.getSpid(), serv.getPassword(), time).getToken());
                payload.addChild(spPassword);

                OMElement service_Id = factory.createOMElement(new QName("serviceId"), null);
                service_Id.setText(serv.getServiceid());
                payload.addChild(service_Id);

                OMElement time_Stamp = factory.createOMElement(new QName("timeStamp"), null);
                time_Stamp.setText(time);
                payload.addChild(time_Stamp);

                startClient.addHeader(payload);

                StartSmsNotification start = new StartSmsNotification();
                String correlator = new Correlator(connection).getCorrelator();
                if (correlator == null) {
                    throw new CorrelatorException("Correlator missing");
                }


                SimpleReference simple = new SimpleReference();
                simple.setEndpoint(new URI(notificationEndpoint.getUrl().trim()));
                simple.setInterfaceName(notificationEndpoint.getInterfacename());
                simple.setCorrelator(correlator);
                start.setReference(simple);
                start.setSmsServiceActivationNumber(new URI(serv.getSmsServiceActivationNumber()));

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
                updateStatement.setInt(2, Status.STATUS_ACTIVE.getStatus());
                updateStatement.setString(3, df.format(new Date()));
                updateStatement.setString(4, serv.getServiceid());
                updateStatement.executeUpdate();

                logManager.debug(response.toString());
                logManager.debug("SERVICE : " + serv.getServiceid() + "NOTIFICATION STARTED, CORRELATOR " + correlator);
            }

        } catch (AxisFault e) {
            logManager.error(e.getMessage(), e);
        } catch (MalformedURIException e) {
            logManager.error(e.getMessage(), e);
        } catch (RemoteException e) {
            logManager.error(e.getMessage(), e);
        } catch (SQLException e) {
            logManager.error(e.getMessage(), e);
        } catch (org.csapi.www.wsdl.parlayx.sms.notification_manager.v2_3.service.PolicyException e) {
            logManager.error(e.getMessage(), e);
        } catch (org.csapi.www.wsdl.parlayx.sms.notification_manager.v2_3.service.ServiceException e) {
            logManager.error(e.getMessage(), e);
        } catch (SdpEndpointException e) {
            logManager.error(e.getMessage(), e);
        } catch (CorrelatorException e) {
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

            String query = "SELECT s.serviceid,s.correlator,a.spid,a.password FROM services s INNER JOIN account a WHERE  " +
                    "s.accountid=a.id AND s.status=" + Status.STOP_PENDING.getStatus();

            List<ServiceModel> services = getAll(connection, query);
            List<EndpointModel> endpoints = Endpoints.getInstance.getEndPoints(connection);
            EndpointModel notificationEndpoint = endpoints.stream()
                    .filter(item -> item.getEndpointname().equals("notification")).findFirst().get();


            if (notificationEndpoint == null) {
                throw new SdpEndpointException("Sdp endpoint missing");
            }
            PreparedStatement updateStatetment = connection.prepareStatement("UPDATE services SET status =? ," +
                    "date_deactivated=? WHERE serviceid = ?");

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

                String time = DateService.instance.formattedTime();

                SOAPFactory factory = OMAbstractFactory.getSOAP11Factory();
                OMElement payload = factory.createOMElement(new QName(
                        "http://www.huawei.com.cn/schema/common/v2_1",
                        "RequestSOAPHeader", "v2"), null);
                OMElement myspid = factory.createOMElement(new QName("spId"));
                myspid.setText(serv.getSpid());
                payload.addChild(myspid);

                OMElement spPassword = factory.createOMElement(new QName(
                        "spPassword"), null);
                spPassword.setText(new TokenGenerator(serv.getSpid(), serv.getPassword(), time).getToken());
                payload.addChild(spPassword);

                OMElement service_Id = factory.createOMElement(new QName(
                        "serviceId"), null);
                service_Id.setText(serv.getServiceid());
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
                StopSmsNotificationResponse response = responseE.getStopSmsNotificationResponse();


                updateStatetment.setInt(1, Status.STATUS_STOPPED.getStatus());
                updateStatetment.setString(2, DateService.instance.now());
                updateStatetment.setString(3, serv.getServiceid());

                updateStatetment.executeUpdate();
                logManager.debug("Service : " + serv.getServiceid() + " has been deactivated:");

            }

        } catch (AxisFault e) {
            logManager.error(e.getMessage(), e);
        } catch (RemoteException e) {
            logManager.error(e.getMessage(), e);
        } catch (SQLException e) {
            logManager.error(e.getMessage(), e);
        } catch (org.csapi.www.wsdl.parlayx.sms.notification_manager.v2_3.service.PolicyException e) {
            logManager.error(e.getMessage(), e);
        } catch (org.csapi.www.wsdl.parlayx.sms.notification_manager.v2_3.service.ServiceException e) {
            logManager.error(e.getMessage(), e);
        } catch (SdpEndpointException e) {
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
}
