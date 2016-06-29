package com.smk.sdp.sms;

import com.smk.sdp.util.DBConnection;
import org.quartz.SchedulerException;

import java.sql.Connection;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class StartNotification {

    /**
     * @param args
     * @throws SchedulerException
     */
    public static void main(String[] args) {

        final DBConnection dbConnect = new DBConnection();
        final Connection connection = dbConnect.getConnection();
        final Notification notification = new Notification(connection);
        Runnable runnable = new Runnable() {
            public void run() {
                notification.startServiceNotification();
                notification.stopServiceNotification();
            }
        };

        ScheduledExecutorService service = Executors
                .newSingleThreadScheduledExecutor();
        service.scheduleAtFixedRate(runnable, 0, 1, TimeUnit.HOURS);
    }
}
