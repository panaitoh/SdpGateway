package com.nairobisoftwarelab.sms;

import com.nairobisoftwarelab.util.DatabaseManager;
import org.apache.log4j.Logger;

import java.sql.*;

public class ContentLoader implements SdpConstants {
    private DatabaseManager databaseManager;
    private Logger logger;
    private Connection connection;

    public ContentLoader(Connection connection) {
        databaseManager = new DatabaseManager();
        logger = new LogManager(this.getClass()).getLogger();
        this.connection = connection;
    }


    public void getNewContent() {
        logger.info("Checking for new content");
        String sql = "SELECT id,productCode,message,scheduledDate FROM content WHERE status=" + SMSNOTSENT + " AND scheduledDate <=now()";
        try {
            PreparedStatement pstmt = connection.prepareStatement("UPDATE content SET status =? WHERE id =?");
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(sql);

            int count = 0;
            while (rs.next()) {
                int id = rs.getInt(1);
                String productCode = rs.getString(2);
                String message = rs.getString(3);
                String scheduledDate = rs.getString(4);

                logger.info("Content for " + productCode + " scheduled for " + scheduledDate);
                prepareToSend(productCode, message);
                pstmt.setInt(1, SMSSENT);
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
            logger.error(e.getMessage(), e);
        }
    }

    private void prepareToSend(String productCode, String message) {
        String sql = "SELECT id,msisdn,serviceid,accountid FROM subscription_data WHERE product_code='" + productCode + "' AND update_type=1";
        try {
            PreparedStatement pstmt = connection.prepareStatement("INSERT INTO contenttosend(serviceid,accountid,msisdn,message) VALUES(?,?,?,?)");
            Statement statement = connection.createStatement();
            ResultSet rs1 = statement.executeQuery(sql);
            int count = 0;
            while (rs1.next()) {
                int id = rs1.getInt(1);
                String msisdn = rs1.getString(2);
                int serviceid = rs1.getInt(3);
                int accountid = rs1.getInt(4);

                pstmt.setInt(1, serviceid);
                pstmt.setInt(2, accountid);
                pstmt.setString(3, msisdn);
                pstmt.setString(4, message);
                pstmt.addBatch();

                count++;

                if (count % 100 == 0) {
                    pstmt.executeBatch();
                    count = 0;
                }
            }

            pstmt.executeBatch();

            rs1.close();
            statement.close();


        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
        }
    }

    public void sendContent() {
        String smsSql = "SELECT count(*) FROM contenttosend";
        int numberOfSmses = 0;

        try {
            Statement smsCount = connection.createStatement();
            ResultSet rsSms = smsCount.executeQuery(smsSql);
            while (rsSms.next()) {
                numberOfSmses = rsSms.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


        try {
            Statement statement2 = connection.createStatement();
            ResultSet rs3;

            PreparedStatement pstmtInsert = connection.prepareStatement("INSERT INTO outbox(accountid,message,senderAddress,serviceid) VALUES(?,?,?,?)");
            PreparedStatement pstmtDelete = connection.prepareStatement("DELETE FROM contenttosend WHERE id=?");
            int numberOfLoop = (numberOfSmses / 1000) + 1;

            for (int i = 0; i <= numberOfLoop; i++) {
                String sql = "SELECT id,serviceid,accountid,msisdn,message FROM contenttosend LIMIT 1000;";
                rs3 = statement2.executeQuery(sql);

                System.out.println("Batch "+i);

                int count = 0;
                while (rs3.next()) {
                    int id = rs3.getInt(1);
                    int serviceid = rs3.getInt(2);
                    int accountid = rs3.getInt(3);
                    String msisdn = rs3.getString(4);
                    String message = rs3.getString(5);
                    pstmtInsert.setInt(1, accountid);
                    pstmtInsert.setString(2, message);
                    pstmtInsert.setString(3, msisdn);
                    pstmtInsert.setInt(4, serviceid);

                    count++;
                    pstmtInsert.addBatch();

                    pstmtDelete.setInt(1, id);

                    pstmtDelete.addBatch();

                    if (count % 100 == 0) {
                        pstmtInsert.executeBatch();
                        pstmtDelete.executeBatch();
                        count = 0;
                    }

                }

                pstmtInsert.executeBatch();
                pstmtDelete.executeBatch();
            }

            System.out.println("Complete");
        } catch (SQLException e) {
            logger.debug(e.getMessage(), e);
        }
    }
}
