package com.smk.sdp.smssender;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;

import static org.quartz.JobBuilder.*;
import static org.quartz.SimpleScheduleBuilder.*;
import static org.quartz.TriggerBuilder.*;

public class StartSMSSender {
	public static Logger LOGGER;

	public StartSMSSender() {
		File file = new File(System.getProperty("user.dir")
				+ System.getProperty("file.separator") + "log4j.properties");
		Properties props = new Properties();
		try {
			props.load(new FileInputStream(file));
			PropertyConfigurator.configure(props);
			LOGGER = Logger.getLogger(StartSMSSender.class);
		} catch (Exception e) {
			LOGGER.debug("SDP Properties", e);
		}
		LOGGER.debug("sendsms starting");
	}

	/**
	 * @param args
	 * @throws SchedulerException
	 */
	public static void main(String[] args) {
		new StartSMSSender();
		try {
			SchedulerFactory schedFact = new org.quartz.impl.StdSchedulerFactory();
			Scheduler sched = schedFact.getScheduler();
			sched.start();
			JobDetail job = newJob(SMSSender.class).withIdentity("SendSms",
					"Vasmaster").build();

			Trigger trigger = newTrigger()
					.withIdentity("send", "sms")
					.startNow()
					.withSchedule(
							simpleSchedule().withIntervalInSeconds(3)
									.repeatForever()).build();

			sched.scheduleJob(job, trigger);
		} catch (Exception e) {
			LOGGER.info("SMSSENDER : ", e);
			e.printStackTrace();
		}
	}
}
