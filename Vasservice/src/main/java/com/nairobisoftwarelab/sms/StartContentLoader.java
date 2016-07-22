package com.nairobisoftwarelab.sms;

import com.nairobisoftwarelab.util.DBConnection;
import org.apache.log4j.Logger;
import org.quartz.SchedulerException;

import java.sql.Connection;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class StartContentLoader {
	private  Logger logger = new LogManager(this.getClass()).getLogger();


	/**
	 * @param args
	 * @throws SchedulerException
	 */
	public static void main(String[] args) {

		final DBConnection dbConnect = new DBConnection();
		final Connection connection = dbConnect.getConnection();
		final 		ContentLoader cl = new ContentLoader(connection);

		Runnable runnable = new Runnable() {
			public void run() {
				cl.getNewContent();
				cl.sendContent();
			}
		};

		ScheduledExecutorService service = Executors
				.newSingleThreadScheduledExecutor();
		service.scheduleAtFixedRate(runnable, 0, 1, TimeUnit.HOURS);


	}
}
