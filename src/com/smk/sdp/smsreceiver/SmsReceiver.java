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
import com.smk.sdp.data.client.DBConnection;

public class SmsReceiver extends DBConnection implements SdpConstants, Job {

	String response = "Thank you for using our services";
	Connection connect = null;

	public SmsReceiver() {
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

	public void getNewSms() {
		String sql = "SELECT id,accountid,serviceid,message,senderAddress,linkid,dateReceived FROM inbox  WHERE status=?";
		PreparedStatement pstmt = null;
		ResultSet rs = null;

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
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	private void saveInOutbox(String accountid, String serviceid,
			String message, String senderAddress, String linkid,
			String dateReceived) {
		String sql = "INSERT INTO outbox(accountid,message,senderAddress,linkid,serviceid) VALUES(?,?,?,?,?)";
		PreparedStatement pstmt = null;
		try {
			pstmt = connect.prepareStatement(sql);
			pstmt.setString(1, accountid);
			pstmt.setString(2, message);
			pstmt.setString(3, senderAddress);
			pstmt.setString(4, linkid);
			pstmt.setString(5, serviceid);
			
			pstmt.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		getNewSms();
		DBConnection.closeConnection(connect);
	}
}
