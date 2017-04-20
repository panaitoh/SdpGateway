package com.nairobisoftwarelab.sms;

import com.nairobisoftwarelab.util.*;
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

        try {
            String sql = "SELECT id, product_id, message, content_type FROM vasmaster_content.scheduled_content_view " +
                    "WHERE scheduled_date <=now() LIMIT 1";
            connection.setAutoCommit(false);
            PreparedStatement pstmt = connection.prepareStatement("UPDATE vasmaster_content.content SET status =? WHERE id =?");
            PreparedStatement pstmtoutbox = connection.prepareStatement("INSERT INTO outbox(message, " +
                    "sender_address, service_id, content_id, correlator) VALUES(?,?,?,?,?)");
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(sql);

            CorrelatorService correlatorService = new CorrelatorService();

            int correlator = correlatorService.getCurrentCorrelator(connection);

            int count = 0;
            while (rs.next()) {
                int content_id = rs.getInt(1);
                int product_id = rs.getInt(2);
                String message = rs.getString(3);
                String content_type = rs.getString(4); //TODO : set message priority

                if (content_type.trim().toLowerCase().equals("bulk")) {
                    String bulkSubscribers = "SELECT msisdn, service_id FROM " +
                            " vasmaster_content.send_bulk_messages where content_id=" + content_id;
                    Statement bulkStatement = connection.createStatement();
                    ResultSet bulkRs = bulkStatement.executeQuery(bulkSubscribers);
                    while (bulkRs.next()) {
                        int cor = ++correlator;
                        String msisdn = bulkRs.getString(1);
                        int serviceId = bulkRs.getInt(2);

                        pstmtoutbox.setString(1, message);
                        pstmtoutbox.setString(2, "254" + msisdn.substring(msisdn.trim().length() - 9));
                        pstmtoutbox.setInt(3, serviceId);
                        pstmtoutbox.setInt(4, content_id);
                        pstmtoutbox.setString(5, formatCorrelator(cor));

                        correlatorService.SaveCurrentCorrelator(connection, cor);

                        pstmtoutbox.addBatch();
                        count++;

                        if (count % 100 == 0) {
                            pstmtoutbox.executeBatch();
                            count = 0;
                        }
                    }
                } else {
                    String subSql = "SELECT msisdn, service_id FROM vasmaster_service.send_subscription_view WHERE product_id = " + product_id;
                    Statement subStatement = connection.createStatement();
                    ResultSet subRs = subStatement.executeQuery(subSql);
                    while (subRs.next()) {
                        int cor = ++correlator;
                        pstmtoutbox.setString(1, message);
                        pstmtoutbox.setString(2, rs.getString(1));
                        pstmtoutbox.setInt(3, rs.getInt(2));
                        pstmtoutbox.setInt(4, content_id);
                        pstmtoutbox.setString(5, formatCorrelator(cor));

                        correlatorService.SaveCurrentCorrelator(connection, cor);

                        pstmtoutbox.addBatch();
                        count++;

                        if (count % 100 == 0) {
                            pstmtoutbox.executeBatch();
                            count = 0;
                        }
                    }
                }

                pstmtoutbox.executeBatch();

                // update content
                pstmt.setString(1, Status.STATUS_PROCESSED.toString());
                pstmt.setInt(2, content_id);
                pstmt.executeUpdate();
            }

            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            logManger.error(e.getMessage(), e);
            try {
                connection.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
            logManger.error(e.getMessage(), e);
            try {
                connection.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
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

    public static void main(String args[]) {
        ContentLoader c = new ContentLoader();
        c.getNewContent();
    }

    private String formatCorrelator(int correlator) {
        Integer i = Integer.valueOf(correlator);
        String format = "%1$010d";
        String newCorrelator = String.format(format, i);
        return newCorrelator;
    }

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        getNewContent();
    }
}
