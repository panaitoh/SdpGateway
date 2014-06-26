package com.smk.sdp.smssender;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.rmi.RemoteException;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
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
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import com.smk.sdp.Endpoint;
import com.smk.sdp.PasswordGenerator;
import com.smk.sdp.SdpConstants;
import com.smk.sdp.data.client.DBConnection;

/**
 * @author Martin lugaliki
 *         <p>
 *         Software developer</br>Vas masters
 *         </p>
 * 
 */
public class SMSSender implements SdpConstants, Job {
	Connection connect = null;
	Date date;
	SimpleDateFormat df;
	private SendSmsServiceStub sendSmsStub = null;

	public SMSSender() {
		date = new Date();
		df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			connect = DBConnection.getConnection();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * This method sends an sms to sdp
	 */
	public void sendSMS() {
		System.out.println("SendSms : Getting new smses");
		StartSMSSender.LOGGER.info("SendSms : Getting new smses");
		SendSms sendsms = new SendSms();
		try {
			// Default sql server
			String sql = "SELECT o.id,o.message,o.senderAddress,o.linkid, s.serviceid,s.smsServiceActivationNumber,a.spid,a.password FROM outbox o INNER JOIN account a INNER JOIN services s WHERE o.accountid=a.id AND o.serviceid=s.id and o.status=? LIMIT ?";

			PreparedStatement pstmt = connect.prepareStatement(sql);
			pstmt.setInt(1, SMSNOTSENT);
			pstmt.setInt(2, 100);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				String time_cor = PasswordGenerator.getTimeStamp();
				int id = rs.getInt("id");
				String message = rs.getString("message");
				String senderAddress = rs.getString("senderAddress");
				String linkid = "";
				if (rs.getString("linkid") != null) {
					linkid = rs.getString("linkid");
				}
				String serviceid = rs.getString("serviceid");
				String smsServiceActivationNumber = rs
						.getString("smsServiceActivationNumber");

				String spid = rs.getString("spid");
				String password = rs.getString("password");

				Map<String, String> sendEndPoint = Endpoint.getEndPoint("sdp");

				Map<String, String> localEndpoint = Endpoint
						.getEndPoint("deliverysms");

				// create soap message
				sendSmsStub = new SendSmsServiceStub(sendEndPoint.get("url")
						.toString().trim());
				ServiceClient sendSmsClient = sendSmsStub._getServiceClient();

				String pass = PasswordGenerator.getPassword(spid, password,
						time_cor);

				Options options = sendSmsClient.getOptions();
				options.setProperty(HTTPConstants.CHUNKED, Boolean.FALSE);
				options.setTransportInProtocol(Constants.TRANSPORT_HTTP);
				options.setProperty(Constants.Configuration.MESSAGE_TYPE,
						HTTPConstants.MEDIA_TYPE_APPLICATION_ECHO_XML);
				options.setProperty(
						Constants.Configuration.DISABLE_SOAP_ACTION,
						Boolean.TRUE);
				sendSmsClient.setOptions(options);

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

				OMElement time_Stamp = factory.createOMElement("timeStamp",
						namespace);
				time_Stamp.setText(time_cor);
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

				URI sender[] = { new URI("tel:" + senderAddress) };

				sendsms.setAddresses(sender);
				sendsms.setSenderName(smsServiceActivationNumber);
				sendsms.setMessage(message.trim());

				SimpleReference ref = new SimpleReference();
				ref.setEndpoint(new URI(localEndpoint.get("url").toString()
						.trim()));
				ref.setInterfaceName("notifySmsDeliveryReceipt");
				ref.setCorrelator(time_cor);

				sendsms.setReceiptRequest(ref);

				SendSmsE sendSmsE = new SendSmsE();
				sendSmsE.setSendSms(sendsms);

				SendSmsResponseE responseE = sendSmsStub.sendSms(sendSmsE);
				SendSmsResponse response = responseE.getSendSmsResponse();

				System.out.println("Sending sms to "+senderAddress);
				StartSMSSender.LOGGER.info("\nSMS SENT TO : " + senderAddress
						+ "Short code " + smsServiceActivationNumber
						+ " Time : " + df.format(new Date()) + "\n");

				System.out.println(response.getResult());
				Statement statement = connect.createStatement();
				statement.execute("UPDATE outbox SET requestIdentifier = '"
						+ response.getResult().trim() + "', status = "
						+ SMSSENT + ",correlator ='" + time_cor
						+ "' where id =" + id);
				statement.close();
			}
		} catch (MalformedURIException e) {
			StartSMSSender.LOGGER.info("SEND TO SDP", e);
		} catch (AxisFault e) {
			StartSMSSender.LOGGER.info("SEND TO SDP", e);
		} catch (RemoteException e) {
			StartSMSSender.LOGGER.info("SEND TO SDP", e);
		} catch (PolicyException e) {
			StartSMSSender.LOGGER.info("SEND TO SDP", e);
		} catch (ServiceException e) {
			StartSMSSender.LOGGER.info("SEND TO SDP", e);
		} catch (SQLException e) {
			StartSMSSender.LOGGER.info("SEND TO SDP", e);
		}
	}

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		sendSMS();
		DBConnection.closeConnection(connect);
	}
}