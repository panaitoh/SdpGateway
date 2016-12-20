package com.nairobisoftwarelab.sms;

import com.nairobisoftwarelab.util.DBConnection;
import com.nairobisoftwarelab.util.ILogManager;
import com.nairobisoftwarelab.util.LogManager;
import com.nairobisoftwarelab.util.Status;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.sql.*;

@DisallowConcurrentExecution
public class ContentLoader implements Job {
    private ILogManager logManger = new LogManager(this);


    public void getNewContent() {
        Connection connection = DBConnection.getConnection();

        logManger.debug("Checking for new content");
        String sql = "SELECT id,product_id,message,scheduled_date FROM content WHERE status='" + Status.STATUS_PENDING.toString() + "' AND scheduled_date <=now()";
        try {
            PreparedStatement pstmt = connection.prepareStatement("UPDATE content SET status =? WHERE id =?");
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(sql);

            int count = 0;
            while (rs.next()) {
                int id = rs.getInt(1);
                String product = rs.getString(2);
                String message = rs.getString(3);
                String scheduledDate = rs.getString(4);

                logManger.debug("Content for " + product + " scheduled for " + scheduledDate);
                prepareToSend(connection, product, message);
                pstmt.setString(1, Status.STATUS_PROCESSED.toString());
                pstmt.setInt(2, id);

                pstmt.addBatch();
                count++;

                if (count % 100 == 0) {
                    pstmt.executeBatch();
                    count = 0;
                }
            }
            pstmt.executeBatch();
        } catch (SQLException e) {
            logManger.error(e.getMessage(), e);
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                logManger.error(e.getMessage(), e);
            }
        }
    }

    private void prepareToSend(Connection connection, String product, String message) {
        String sql = "SELECT msisdn, service_id FROM vasmaster_service.send_subscription_view WHERE product_id=" + product;
        try {
            PreparedStatement pstmt = connection.prepareStatement("INSERT INTO vasmaster_service.outbox(message,service_id,sender_address) VALUES(?,?,?)");
            Statement statement = connection.createStatement();
            ResultSet rs1 = statement.executeQuery(sql);
            int count = 0;
            while (rs1.next()) {
                String msisdn = rs1.getString(1);
                int service = rs1.getInt(2);

                pstmt.setString(1, message);
                pstmt.setInt(2, service);
                pstmt.setString(3, msisdn);
                pstmt.addBatch();

                count++;

                if (count % 100 == 0) {
                    pstmt.executeBatch();
                    count = 0;
                }
            }

            pstmt.executeBatch();

        } catch (SQLException e) {
            logManger.error(e.getMessage(), e);
        }
    }

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        getNewContent();
    }
}
