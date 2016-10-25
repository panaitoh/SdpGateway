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
                    .withIdentity("myJob", "group1")
                    .build();

            Trigger trigger = newTrigger()
                    .withIdentity("myTrigger", "group1")
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
