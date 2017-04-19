package com.nairobisoftwarelab.sms;

import com.nairobisoftwarelab.util.CorrelatorService;
import com.nairobisoftwarelab.util.DBConnection;

import java.sql.*;

/**
 * Created by martin.lugaliki on 4/19/2017.
 */
public class BulkHack {
    public static void  main(String[] args)
    {
        DBConnection dbConnection = new DBConnection();
        Connection connection = dbConnection.getConnection();
        String sql = "SELECT msisdn, content_id, message, service_id FROM vasmaster_content.send_bulk_messages where content_id=3";

        try {
            connection.setAutoCommit(false);
            PreparedStatement pstmtoutbox = connection.prepareStatement("INSERT INTO outbox(message, " +
                    "sender_address, service_id, content_id, correlator) VALUES(?,?,?,?,?)");

            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            CorrelatorService correlatorService = new CorrelatorService();
            int correlator = correlatorService.getCurrentCorrelator(connection);
            while(rs.next()){
                int cor = correlator++;
                String msisdn = rs.getString(1);
                String content_id = rs.getString(2);
                String message = rs.getString(3);
                String service_id = rs.getString(4);

                pstmtoutbox.setString(1, message);
                pstmtoutbox.setString(2, "254"+msisdn.substring(msisdn.trim().length()-9));
                pstmtoutbox.setString(3, service_id);
                pstmtoutbox.setString(4, content_id);
                pstmtoutbox.setString(5, correlatorService.formatCorrelator(cor));
                correlatorService.SaveCurrentCorrelator(connection, cor);
                pstmtoutbox.addBatch();
            }

            pstmtoutbox.executeBatch();
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
