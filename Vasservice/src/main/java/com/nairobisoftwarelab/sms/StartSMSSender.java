package com.nairobisoftwarelab.sms;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

public class StartSMSSender {
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

            JobDetail job = newJob(SMSSender.class)
                    .withIdentity("SMS_SENDER_JOB", "SMS_SENDER")
                    .build();

            Trigger trigger = newTrigger()
                    .withIdentity("SMS_SENDER_TRIGGER", "SMS_SENDER")
                    .startNow()
                    .withSchedule(simpleSchedule()
                            .withIntervalInSeconds(5)
                            .repeatForever())
                    .build();
            sched.scheduleJob(job, trigger);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }
}
