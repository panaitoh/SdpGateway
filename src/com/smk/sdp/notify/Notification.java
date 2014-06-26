package com.smk.sdp.notify;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import javax.xml.namespace.QName;

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
import org.csapi.www.schema.parlayx.sms.notification_manager.v2_3.local.StartSmsNotification;
import org.csapi.www.schema.parlayx.sms.notification_manager.v2_3.local.StartSmsNotificationE;
import org.csapi.www.schema.parlayx.sms.notification_manager.v2_3.local.StartSmsNotificationResponse;
import org.csapi.www.schema.parlayx.sms.notification_manager.v2_3.local.StartSmsNotificationResponseE;
import org.csapi.www.schema.parlayx.sms.notification_manager.v2_3.local.StopSmsNotification;
import org.csapi.www.schema.parlayx.sms.notification_manager.v2_3.local.StopSmsNotificationE;
import org.csapi.www.wsdl.parlayx.sms.notification_manager.v2_3.service.SmsNotificationManagerServiceStub;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.smk.sdp.Endpoint;
import com.smk.sdp.PasswordGenerator;
import com.smk.sdp.SdpConstants;
import com.smk.sdp.data.client.DBConnection;

public class Notification extends DBConnection implements SdpConstants, Job {
	// Notification
	private SmsNotificationManagerServiceStub notification_stub = null;
	public static Logger LOGGER;
	Date date;
	SimpleDateFormat df;
	Connection connect = null;

	public Notification() {

		try {
			connect = DBConnection.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * This method initiates start service notification by invoking sdp
	 * startNotification method. This will allow an sms service to receive smses
	 * and delivery reports online.
	 */
	public void startServiceNotification() {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {

			StartNotification.LOGGER.info("Registering a new service");
			String sql = "SELECT s.serviceid,s.smsServiceActivationAumber,s.criteria,a.spid,a.password FROM services s INNER JOIN account a WHERE  s.accountid=a.id AND s.status=?;";

			pstmt = connect.prepareStatement(sql);
			pstmt.setInt(1, STATUS_READY);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				String serviceid = rs.getString("serviceid");
				String smsServiceActivationNumber = rs
						.getString("smsServiceActivationAumber");
				String criteria = rs.getString("criteria");
				String password = rs.getString("password");
				String spid = rs.getString("spid");

				Map<String, String> startStopEndpoint = Endpoint
						.getEndPoint("notification");
				Map<String, String> deliveryEndPoint = Endpoint
						.getEndPoint("delivery");

				notification_stub = new SmsNotificationManagerServiceStub(
						startStopEndpoint.get("url").toString().trim());
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

				String time_cor = PasswordGenerator.getTimeStamp();
				String pass = PasswordGenerator.getPassword(spid.trim(),
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
				simple.setEndpoint(new URI(deliveryEndPoint.get("url")
						.toString().trim()));
				simple.setInterfaceName("notifySmsReception");
				simple.setCorrelator(time_cor);
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
				System.out.println(response.toString());
				Statement statement = connect.createStatement();
				statement.execute("update services set correlator ='"
						+ time_cor + "', status ='" + STATUS_ACTIVE
						+ "',dateActivated='" + df.format(new Date())
						+ "' WHERE serviceid = " + serviceid);

				StartNotification.LOGGER.info("SERVICE : " + serviceid + "NOTICATION STARTED");
				statement.close();
			}

		} catch (AxisFault e) {
			StartNotification.LOGGER.info("Axis fault", e);
		} catch (MalformedURIException e) {
			StartNotification.LOGGER.info("Bad url", e);
		} catch (RemoteException e) {
			StartNotification.LOGGER.info("REMOTE EXCEPTION", e);
		} catch (SQLException e) {
			StartNotification.LOGGER.info("SQL Error:  ", e);
		} catch (org.csapi.www.wsdl.parlayx.sms.notification_manager.v2_3.service.PolicyException e) {
			StartNotification.LOGGER.info("Policy Exception", e);
		} catch (org.csapi.www.wsdl.parlayx.sms.notification_manager.v2_3.service.ServiceException e) {
			StartNotification.LOGGER.info("Service Exception", e);
		}
	}

	/**
	 * This method invokes sdp stopsmsnotification there by disabling online sms
	 * reception and delivery reports.
	 */
	public void stopServiceNotification() {
		LOGGER.info("Stop service notification");
		String sql = "SELECT s.serviceid,s.correlator,a.spid,a.password FROM services s INNER JOIN account a WHERE  s.accountid=a.id AND s.status=?;";
		ResultSet rs = null;
		PreparedStatement pstmt = null;
		try {
			Map<String, String> startStopEndpoint = Endpoint
					.getEndPoint("notification");
			pstmt = connect.prepareStatement(sql);
			pstmt.setInt(1, STOP_PENDING);
			pstmt.setInt(2, 100);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				String serviceid = rs.getString("serviceid");
				String correlator = rs.getString("correlator");
				String spid = rs.getString("spid");
				String password = rs.getString("password");

				notification_stub = new SmsNotificationManagerServiceStub(
						startStopEndpoint.get("url").toString().trim());

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

				String time_cor = PasswordGenerator.getTimeStamp();
				String pass = PasswordGenerator.getPassword(spid, password,
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
				stop.setCorrelator(correlator);

				StopSmsNotificationE stopE = new StopSmsNotificationE();
				stopE.setStopSmsNotification(stop);

				// StopSmsNotificationResponseE responseE =
				notification_stub.stopSmsNotification(stopE);
				// StopSmsNotificationResponse response = responseE
				// .getStopSmsNotificationResponse();
				Statement statement = connect.createStatement();
				statement.execute("UPDATE services SET status ="
						+ STATUS_STOPPED + ",date_deactivated='"
						+ df.format(new Date() + "'") + " where serviceid = "
						+ serviceid);
				LOGGER.info("Service : " + serviceid + " has been deactivated:");
				statement.close();
			}
		} catch (AxisFault e) {
			LOGGER.info("STOP NOTICATION : ", e);
		} catch (RemoteException e) {
			LOGGER.info("STOP NOTICATION : ", e);
		} catch (SQLException e) {
			LOGGER.info("STOP NOTICATION : ", e);
		} catch (org.csapi.www.wsdl.parlayx.sms.notification_manager.v2_3.service.PolicyException e) {
			LOGGER.info("PolicyException : ", e);
		} catch (org.csapi.www.wsdl.parlayx.sms.notification_manager.v2_3.service.ServiceException e) {
			LOGGER.info("ServiceException : ", e);
		}
	}

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		startServiceNotification();
		stopServiceNotification();
		DBConnection.closeConnection(connect);
	}

}
