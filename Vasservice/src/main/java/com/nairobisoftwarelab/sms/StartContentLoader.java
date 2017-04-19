package com.nairobisoftwarelab.sms;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

public class StartContentLoader {
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

            JobDetail job = newJob(ContentLoader.class)
                    .withIdentity("SmsContentJob", "SMSCONTENTGROUP")
                    .build();

            CronTrigger trigger = newTrigger()
                    .withIdentity("SmsContentTrigger", "SMSCONTENTGROUP")
                    .withSchedule(cronSchedule("0 0/15 6-23 * * ?"))
                    .forJob("SmsContentJob", "SMSCONTENTGROUP")
                    .build();
            sched.scheduleJob(job, trigger);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }
}
