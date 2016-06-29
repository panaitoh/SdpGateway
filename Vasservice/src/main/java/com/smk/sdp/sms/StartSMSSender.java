package com.smk.sdp.sms;

import com.smk.sdp.util.DBConnection;
import org.quartz.SchedulerException;

import java.sql.Connection;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class StartSMSSender {
    /**
     * @param args
     * @throws SchedulerException
     */

    public static void main(String[] args) {

        final DBConnection dbConnect = new DBConnection();
        final Connection connection = dbConnect.getConnection();
        final SMSSender sender = new SMSSender(connection);

        Runnable runnable = new Runnable() {
            public void run() {
                sender.sendSMS();
            }
        };

        ScheduledExecutorService service = Executors
                .newSingleThreadScheduledExecutor();
        service.scheduleAtFixedRate(runnable, 0, 10, TimeUnit.SECONDS);
    }
}
