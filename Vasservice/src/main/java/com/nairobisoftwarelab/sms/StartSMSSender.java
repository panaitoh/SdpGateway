package com.nairobisoftwarelab.sms;

import org.quartz.SchedulerException;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class StartSMSSender {
    /**
     * @param args
     * @throws SchedulerException
     */

    public static void main(String[] args) {

        final SMSSender sender = new SMSSender();
        Runnable runnable = new Runnable() {
            public void run() {
                sender.sendSMS();
            }
        };

        ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
        service.scheduleAtFixedRate(runnable, 0, 5, TimeUnit.SECONDS);
    }
}
