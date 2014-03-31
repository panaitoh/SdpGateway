package com.mato.client;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;

public class DbManager {
	private String username = "";
	private String password = "";
	private String server = "";
	private String database = "";
	private String port = "";
	private Connection conn = null;

	public DbManager() {
		String path = System.getProperty("user.dir");
		File file = new File(path + System.getProperty("file.separator")
				+ "config.conf");
		Properties props = new Properties();
		try {
			props.load(new FileReader(file));
			username = props.getProperty("database-username");
			password = props.getProperty("database-password");
			server = props.getProperty("database-host");
			database = props.getProperty("database-name");
			port = props.getProperty("database-port");
		} catch (FileNotFoundException e) {
			SdpMain.logger.info("FileNotFoundException", e);
			// e.printStackTrace();
		} catch (IOException e) {
			SdpMain.logger.info("IOException", e);
			// e.printStackTrace();
		}

	}

	public Connection getConnection() {
		try {
			SdpMain.logger.info("Starting connection database");
			Class.forName("com.mysql.jdbc.Driver");
			String url = "jdbc:mysql://" + server + ":" + port + "/" + database;
			conn = DriverManager.getConnection(url + "?user=" + username
					+ "&password=" + password);
			System.err.println("Connected!!");
		} catch (SQLException e) {
			SdpMain.logger.info("getconnection", e);
			// e.printStackTrace();
		} catch (ClassNotFoundException cnf) {
			SdpMain.logger.info("getconnection", cnf);
			// cnf.printStackTrace();
		}
		return conn;
	}

}
