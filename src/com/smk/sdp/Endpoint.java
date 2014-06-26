package com.smk.sdp;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.smk.sdp.data.client.DBConnection;

public class Endpoint {

	public Endpoint() {
	}

	public static Map<String, String> getEndPoint(String name) {
		String sql = "SELECT url,interfaceName FROM endpoint WHERE endpointName =?;";
		PreparedStatement pstmt = null;
		Connection connect = null;
		ResultSet rs = null;
		Map<String, String> endpoint = new HashMap<String, String>();
		try {
			connect = DBConnection.getConnection();
			pstmt = connect.prepareStatement(sql);
			pstmt.setString(1, name);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				endpoint.put("url", rs.getString(1));
				endpoint.put("ïnterface", rs.getString(2));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
				if (connect != null) {
					connect.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return endpoint;
	}
}
