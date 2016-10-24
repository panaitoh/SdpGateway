package com.nairobisoftwarelab.util;

import com.nairobisoftwarelab.Database.QueryRunner;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by pc on 10/18/2016.
 */
public class Correlator {
    private ILogManager logManager = new LogManager(this);
    private String correlator;

    public Correlator(Connection conn) {
        correlator = getNextCorrelator(conn);
    }

    public String getNextCorrelator(Connection conn) {
        String sql = "SELECT correlator from correlator order by id desc limit 1";
        int correlator = 0;
        try {
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()) {
                correlator = rs.getInt(1);
            }
            correlator++;
            String updateCor = "INSERT INTO correlator(correlator) VALUES(" + correlator + ")";
            new QueryRunner<String>(conn).excuteQuery(updateCor);

            Integer i = Integer.valueOf(correlator);
            String format = "%1$07d";
            String newCorrelator = String.format(format, i);

            logManager.debug("New correlator " + newCorrelator);
            return newCorrelator;
        } catch (SQLException ex) {
            logManager.error(ex.getMessage(), ex);
        }
        return null;
    }

    public String getCorrelator() {
        return correlator;
    }
}
