package com.smk.sdp.contentloader;

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

public class ContentLoader implements SdpConstants, Job {
	Connection connect = null;
	SdpLogger log = new SdpLogger(this);
	public ContentLoader() {
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

	public void getNewContent() {
		System.out.println("Checking for new content");
		SdpLogger.LOGGER.debug("Checking for new content");
		String sql = "SELECT id,productCode,message,scheduledDate FROM content WHERE status=? and scheduledDate <=now()";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Connection connect = null;
		try {
			connect = DBConnection.getConnection();
			pstmt = connect.prepareStatement(sql);
			pstmt.setInt(1, SMSNOTSENT);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				int id = rs.getInt(1);
				String productCode = rs.getString(2);
				String message = rs.getString(3);
				String scheduledDate = rs.getString(4);
				System.out.println("Content available");
				SdpLogger.LOGGER.debug("Content available scheduled for "
						+ scheduledDate);
				prepareToSend(productCode, message);
				Statement stmt = connect.createStatement();
				stmt.execute("UPDATE content SET status =" + SMSSENT
						+ " WHERE id =" + id);
				stmt.close();
			}
		} catch (FileNotFoundException e) {
			SdpLogger.LOGGER.debug("getNewContent()", e);
			// e.printStackTrace();
		} catch (IOException e) {
			SdpLogger.LOGGER.debug("getNewContent()", e);
			// e.printStackTrace();
		} catch (ClassNotFoundException e) {
			SdpLogger.LOGGER.debug("getNewContent()", e);
			// e.printStackTrace();
		} catch (SQLException e) {
			SdpLogger.LOGGER.debug("getNewContent()", e);
			// e.printStackTrace();
		}
	}

	private void prepareToSend(String productCode, String message) {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "SELECT id,msisdn,serviceid,accountid FROM subscription_data WHERE product_code=? AND update_type=?";
		try {
			pstmt = connect.prepareStatement(sql);
			pstmt.setString(1, productCode);
			pstmt.setInt(2, 1);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				int id = rs.getInt(1);
				String msisdn = rs.getString(2);
				int serviceid = rs.getInt(3);
				int accountid = rs.getInt(4);
				saveContentToSend(message, msisdn, serviceid, accountid);
			}
		} catch (SQLException e) {
			SdpLogger.LOGGER.debug("prepareToSend", e);
			// e.printStackTrace();
		}
	}

	private boolean saveContentToSend(String message, String msisdn,
			int serviceid, int accountid) {
		boolean isSaved = false;
		String sql = "INSERT INTO contenttosend(serviceid,accountid,msisdn,message) VALUES(?,?,?,?);";
		PreparedStatement pstmt = null;
		try {
			pstmt = connect.prepareStatement(sql);
			pstmt.setInt(1, serviceid);
			pstmt.setInt(2, accountid);
			pstmt.setString(3, msisdn);
			pstmt.setString(4, message);
			pstmt.execute();
			isSaved = true;
		} catch (SQLException e) {
			SdpLogger.LOGGER.debug("prepareToSend", e);
			// e.printStackTrace();
		}
		return isSaved;
	}

	public void sendContent() {
		String sql = "SELECT id,serviceid,accountid,msisdn,message FROM contenttosend LIMIT 1000;";
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			pstmt = connect.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				System.err.println("getting smses to send");
				int id = rs.getInt(1);
				int serviceid = rs.getInt(2);
				int accountid = rs.getInt(3);
				String msisdn = rs.getString(4);
				String message = rs.getString(5);
				String outboxInsert = "INSERT INTO outbox(accountid,message,senderAddress,serviceid) VALUES('"
						+ accountid
						+ "','"
						+ message
						+ "','"
						+ msisdn
						+ "','"
						+ serviceid + "');";
				Statement stmt = connect.createStatement();
				System.err.println("inserting content in outbox");
				stmt.execute(outboxInsert);
				stmt.execute("DELETE FROM contenttosend WHERE id=" + id);
				System.err.println("deleting from contenttosend " + msisdn);
				stmt.close();
			}
		} catch (SQLException e) {
			// e.printStackTrace();
			SdpLogger.LOGGER.debug("sendContent()", e);
		} finally {
			try {
				if (pstmt != null) {
					pstmt.close();
				}
			} catch (SQLException e) {
				SdpLogger.LOGGER.debug("sendContent()", e);
				// e.printStackTrace();
			}
		}
	}

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		getNewContent();
		sendContent();
		DBConnection.closeConnection(connect);
	}
}
