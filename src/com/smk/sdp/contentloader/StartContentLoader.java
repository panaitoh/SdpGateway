package com.smk.sdp.contentloader;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;
import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;
import org.apache.log4j.PropertyConfigurator;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;

import com.smk.sdp.SdpLogger;

public class StartContentLoader {
	SdpLogger log = new SdpLogger(this);
	public StartContentLoader() {
		File file = new File(System.getProperty("user.dir")
				+ System.getProperty("file.separator") + "log4j.properties");
		Properties props = new Properties();
		try {
			props.load(new FileInputStream(file));
			PropertyConfigurator.configure(props);
		} catch (Exception e) {
			SdpLogger.LOGGER.debug("SDP Properties", e);
		}
		SdpLogger.LOGGER.debug("Sdp notification starting");
	}

	/**
	 * @param args
	 * @throws SchedulerException
	 */
	public static void main(String[] args) {
		new StartContentLoader();
		try {
			SchedulerFactory schedFact = new org.quartz.impl.StdSchedulerFactory();

			Scheduler sched = schedFact.getScheduler();

			sched.start();
			JobDetail job = newJob(ContentLoader.class).withIdentity(
					"ContentLoader", "contnet_loader").build();

			Trigger trigger = newTrigger()
					.withIdentity("getting", "content")
					.startNow()
					.withSchedule(
							simpleSchedule().withIntervalInMinutes(2)
									.repeatForever()).build();
			sched.scheduleJob(job, trigger);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
