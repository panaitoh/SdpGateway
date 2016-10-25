package com.nairobisoftwarelab.sms;

import com.nairobisoftwarelab.model.EndpointModel;
import com.nairobisoftwarelab.model.OutboxModel;
import com.nairobisoftwarelab.util.*;
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
import org.csapi.www.schema.parlayx.common.v2_1.SimpleReference;
import org.csapi.www.schema.parlayx.sms.send.v2_2.local.SendSms;
import org.csapi.www.schema.parlayx.sms.send.v2_2.local.SendSmsE;
import org.csapi.www.schema.parlayx.sms.send.v2_2.local.SendSmsResponse;
import org.csapi.www.schema.parlayx.sms.send.v2_2.local.SendSmsResponseE;
import org.csapi.www.wsdl.parlayx.sms.send.v2_2.service.PolicyException;
import org.csapi.www.wsdl.parlayx.sms.send.v2_2.service.SendSmsServiceStub;
import org.csapi.www.wsdl.parlayx.sms.send.v2_2.service.ServiceException;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

/**
 * @author Martin lugaliki
 *         <p>
 *         Software developer</br>Vas masters
 *         </p>StartContentLoader
 */
@DisallowConcurrentExecution
public class SMSSender extends DatabaseManager<OutboxModel> implements Job {
    private ILogManager logger = new LogManager(this);
    private SendSmsServiceStub sendSmsStub = null;
    private ServiceClient sendSmsClient;

    /**
     * This method sends an sms to sdp
     */
    public void sendSMS() {
        logger.info("SendSms : Getting new smses");
        SendSms sendsms = new SendSms();
        Connection connection = DBConnection.getConnection();
        try {
            String sql = "SELECT o.id,o.message,o.senderAddress,o.linkid, s.serviceid,s.smsServiceActivationNumber," +
                    "a.spid,a.password FROM outbox o INNER JOIN account a INNER JOIN services s WHERE o.accountid=a.id " +
                    "AND o.serviceid=s.id and o.status=" + Status.STATUS_PENDING.getStatus() + " LIMIT 20";
            List<OutboxModel> outboxMessages = getAll(connection, sql);

            List<EndpointModel> endpoints = Endpoints.getInstance.getEndPoints(connection);
            EndpointModel deliveryEndpoint = endpoints.stream()
                    .filter(item -> item.getEndpointname().equals("deliverysms")).findFirst().get();

            EndpointModel sdpEndpoint = endpoints.stream()
                    .filter(item -> item.getEndpointname().equals("sdp")).findFirst().get();
            if (deliveryEndpoint == null || sdpEndpoint == null) {
                throw new SdpEndpointException("Sdp endpoint missing");
            }

            sendSmsStub = new SendSmsServiceStub(sdpEndpoint.getUrl());
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

            PreparedStatement updateStatement = connection.prepareStatement("UPDATE outbox SET requestIdentifier=?,  status = ? where id =?");

            for (OutboxModel outbox : outboxMessages) {
                String time = new DateService().formattedTime();
                String pass = new TokenGenerator(outbox.getSpid(), outbox.getPassword(), time).getToken();
                String correlator = "" + new Correlator(connection).getCorrelator();

                SOAPFactory factory = OMAbstractFactory.getSOAP11Factory();
                OMNamespace namespace = factory.createOMNamespace(
                        "http://www.huawei.com.cn/schema/common/v2_1", "v2");
                OMElement payload = factory.createOMElement(
                        "RequestSOAPHeader", namespace);
                OMElement myspid = factory.createOMElement("spId", namespace);
                myspid.setText(outbox.getSpid());
                payload.addChild(myspid);

                OMElement spPassword = factory.createOMElement("spPassword",
                        namespace);
                spPassword.setText(pass);
                payload.addChild(spPassword);

                OMElement service_Id = factory.createOMElement("serviceId",
                        namespace);
                service_Id.setText(outbox.getServiceid());
                payload.addChild(service_Id);

                OMElement time_Stamp = factory.createOMElement("timeStamp", namespace);
                time_Stamp.setText(time);
                payload.addChild(time_Stamp);
                if (outbox.getLinkid() != null) {
                    OMElement sdp_linkid = factory.createOMElement("linkid",
                            namespace);
                    sdp_linkid.setText(outbox.getLinkid());
                    payload.addChild(sdp_linkid);
                }

                OMElement sdp_oa = factory.createOMElement("OA", namespace);
                sdp_oa.setText(outbox.getSenderAddress());
                payload.addChild(sdp_oa);

                OMElement sdp_fa = factory.createOMElement("FA", namespace);
                sdp_fa.setText(outbox.getSenderAddress());
                payload.addChild(sdp_fa);

                sendSmsClient.addHeader(payload);

                URI sender[] = {new URI("tel:" + outbox.getSenderAddress())};

                sendsms.setAddresses(sender);
                sendsms.setSenderName(outbox.getSmsServiceActivationNumber());
                sendsms.setMessage(outbox.getMessage());

                SimpleReference ref = new SimpleReference();
                ref.setEndpoint(new URI(deliveryEndpoint.getUrl()));
                ref.setInterfaceName(deliveryEndpoint.getInterfacename());
                ref.setCorrelator(correlator);

                sendsms.setReceiptRequest(ref);

                SendSmsE sendSmsE = new SendSmsE();
                sendSmsE.setSendSms(sendsms);

                SendSmsResponseE responseE = sendSmsStub.sendSms(sendSmsE);
                SendSmsResponse response = responseE.getSendSmsResponse();

                logger.debug("SMS SENT TO : " + outbox.getSenderAddress() + " from Short code " +
                        outbox.getSmsServiceActivationNumber() + " at : " + new DateService().now());
                String result = response.getResult();

                updateStatement.setString(1, result);
                updateStatement.setInt(2, Status.STATUS_READY.getStatus());
                updateStatement.setInt(3, outbox.getId());
                updateStatement.executeUpdate();
            }


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
        } catch (SdpEndpointException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
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
        sendSMS();
    }
}