package com.smk.sdp.sms;

import com.smk.sdp.util.DBConnection;
import org.apache.log4j.Logger;
import org.quartz.SchedulerException;

import java.sql.Connection;

public class StartSmsReceiver {
	private  Logger logger = new LogManager(this.getClass()).getLogger();

	/**
	 * @param args
	 * @throws SchedulerException
	 */
	public static void main(String[] args) {
		DBConnection dbConnect = new DBConnection();
		Connection connection = dbConnect.getConnection();
		SmsReceiver receiver = new SmsReceiver(connection);
		while (true) {
			try {
				receiver.receiver();
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				new StartSmsReceiver().logger.debug(e.getMessage(), e);
			}
		}
	}

}
