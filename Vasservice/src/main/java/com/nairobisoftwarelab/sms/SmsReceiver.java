package com.nairobisoftwarelab.sms;

import com.nairobisoftwarelab.util.DatabaseManager;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SmsReceiver implements SdpConstants {
    private DatabaseManager databaseManager;
    private Logger logger;
    private Connection connection;

    public SmsReceiver(Connection connection) {
        databaseManager = new DatabaseManager();
        logger = new LogManager(this.getClass()).getLogger();
        this.connection = connection;
    }

    public void receiver() {
        String sql = "SELECT id,accountid,serviceid,message,senderAddress,linkid FROM inbox  WHERE status=" + SMSNOTSENT + " LIMIT 500";
        logger.debug("receiver() -> Getting new smses");
        try {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()) {
                int id = rs.getInt(1);
                String accountid = rs.getString(2);
                String serviceid = rs.getString(3);
                String message = rs.getString(4);
                String senderAddress = rs.getString(5);
                String linkid = rs.getString(6);

                if (databaseManager.sendSms(serviceid)) {
                    String response = databaseManager.getResponseMessage(serviceid, connection);
                    saveInOutbox(accountid, serviceid, response, senderAddress, linkid, connection);
                }
                String updateSql = "UPDATE inbox SET status =" + SMSSENT + " WHERE id=" + id;
                logger.debug("New sms found from " + senderAddress);
                Statement st = connection.createStatement();
                st.execute(updateSql);
                st.close();
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException ex) {
                logger.error(ex.getMessage(), ex);
            }
        }
    }

    private void saveInOutbox(String accountid, String serviceid, String message, String senderAddress, String linkid, Connection connection) {
        String sql = "INSERT INTO outbox(accountid,message,senderAddress,linkid,serviceid) VALUES('" + accountid + "','" + message + "','" + senderAddress + "','" + linkid + "','" + serviceid + "')";
        logger.error("saveInOutbox() -> sending a response");
        Statement statement = null;
        try {
            statement = connection.createStatement();
            statement.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

}
