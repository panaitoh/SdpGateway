package com.smk.sdp.sms;

import com.smk.sdp.model.Endpoint;
import com.smk.sdp.util.DatabaseManager;
import com.smk.sdp.util.DateService;
import com.smk.sdp.util.PasswordGenerator;
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
import org.apache.log4j.Logger;
import org.csapi.www.schema.parlayx.common.v2_1.SimpleReference;
import org.csapi.www.schema.parlayx.sms.notification_manager.v2_3.local.*;
import org.csapi.www.wsdl.parlayx.sms.notification_manager.v2_3.service.SmsNotificationManagerServiceStub;

import javax.xml.namespace.QName;
import java.rmi.RemoteException;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class Notification implements SdpConstants {
    private SmsNotificationManagerServiceStub notification_stub = null;
    private DatabaseManager databaseManager;
    private Logger logger;
    private static HashMap<String, Endpoint> sdpEndpoints;

    private Connection connection;

    public Notification(Connection connection) {
        logger = new LogManager(this.getClass()).getLogger();
        databaseManager = new DatabaseManager();
        this.connection = connection;
        sdpEndpoints = Endpoints.instance.getEndPoint(connection);
    }

    /**
     * This method initiates start service notification by invoking sdp
     * startNotification method. This will allow an sms service to receive smses
     * and delivery reports online.
     */
    public void startServiceNotification() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        logger.info("Checking new services");
        try {
            String sql = "SELECT s.serviceid,s.smsServiceActivationNumber,s.criteria,a.spid,a.password FROM services s INNER JOIN account a WHERE  s.accountid=a.id AND s.status=" + STATUS_READY +" LIMIT 20";
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(sql);
            PreparedStatement updateStatement = connection.prepareStatement("update services set correlator =?, status =?,dateActivated=? WHERE serviceid =?");

            while (rs.next()) {
                String serviceid = rs.getString(1);
                String smsServiceActivationNumber = rs.getString(2);
                String criteria = rs.getString(3);
                String spid = rs.getString(4);
                String password = rs.getString(5);

                notification_stub = new SmsNotificationManagerServiceStub(sdpEndpoints.get("notification").getUrl().trim());
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
                String pass = PasswordGenerator.instance.getPassword(spid.trim(), password.trim(), time.trim());
                SOAPFactory factory = OMAbstractFactory.getSOAP11Factory();
                OMElement payload = factory.createOMElement(new QName(
                        "http://www.huawei.com.cn/schema/common/v2_1",
                        "RequestSOAPHeader", "v2"), null);
                OMElement myspid = factory.createOMElement(new QName("spId"));
                myspid.setText(spid);
                payload.addChild(myspid);

                OMElement spPassword = factory.createOMElement(new QName("spPassword"), null);
                spPassword.setText(pass);
                payload.addChild(spPassword);

                OMElement service_Id = factory.createOMElement(new QName("serviceId"), null);
                service_Id.setText(serviceid);
                payload.addChild(service_Id);

                OMElement time_Stamp = factory.createOMElement(new QName("timeStamp"), null);
                time_Stamp.setText(time);
                payload.addChild(time_Stamp);

                startClient.addHeader(payload);

                StartSmsNotification start = new StartSmsNotification();
                String correlator = "" + PasswordGenerator.instance.generateCorrelator(connection);

                SimpleReference simple = new SimpleReference();
                simple.setEndpoint(new URI(sdpEndpoints.get("notifysms").getUrl().trim()));
                simple.setInterfaceName(sdpEndpoints.get("notifysms").getInterfacename());
                simple.setCorrelator(correlator);
                start.setReference(simple);
                start.setSmsServiceActivationNumber(new URI(
                        smsServiceActivationNumber));
                if (criteria != null) {
                    start.setCriteria(criteria);
                }

                StartSmsNotificationE startE = new StartSmsNotificationE();
                startE.setStartSmsNotification(start);

                StartSmsNotificationResponseE responseE = notification_stub
                        .startSmsNotification(startE);

                StartSmsNotificationResponse response = responseE
                        .getStartSmsNotificationResponse();
                logger.info(response.toString());


                updateStatement.setString(1, correlator);
                updateStatement.setInt(2, STATUS_ACTIVE);
                updateStatement.setString(3, df.format(new Date()));
                updateStatement.setString(4, serviceid);
                updateStatement.addBatch();
                logger.info("SERVICE : " + serviceid + "NOTIFICATION STARTED");

            }

            updateStatement.executeBatch();

        } catch (AxisFault e) {
            logger.error(e.getMessage(), e);
        } catch (MalformedURIException e) {
            logger.error(e.getMessage(), e);
        } catch (RemoteException e) {
            logger.error(e.getMessage(), e);
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
        } catch (org.csapi.www.wsdl.parlayx.sms.notification_manager.v2_3.service.PolicyException e) {
            logger.error(e.getMessage(), e);
        } catch (org.csapi.www.wsdl.parlayx.sms.notification_manager.v2_3.service.ServiceException e) {
            logger.error(e.getMessage(), e);
        }
    }

    /**
     * This method invokes sdp stopsmsnotification there by disabling online sms
     * reception and delivery reports.
     */
    public void stopServiceNotification() {
        logger.info("Stop service notification");

        try {
            String sql = "SELECT s.serviceid,s.correlator,a.spid,a.password FROM services s INNER JOIN account a WHERE  s.accountid=a.id AND s.status=" + STOP_PENDING;

            logger.info("Checking services to stop");
            ResultSet rs = databaseManager.RunQuery(sql, connection);

            PreparedStatement updateStatetment = connection.prepareStatement("UPDATE services SET status =? ,date_deactivated=? WHERE serviceid = ?");
            while (rs.next()) {
                String serviceid = rs.getString(1);
                String correlator = rs.getString(2);
                String spid = rs.getString(3);
                String password = rs.getString(4);

                notification_stub = new SmsNotificationManagerServiceStub(sdpEndpoints.get("notification").toString().trim());

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
                String pass = PasswordGenerator.instance.getPassword(spid, password, time);

                SOAPFactory factory = OMAbstractFactory.getSOAP11Factory();
                OMElement payload = factory.createOMElement(new QName(
                        "http://www.huawei.com.cn/schema/common/v2_1",
                        "RequestSOAPHeader", "v2"), null);
                OMElement myspid = factory.createOMElement(new QName("spId"));
                myspid.setText(spid);
                payload.addChild(myspid);

                OMElement spPassword = factory.createOMElement(new QName(
                        "spPassword"), null);
                spPassword.setText(pass);
                payload.addChild(spPassword);

                OMElement service_Id = factory.createOMElement(new QName(
                        "serviceId"), null);
                service_Id.setText(serviceid);
                payload.addChild(service_Id);

                OMElement time_Stamp = factory.createOMElement(new QName(
                        "timeStamp"), null);
                time_Stamp.setText(time);
                payload.addChild(time_Stamp);

                stopClient.addHeader(payload);

                StopSmsNotification stop = new StopSmsNotification();
                stop.setCorrelator(correlator);

                StopSmsNotificationE stopE = new StopSmsNotificationE();
                stopE.setStopSmsNotification(stop);

                StopSmsNotificationResponseE responseE = notification_stub.stopSmsNotification(stopE);
                StopSmsNotificationResponse response = responseE.getStopSmsNotificationResponse();

                logger.info("Service : " + serviceid + " has been deactivated:");

                updateStatetment.setInt(1, STATUS_STOPPED);
                updateStatetment.setString(2, DateService.instance.now());
                updateStatetment.setString(3, serviceid);

                updateStatetment.addBatch();
            }

            updateStatetment.executeBatch();

        } catch (AxisFault e) {
            logger.error(e.getMessage(), e);
        } catch (RemoteException e) {
            logger.error(e.getMessage(), e);
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
        } catch (org.csapi.www.wsdl.parlayx.sms.notification_manager.v2_3.service.PolicyException e) {
            logger.error(e.getMessage(), e);
        } catch (org.csapi.www.wsdl.parlayx.sms.notification_manager.v2_3.service.ServiceException e) {
            logger.error(e.getMessage(), e);
        }
    }
}
