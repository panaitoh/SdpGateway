package com.smk.sdp.smsreceiver;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

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

import com.smk.sdp.notify.Notification;
import com.smk.sdp.notify.StartNotification;
import com.smk.sdp.smssender.StartSMSSender;

public class StartSmsReceiver {

	public static Logger LOGGER = null;

	public StartSmsReceiver() {
		File file = new File(System.getProperty("user.dir")
				+ System.getProperty("file.separator") + "log4j.properties");
		Properties props = new Properties();
		try {
			props.load(new FileInputStream(file));
			PropertyConfigurator.configure(props);
			LOGGER = Logger.getLogger(StartNotification.class);
		} catch (Exception e) {
			LOGGER.debug("SDP Properties", e);
		}
		LOGGER.debug("Sdp notification starting");
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
			JobDetail job = newJob(SmsReceiver.class).withIdentity(
					"SDPNotification", "Vasmaster").build();

			Trigger trigger = newTrigger()
					.withIdentity("sms", "receiving")
					.startNow()
					.withSchedule(
							simpleSchedule().withIntervalInSeconds(5)
									.repeatForever()).build();
			sched.scheduleJob(job, trigger);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
