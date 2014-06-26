package com.smk.sdp.data.client;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;

import com.smk.sdp.smssender.StartSMSSender;

public class DBConnection {
	public static Connection getConnection() throws FileNotFoundException,
			IOException, ClassNotFoundException, SQLException {
		Connection conn = null;
		String path = System.getProperty("user.dir");
		File file = new File(path + System.getProperty("file.separator")
				+ "config.conf");
		Properties props = new Properties();
		props.load(new FileReader(file));
		String username = props.getProperty("database-username");
		String password = props.getProperty("database-password");
		String server = props.getProperty("database-host");
		String database = props.getProperty("database-name");
		String port = props.getProperty("database-port");

		StartSMSSender.LOGGER.info("Starting connection database");
		Class.forName("com.mysql.jdbc.Driver");
		String url = "jdbc:mysql://" + server + ":" + port + "/" + database;
		conn = DriverManager.getConnection(url + "?user=" + username
				+ "&password=" + password);
		System.err.println("Connected!!");
		return conn;
	}

	public static void closeConnection(Connection conn) {
		System.err.println("Closed database connection");
		try {
			if (conn != null) {
				conn.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}
}
