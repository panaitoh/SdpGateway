package com.mato.client;

import java.rmi.RemoteException;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.namespace.QName;
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
import org.csapi.www.schema.parlayx.sms.notification_manager.v2_3.local.StartSmsNotification;
import org.csapi.www.schema.parlayx.sms.notification_manager.v2_3.local.StartSmsNotificationE;
import org.csapi.www.schema.parlayx.sms.notification_manager.v2_3.local.StartSmsNotificationResponse;
import org.csapi.www.schema.parlayx.sms.notification_manager.v2_3.local.StartSmsNotificationResponseE;
import org.csapi.www.schema.parlayx.sms.notification_manager.v2_3.local.StopSmsNotification;
import org.csapi.www.schema.parlayx.sms.notification_manager.v2_3.local.StopSmsNotificationE;
import org.csapi.www.schema.parlayx.sms.send.v2_2.local.SendSms;
import org.csapi.www.schema.parlayx.sms.send.v2_2.local.SendSmsE;
import org.csapi.www.schema.parlayx.sms.send.v2_2.local.SendSmsResponse;
import org.csapi.www.schema.parlayx.sms.send.v2_2.local.SendSmsResponseE;
import org.csapi.www.wsdl.parlayx.sms.notification_manager.v2_3.service.SmsNotificationManagerServiceStub;
import org.csapi.www.wsdl.parlayx.sms.send.v2_2.service.PolicyException;
import org.csapi.www.wsdl.parlayx.sms.send.v2_2.service.SendSmsServiceStub;
import org.csapi.www.wsdl.parlayx.sms.send.v2_2.service.ServiceException;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * @author Martin lugaliki
 *         <p>
 *         Software developer</br>Vas masters
 *         </p>
 * 
 */
public class Sdp implements SdpConstants, Job {
	DbManager dbman = null;
	PasswordGenerator passwordGenerator;
	Date date;
	SimpleDateFormat df;
	Connection conn = null;
	private SendSmsServiceStub sendSmsStub = null;
	// Notification
	private SmsNotificationManagerServiceStub notification_stub = null;

	// Configurtion data

	public Sdp() {
		dbman = new DbManager();
		conn = dbman.getConnection();
		passwordGenerator = new PasswordGenerator();
		date = new Date();
		df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * This method sends an sms to sdp
	 */
	public void sendSMS() {
		SdpMain.logger.info("SendSms : Getting new smses");
		SendSms sendsms = new SendSms();
		try {
			// Default sql server
			String sql = "SELECT id,spid,password,message,sender_address,linkid,serviceid,sms_service_activation_number,endpoint,local_endpoint,delivery_interface_name FROM view_outbox WHERE status =? limit ?";

			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, 0);
			pstmt.setInt(2, 100);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				String time_cor = passwordGenerator.getTimeStamp();
				int id = rs.getInt(1);
				String spid = rs.getString(2);
				String password = rs.getString(3);
				String message = rs.getString(4);
				String sender_address = rs.getString(5);
				String linkid = "";
				if (rs.getString(6) != null) {
					linkid = rs.getString(6);
				}
				String serviceid = rs.getString(7);
				String sms_service_activation_number = rs.getString(8);
				String endpoint = rs.getString(9);
				String local_endpoint = rs.getString(10);
				String delivery_interface_name = rs.getString(11);

				// create soap message
				sendSmsStub = new SendSmsServiceStub(endpoint);
				ServiceClient sendSmsClient = sendSmsStub._getServiceClient();

				String pass = passwordGenerator.getPassword(spid, password,
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
				sdp_oa.setText(sender_address);
				payload.addChild(sdp_oa);

				OMElement sdp_fa = factory.createOMElement("FA", namespace);
				sdp_fa.setText(sender_address);
				payload.addChild(sdp_fa);

				sendSmsClient.addHeader(payload);

				URI sender[] = { new URI("tel:" + sender_address) };

				sendsms.setAddresses(sender);
				sendsms.setSenderName(sms_service_activation_number);
				sendsms.setMessage(message.trim());

				SimpleReference ref = new SimpleReference();
				ref.setEndpoint(new URI(local_endpoint));
				ref.setInterfaceName(delivery_interface_name);
				ref.setCorrelator(time_cor);

				sendsms.setReceiptRequest(ref);

				SendSmsE sendSmsE = new SendSmsE();
				sendSmsE.setSendSms(sendsms);

				SendSmsResponseE responseE = sendSmsStub.sendSms(sendSmsE);
				SendSmsResponse response = responseE.getSendSmsResponse();
				SdpMain.logger.info("\nSMS SENT TO : " + sender_address
						+ "Short code " + sms_service_activation_number
						+ " Time : " + df.format(new Date()) + "\n");

				System.out.println(response.getResult());
				Statement statement = conn.createStatement();
				statement.execute("UPDATE outbox SET request_identifier = '"
						+ response.getResult().trim()
						+ "', status = 1,correlator ='" + time_cor
						+ "' where id =" + id);
				statement.close();
			}
		} catch (MalformedURIException e) {
			SdpMain.logger.info("SEND TO SDP", e);
		} catch (AxisFault e) {
			SdpMain.logger.info("SEND TO SDP", e);
		} catch (RemoteException e) {
			SdpMain.logger.info("SEND TO SDP", e);
		} catch (PolicyException e) {
			SdpMain.logger.info("SEND TO SDP", e);
		} catch (ServiceException e) {
			SdpMain.logger.info("SEND TO SDP", e);
		} catch (SQLException e) {
			SdpMain.logger.info("SEND TO SDP", e);
		}
	}

	/*
	 * This method initiates start service notification by invoking sdp
	 * startNotification method. This will allow an sms service to receive smses
	 * and delivery reports online.
	 */
	public void startServiceNotification() {
		try {
			SdpMain.logger.info("Starting a new service");
			String sql = "SELECT id,spid,password,serviceid,local_endpoint,start_stop_endpoint,sms_service_activation_number,criteria,notify_interface_name FROM view_services WHERE status =? LIMIT ?";

			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, STATUS_READY);
			pstmt.setInt(2, 100);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				int id = rs.getInt(1);
				String spid = rs.getString(2);
				String password = rs.getString(3);
				String serviceid = rs.getString(4);
				String local_endpoint = rs.getString(5);
				String start_stop_endpoint = rs.getString(6);
				String sms_service_activation_number = rs.getString(7);
				String criteria = rs.getString(8);
				String notify_interface_name = rs.getString(9);

				notification_stub = new SmsNotificationManagerServiceStub(
						start_stop_endpoint.trim());
				ServiceClient startClient = notification_stub
						._getServiceClient();
				Options options = startClient.getOptions();
				options.setProperty(HTTPConstants.CHUNKED, Boolean.FALSE);
				options.setTransportInProtocol(Constants.TRANSPORT_HTTP);
				options.setProperty(Constants.Configuration.MESSAGE_TYPE,
						HTTPConstants.MEDIA_TYPE_APPLICATION_ECHO_XML);
				options.setProperty(
						Constants.Configuration.DISABLE_SOAP_ACTION,
						Boolean.TRUE);
				startClient.setOptions(options);

				String time_cor = passwordGenerator.getTimeStamp();
				String pass = passwordGenerator.getPassword(spid.trim(),
						password.trim(), time_cor.trim());
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
				time_Stamp.setText(time_cor);
				payload.addChild(time_Stamp);

				startClient.addHeader(payload);

				StartSmsNotification start = new StartSmsNotification();

				SimpleReference simple = new SimpleReference();
				simple.setEndpoint(new URI(local_endpoint));
				simple.setInterfaceName(notify_interface_name);
				simple.setCorrelator(time_cor);
				start.setReference(simple);
				start.setSmsServiceActivationNumber(new URI(
						sms_service_activation_number));
				if (criteria != null) {
					start.setCriteria(criteria);
				}

				StartSmsNotificationE startE = new StartSmsNotificationE();
				startE.setStartSmsNotification(start);

				StartSmsNotificationResponseE responseE = notification_stub
						.startSmsNotification(startE);

				StartSmsNotificationResponse response = responseE
						.getStartSmsNotificationResponse();
				System.out.println(response.toString());
				Statement statement = conn.createStatement();
				statement.execute("update services set correlator ='"
						+ time_cor + "', status ='" + STATUS_ACTIVE
						+ "',date_activated='" + df.format(new Date())
						+ "' WHERE id = " + id);

				SdpMain.logger.info("STAT NOTIFICATION : SERVICE : " + serviceid
						+ "NOTICATION STARTED");
				statement.close();
			}

		} catch (AxisFault e) {
			SdpMain.logger.info("Axis fault", e);
		} catch (MalformedURIException e) {
			SdpMain.logger.info("Bad url", e);
		} catch (RemoteException e) {
			SdpMain.logger.info("REMOTE EXCEPTION", e);
		} catch (SQLException e) {
			SdpMain.logger.info("Sql", e);
		} catch (org.csapi.www.wsdl.parlayx.sms.notification_manager.v2_3.service.PolicyException e) {
			SdpMain.logger.info("Policy Exception", e);
		} catch (org.csapi.www.wsdl.parlayx.sms.notification_manager.v2_3.service.ServiceException e) {
			SdpMain.logger.info("Service Exception", e);
		}
	}

	/**
	 * This method invokes sdp stopsmsnotification there by disabling online sms
	 * reception and delivery reports.
	 */
	public void stopServiceNotification() {
		SdpMain.logger.info("Stopping a service");
		String sql = "SELECT id,spid,password,serviceid,start_stop_endpoint FROM view_services WHERE status=? limit ?";

		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, STOP_PENDING);
			pstmt.setInt(2, 100);
			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				int id = rs.getInt(1);
				String spid = rs.getString(2);
				String password = rs.getString(3);
				String serviceid = rs.getString(4);
				String start_stop_endpoint = rs.getString(5);

				notification_stub = new SmsNotificationManagerServiceStub(
						start_stop_endpoint);

				ServiceClient stopClient = notification_stub
						._getServiceClient();

				Options options = stopClient.getOptions();
				options.setProperty(HTTPConstants.CHUNKED, Boolean.FALSE);
				options.setTransportInProtocol(Constants.TRANSPORT_HTTP);
				options.setProperty(Constants.Configuration.MESSAGE_TYPE,
						HTTPConstants.MEDIA_TYPE_APPLICATION_ECHO_XML);
				options.setProperty(
						Constants.Configuration.DISABLE_SOAP_ACTION,
						Boolean.TRUE);

				stopClient.setOptions(options);

				String time_cor = passwordGenerator.getTimeStamp();
				String pass = passwordGenerator.getPassword(spid, password,
						time_cor);

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
				time_Stamp.setText(time_cor);
				payload.addChild(time_Stamp);

				stopClient.addHeader(payload);

				StopSmsNotification stop = new StopSmsNotification();
				stop.setCorrelator(time_cor);

				StopSmsNotificationE stopE = new StopSmsNotificationE();
				stopE.setStopSmsNotification(stop);

				// StopSmsNotificationResponseE responseE =
				notification_stub.stopSmsNotification(stopE);
				// StopSmsNotificationResponse response = responseE
				// .getStopSmsNotificationResponse();
				Statement statement = conn.createStatement();
				statement.execute("UPDATE services SET status ="
						+ STATUS_STOPPED + ",date_deactivated='"
						+ df.format(new Date() + "'") + " where id = " + id);
				SdpMain.logger.info("STOP NOTIFICATION : Service " + serviceid
						+ " has been deactivated:");
				statement.close();
			}
		} catch (AxisFault e) {
			SdpMain.logger.info("STOP NOTICATION : ", e);
		} catch (RemoteException e) {
			SdpMain.logger.info("STOP NOTICATION : ", e);
		} catch (SQLException e) {
			SdpMain.logger.info("STOP NOTICATION : ", e);
		} catch (org.csapi.www.wsdl.parlayx.sms.notification_manager.v2_3.service.PolicyException e) {
			SdpMain.logger.info("STOP NOTICATION : ", e);
		} catch (org.csapi.www.wsdl.parlayx.sms.notification_manager.v2_3.service.ServiceException e) {
			SdpMain.logger.info("STOP NOTICATION : ", e);
		}
	}

	public void closeConnection() {
		try {
			if (conn != null) {
				conn.close();
				System.err.println("Connection closed");
			}
		} catch (SQLException e) {
			SdpMain.logger.info("Sqlexception", e);
		}
	}

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		sendSMS();
		startServiceNotification();
		stopServiceNotification();
		closeConnection();
	}
}