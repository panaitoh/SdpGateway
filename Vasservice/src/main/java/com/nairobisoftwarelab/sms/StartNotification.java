package com.nairobisoftwarelab.sms;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

public class StartNotification {

    /**
     * @param args
     * @throws SchedulerException
     */
    public static void main(String[] args) {
        SchedulerFactory sf = new StdSchedulerFactory();
        final Scheduler sched;
        try {
            sched = sf.getScheduler();
            sched.start();

            JobDetail job = newJob(ServiceNotification.class)
                    .withIdentity("SmsServiceJob", "SMSSERVICEGROUP")
                    .build();

            CronTrigger trigger = newTrigger()
                    .withIdentity("SmsServiceTrigger", "SMSSERVICEGROUP")
                    .withSchedule(cronSchedule("0 0/15 8-18 * * ?"))
                    .forJob("SmsServiceJob", "SMSSERVICEGROUP")
                    .build();

            sched.scheduleJob(job, trigger);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }
}
