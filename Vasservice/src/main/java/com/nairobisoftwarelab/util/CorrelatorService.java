package com.nairobisoftwarelab.util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by pc on 10/18/2016.
 */
public class CorrelatorService {
    private ILogManager logManager = new LogManager(this);

    public int getCurrentCorrelator(Connection connection) {
        String sql = "SELECT MAX(id) FROM correlator order by id desc limit 1";
        int correlator = 0;

        try {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()) {
                correlator = rs.getInt(1);
            }

            return correlator;
        } catch (SQLException ex) {
            logManager.error(ex.getMessage(), ex);
        }

        return 0;
    }

    public String formatCorrelator(int correlator){
        Integer i = Integer.valueOf(correlator);
        String format = "%1$010d";
        String newCorrelator = String.format(format, i);
        return newCorrelator;
    }

    public void SaveCurrentCorrelator(Connection connection, int correlator) {
        Statement stmt;
        try {
            stmt = connection.createStatement();
            stmt.execute("INSERT INTO correlator(id) VALUES("+correlator+")");
        } catch (SQLException e) {
            logManager.error(e.getMessage(), e);
        }
    }
}
