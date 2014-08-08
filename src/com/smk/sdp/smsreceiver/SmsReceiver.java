package com.smk.sdp.smsreceiver;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.smk.sdp.SdpConstants;
import com.smk.sdp.SdpLogger;
import com.smk.sdp.data.client.DBConnection;

public class SmsReceiver extends DBConnection implements SdpConstants, Job {

	String response = "Thank you for using our services";
	Connection connect = null;
SdpLogger log = new SdpLogger(this);
	public SmsReceiver() {
		try {
			connect = DBConnection.getConnection();
		} catch (FileNotFoundException e) {
			SdpLogger.LOGGER.debug("Connection", e);
		//	e.printStackTrace();
		} catch (IOException e) {
			SdpLogger.LOGGER.debug("Connection", e);
		} catch (ClassNotFoundException e) {
			SdpLogger.LOGGER.debug("Connection", e);
		} catch (SQLException e) {
			SdpLogger.LOGGER.debug("Connection", e);
		}
	}

	public void getNewSms() {
		String sql = "SELECT id,accountid,serviceid,message,senderAddress,linkid,dateReceived FROM inbox  WHERE status=?";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		SdpLogger.LOGGER.debug("getNewSms() -> Getting new smses");
		try {

			pstmt = connect.prepareStatement(sql);
			pstmt.setInt(1, SMSNOTSENT);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				int id=rs.getInt(1);
				String accountid = rs.getString(2);
				String serviceid = rs.getString(3);
				String message = rs.getString(4);
				String senderAddress = rs.getString(5);
				String linkid = rs.getString(6);
				String dateReceived = rs.getString(7);
				saveInOutbox(accountid, serviceid, response, senderAddress,
						linkid, dateReceived);
				System.out.println("Message from " + senderAddress);
				Statement stmt = connect.createStatement();
				stmt.execute("UPDATE inbox SET status ="+SMSSENT+" WHERE id="+id);
				stmt.close();
				SdpLogger.LOGGER.debug("getNewSms() -> new sms founf from "+ senderAddress);
			}
		} catch (SQLException e) {
			SdpLogger.LOGGER.debug("getNewSms()",e);
		}

	}

	private void saveInOutbox(String accountid, String serviceid,
			String message, String senderAddress, String linkid,
			String dateReceived) {
		String sql = "INSERT INTO outbox(accountid,message,senderAddress,linkid,serviceid) VALUES(?,?,?,?,?)";
		PreparedStatement pstmt = null;
		SdpLogger.LOGGER.debug("saveInOutbox() -> sending a response");
		try {
			pstmt = connect.prepareStatement(sql);
			pstmt.setString(1, accountid);
			pstmt.setString(2, message);
			pstmt.setString(3, senderAddress);
			pstmt.setString(4, linkid);
			pstmt.setString(5, serviceid);
			
			pstmt.execute();
		} catch (SQLException e) {
			SdpLogger.LOGGER.debug("saveInOutbox() ->,",e);
		}
	}
	
	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		getNewSms();
		DBConnection.closeConnection(connect);
	}
}
