package com.nairobisoftwarelab.test;

import com.nairobisoftwarelab.model.Endpoint;
import com.nairobisoftwarelab.sms.LogManager;
import com.nairobisoftwarelab.sms.SdpConstants;
import com.nairobisoftwarelab.util.DateService;
import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMNamespace;
import org.apache.axiom.soap.SOAPFactory;
import org.apache.axis2.AxisFault;
import org.apache.axis2.client.ServiceClient;
import org.apache.axis2.databinding.types.URI;
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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;

/**
 * Created by pc on 22/07/2016.
 */
public class SmsObject implements Runnable, SdpConstants {
    private int id;
    private String message;
    private String senderAddress;
    private String linkid;
    private String serviceid;
    private String smsServiceActivationNumber;
    private String spid;
    private String password;
    private String correlator;
    private Logger logger;

    private SendSmsServiceStub sendSmsStub = null;

    private static Map<String, Endpoint> sdpEndpoints;

    private Connection connection;
    private String time;
    private PreparedStatement updateStatement;

    public SmsObject(int id, String message, String senderAddress, String linkid, String serviceid, String smsServiceActivationNumber, String spid, String time, String password, String correlator, Map<String, Endpoint> url, Connection connection) {
        this.id = id;
        this.message = message;
        this.senderAddress = senderAddress;
        this.linkid = linkid;
        this.serviceid = serviceid;
        this.smsServiceActivationNumber = smsServiceActivationNumber;
        this.spid = spid;
        this.password = password;
        this.correlator = correlator;
        this.connection = connection;
        this.time = time;
        this.sdpEndpoints = url;

        logger = new LogManager(this.getClass()).getLogger();

        try {
            sendSmsStub = new SendSmsServiceStub(url.get("sdp").getUrl());
            updateStatement = this.connection.prepareStatement("UPDATE outbox SET requestIdentifier = ?, status = ?,correlator = ? where id =?");
        } catch (AxisFault axisFault) {
            axisFault.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSenderAddress() {
        return senderAddress;
    }

    public void setSenderAddress(String senderAddress) {
        this.senderAddress = senderAddress;
    }

    public String getLinkid() {
        return linkid;
    }

    public void setLinkid(String linkid) {
        this.linkid = linkid;
    }

    public String getServiceid() {
        return serviceid;
    }

    public void setServiceid(String serviceid) {
        this.serviceid = serviceid;
    }

    public String getSmsServiceActivationNumber() {
        return smsServiceActivationNumber;
    }

    public void setSmsServiceActivationNumber(String smsServiceActivationNumber) {
        this.smsServiceActivationNumber = smsServiceActivationNumber;
    }

    public String getSpid() {
        return spid;
    }

    public void setSpid(String spid) {
        this.spid = spid;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCorrelator() {
        return correlator;
    }

    public void setCorrelator(String correlator) {
        this.correlator = correlator;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }


    private void sendSms()
    {
        try {
            SendSms sendsms = new SendSms();

            final ServiceClient sendSmsClient = sendSmsStub._getServiceClient();

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
            spPassword.setText(getPassword());
            payload.addChild(spPassword);

            OMElement service_Id = factory.createOMElement("serviceId",
                    namespace);
            service_Id.setText(getServiceid());
            payload.addChild(service_Id);

            OMElement time_Stamp = factory.createOMElement("timeStamp", namespace);
            time_Stamp.setText(getTime());
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

            URI sender[] = new URI[]{new URI("tel:" + senderAddress)};
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
            String result = response.getResult();

            logger.info("SMS SENT TO : " + senderAddress + " from Short code " + smsServiceActivationNumber + " at : " + DateService.instance.now() + "Request ID "+ result);

            updateStatement.setString(1, result);
            updateStatement.setInt(2, SMSSENT);
            updateStatement.setString(3, correlator);
            updateStatement.setInt(4, id);
            updateStatement.execute();
            System.out.println("SMS SENT TO : " + senderAddress + " from Short code " + smsServiceActivationNumber + " at : " + DateService.instance.now());
            System.out.println("Current Thread Name : " + Thread.currentThread().getName());
        } catch (URI.MalformedURIException e) {
            e.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (ServiceException e) {
            e.printStackTrace();
        } catch (PolicyException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void run() {
       sendSms();
    }
}
