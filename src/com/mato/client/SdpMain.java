package com.mato.client;

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

public class SdpMain {
	public static Logger logger;

	public SdpMain() {
		File file = new File(System.getProperty("user.dir")
				+ System.getProperty("file.separator") + "log4j.properties");
		Properties props = new Properties();
		try {
			props.load(new FileInputStream(file));
			PropertyConfigurator.configure(props);
			logger = Logger.getLogger(SdpMain.class);
		} catch (Exception e) {
			logger.debug("SDP Properties", e);
		}
		logger.debug("Sdp sendsms starting");
	}

	/**
	 * @param args
	 * @throws SchedulerException
	 */
	public static void main(String[] args) {
		new SdpMain();
		try{SchedulerFactory schedFact = new org.quartz.impl.StdSchedulerFactory();

		Scheduler sched = schedFact.getScheduler();

		sched.start();
		JobDetail job = newJob(Sdp.class).withIdentity("SendSms", "Vasmaster")
				.build();

		Trigger trigger = newTrigger()
				.withIdentity("sending", "SmsMaster")
				.startNow()
				.withSchedule(
						simpleSchedule().withIntervalInSeconds(3)
								.repeatForever()).build();

		sched.scheduleJob(job, trigger);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
