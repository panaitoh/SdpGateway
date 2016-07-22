package com.nairobisoftwarelab.sms;

import com.nairobisoftwarelab.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by pc on 14/07/2016.
 */
public class Uploader {
    public static void main(String[] args){
        DBConnection dbConnection = new DBConnection();
        Connection connection = dbConnection.getConnection();
        String sql = "SELECT * FROM data";
        int count =0;
        try {
            PreparedStatement pstmt = connection.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();

            String insertSql = "INSERT INTO subscription_data(id,msisdn,accountid,serviceid,update_type,product_code) VALUES(?,?,?,?,?,?)";
            PreparedStatement ps = connection.prepareStatement(insertSql);
            int id =2264;
            while(rs.next()){
                System.out.println("Inserting "+rs.getString(1));
                ps.setInt(1,id);
                ps.setString(2,rs.getString(1));
                ps.setInt(3,2);
                ps.setInt(4,10);
                ps.setInt(5,1);
                ps.setString(6,"MDSP2000268052");

                ps.addBatch();
                count++;

                if (count % 100 == 0) {
                    ps.executeBatch();
                    count = 0;
                }
                id ++;
            }
            ps.executeBatch();

            System.out.println("Done Inserting ");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
