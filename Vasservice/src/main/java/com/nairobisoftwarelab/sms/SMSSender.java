package com.nairobisoftwarelab.sms;

import com.nairobisoftwarelab.model.Endpoint;
import com.nairobisoftwarelab.util.DBConnection;
import com.nairobisoftwarelab.util.DateService;
import com.nairobisoftwarelab.util.PasswordGenerator;
import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMNamespace;
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
import org.csapi.www.schema.parlayx.sms.send.v2_2.local.SendSms;
import org.csapi.www.schema.parlayx.sms.send.v2_2.local.SendSmsE;
import org.csapi.www.schema.parlayx.sms.send.v2_2.local.SendSmsResponse;
import org.csapi.www.schema.parlayx.sms.send.v2_2.local.SendSmsResponseE;
import org.csapi.www.wsdl.parlayx.sms.send.v2_2.service.PolicyException;
import org.csapi.www.wsdl.parlayx.sms.send.v2_2.service.SendSmsServiceStub;
import org.csapi.www.wsdl.parlayx.sms.send.v2_2.service.ServiceException;
import java.rmi.RemoteException;
import java.sql.*;
import java.util.Map;

/**
 * @author Martin lugaliki
 *         <p>
 *         Software developer</br>Vas masters
 *         </p>StartContentLoader
 */
public class SMSSender implements SdpConstants {
    private Logger logger;
    private static Map<String, Endpoint> sdpEndpoints;
    DBConnection dbConnect;
    private Connection connection;
    private SendSmsServiceStub sendSmsStub = null;
    private ServiceClient sendSmsClient;
    private String sdpUrl;

    public SMSSender(Connection connection) {
        logger = new LogManager(this.getClass()).getLogger();
        dbConnect = new DBConnection();
        this.connection = connection;
        sdpEndpoints = Endpoints.instance.getEndPoint(connection);

        sdpUrl = sdpEndpoints.get("sdp").getUrl().trim();

        try {
            sendSmsStub = new SendSmsServiceStub(sdpUrl);
            sendSmsClient = sendSmsStub._getServiceClient();
            Options options = sendSmsClient.getOptions();
            options.setProperty(HTTPConstants.CHUNKED, Boolean.FALSE);
            options.setTransportInProtocol(Constants.TRANSPORT_HTTP);
            options.setProperty(Constants.Configuration.MESSAGE_TYPE,
                    HTTPConstants.MEDIA_TYPE_APPLICATION_ECHO_XML);
            options.setProperty(
                    Constants.Configuration.DISABLE_SOAP_ACTION,
                    Boolean.TRUE);
            sendSmsClient.setOptions(options);
        } catch (AxisFault e) {
            logger.error(e.getMessage(), e);
        }
    }


    /**
     * This method sends an sms to sdp
     */
    public void sendSMS() {
        logger.info("SendSms : Getting new smses");
        SendSms sendsms = new SendSms();
        try {
            String sql = "SELECT o.id,o.message,o.senderAddress,o.linkid, s.serviceid,s.smsServiceActivationNumber," +
                    "a.spid,a.password FROM outbox o INNER JOIN account a INNER JOIN services s WHERE o.accountid=a.id " +
                    "AND o.serviceid=s.id and o.status=" + SMSNOTSENT + " LIMIT 20";
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(sql);

            //ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(4);
            PreparedStatement updateStatement = connection.prepareStatement("UPDATE outbox SET requestIdentifier=?,  status = ? where id =?");

            while (rs.next()) {
                String time = DateService.instance.formattedTime();
                int id = rs.getInt(1);
                String message = rs.getString(2);
                String senderAddress = rs.getString(3);
                String linkid = rs.getString(4);
                String serviceid = rs.getString(5);
                String smsServiceActivationNumber = rs.getString(6);
                String spid = rs.getString(7);
                String password = rs.getString(8);
                String pass = PasswordGenerator.instance.getPassword(spid, password, time);
                String correlator = "" + PasswordGenerator.instance.generateCorrelator(connection);

                updateStatement.setString(1, null);
                updateStatement.setInt(2, 3);
                updateStatement.setInt(3, id);
                updateStatement.execute();

              /* SmsObject task = new SmsObject(id, message, senderAddress, linkid, serviceid, smsServiceActivationNumber, spid, time, pass, correlator, sdpEndpoints, connection);
                executor.execute(task);*/


                SOAPFactory factory = OMAbstractFactory.getSOAP11Factory();
                OMNamespace namespace = factory.createOMNamespace(
                        "http://www.huawei.com.cn/schema/common/v2_1", "v2");
                OMElement payload = factory.createOMElement(
                        "RequestSOAPHeader", namespace);
                OMElement myspid = factory.createOMElement("spId", namespace);
                myspid.setText(spid);
                payload.addChild(myspid);

                OMElement spPassword = factory.createOMElement("spPassword",
                        namespace);
                spPassword.setText(pass);
                payload.addChild(spPassword);

                OMElement service_Id = factory.createOMElement("serviceId",
                        namespace);
                service_Id.setText(serviceid);
                payload.addChild(service_Id);

                OMElement time_Stamp = factory.createOMElement("timeStamp", namespace);
                time_Stamp.setText(time);
                payload.addChild(time_Stamp);
                if (linkid != null) {
                    OMElement sdp_linkid = factory.createOMElement("linkid",
                            namespace);
                    sdp_linkid.setText(linkid);
                    payload.addChild(sdp_linkid);
                }

                OMElement sdp_oa = factory.createOMElement("OA", namespace);
                sdp_oa.setText(senderAddress);
                payload.addChild(sdp_oa);

                OMElement sdp_fa = factory.createOMElement("FA", namespace);
                sdp_fa.setText(senderAddress);
                payload.addChild(sdp_fa);

                sendSmsClient.addHeader(payload);

                URI sender[] = {new URI("tel:" + senderAddress)};

                sendsms.setAddresses(sender);
                sendsms.setSenderName(smsServiceActivationNumber);
                sendsms.setMessage(message.trim());

                SimpleReference ref = new SimpleReference();
                ref.setEndpoint(new URI(sdpEndpoints.get("deliverysms").getUrl().trim()));
                ref.setInterfaceName(sdpEndpoints.get("deliverysms").getInterfacename().trim());
                ref.setCorrelator(correlator);

                sendsms.setReceiptRequest(ref);

                SendSmsE sendSmsE = new SendSmsE();
                sendSmsE.setSendSms(sendsms);

                SendSmsResponseE responseE = sendSmsStub.sendSms(sendSmsE);
                SendSmsResponse response = responseE.getSendSmsResponse();

                logger.info("SMS SENT TO : " + senderAddress + " from Short code " + smsServiceActivationNumber + " at : " + DateService.instance.now());
                String result = response.getResult();

                updateStatement.setString(1, result);
                updateStatement.setInt(2, SMSSENT);
                updateStatement.setInt(3, id);
                updateStatement.execute();
                //updateStatement.addBatch();
            }
           // executor.shutdown();

            //updateStatement.executeBatch();

        } catch (MalformedURIException e) {
            logger.error(e.getMessage(), e);
        } catch (AxisFault e) {
            logger.error(e.getMessage(), e);
        } catch (RemoteException e) {
            logger.error(e.getMessage(), e);
        } catch (PolicyException e) {
            logger.error(e.getMessage(), e);
        } catch (ServiceException e) {
            logger.error(e.getMessage(), e);
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
        }
    }
}