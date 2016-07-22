package com.nairobisoftwarelab.util;

import com.nairobisoftwarelab.sms.LogManager;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by Martin on 23/01/2016.
 */
public class DatabaseManager extends DBConnection {
    private Logger logger;

    public DatabaseManager(){
        logger = new LogManager(this.getClass()).getLogger();
    }

    public ResultSet RunQuery(String sql){
        Connection connection = getConnection();
        try {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(sql);

            return rs;
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    public ResultSet RunQuery(String sql, Connection connection){
        try {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(sql);

            return rs;
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    public String getResponseMessage(String servId, Connection connection){
        ResultSet rs = RunQuery("SELECT welcome FROM products p inner join services s on p.serviceId = s.id where s.serviceid='"+servId+"' limit 1", connection);
        String response = "Thank you for using our services";
        try {
            while(rs.next()){
                response = rs.getString(1).isEmpty() ? response : rs.getString(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return response;
    }

    public boolean sendSms(String serviceid){
        return true;
    }

    public void saveStatus(String sql){
        Connection connection = getConnection();
        try {
            Statement statement = connection.createStatement();
            statement.execute(sql);
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
        }finally {
            try {
                if(connection!=null)
                    connection.close();
            } catch (SQLException e) {
                logger.error(e.getMessage(), e);
            }
        }
    }
}
