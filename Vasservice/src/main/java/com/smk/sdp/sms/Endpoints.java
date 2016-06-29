package com.smk.sdp.sms;

import com.smk.sdp.model.Endpoint;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

public class Endpoints {
    private static Logger logger;
    public static Endpoints instance = new Endpoints();
    HashMap<String, Endpoint> endpoint;

    private Endpoints() {
        logger = new LogManager(this.getClass()).getLogger();
    }

    public HashMap<String, Endpoint> getEndPoint(Connection connection) {
        String sql = "SELECT id, endpointName, url, interfacename, status FROM endpoint";
        if (endpoint != null) {
            return endpoint;
        } else {
            endpoint = new HashMap<>();
            try {
                Statement statement = connection.createStatement();
                ResultSet rs = statement.executeQuery(sql);
                while (rs.next()) {
                    int id = rs.getInt(1);
                    String endpointName = rs.getString(2);
                    String url = rs.getString(3);
                    String interfacename = rs.getString(4);
                    int status = rs.getInt(5);
                    endpoint.put(endpointName, new Endpoint(id, endpointName, url, interfacename, status));
                }
                rs.close();
            } catch (SQLException e) {
                logger.error(e.getMessage(), e);
            }
            return endpoint;
        }
    }
}
