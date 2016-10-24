package com.nairobisoftwarelab.sms;

import org.quartz.SchedulerException;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class StartContentLoader {

    /**
     * @param args
     * @throws SchedulerException
     */
    public static void main(String[] args) {

        final ContentLoader cl = new ContentLoader();

        Runnable runnable = () -> {
            cl.getNewContent();
            cl.sendContent();
        };

        ScheduledExecutorService service = Executors
                .newSingleThreadScheduledExecutor();
        service.scheduleAtFixedRate(runnable, 0, 1, TimeUnit.HOURS);
    }
}
