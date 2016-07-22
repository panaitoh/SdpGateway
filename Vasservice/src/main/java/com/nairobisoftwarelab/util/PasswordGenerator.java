package com.nairobisoftwarelab.util;

import com.nairobisoftwarelab.sms.LogManager;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class PasswordGenerator{
	private DatabaseManager databaseManager;
	private Logger logger;
	public static PasswordGenerator instance = new PasswordGenerator();
	private PasswordGenerator(){
		databaseManager = new DatabaseManager();
		logger = new LogManager(this.getClass()).getLogger();

	}
	public static void main(String args[]) {
		/*Calendar calendar2 = Calendar.getInstance();
		calendar2.setTime(new Date());
		calendar2.add(Calendar.HOUR_OF_DAY, +7);

		Date t = calendar2.getTime();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		System.out.println(df.format(t));*/

	}

	public int generateCorrelator(Connection connection){
		String sql = "SELECT correlator from correlator order by id desc limit 1";
		int correlator =0;
		try {
			Statement statement = connection.createStatement();
			ResultSet rs = statement.executeQuery(sql);
			while (rs.next()) {
				correlator = rs.getInt(1);
			}

			correlator++;
			if(correlator ==1){
				correlator = 1000;
			}
			String updateCor ="INSERT INTO correlator(correlator) VALUES("+correlator+")";

			Statement st = connection.createStatement();
			st.execute(updateCor);
			st.close();

			return correlator;
		}catch (SQLException ex){
			logger.debug(ex.getMessage(),ex);
		}
		return 0;
	}

	public String getPassword(String spid, String sppass, String time) {
		String pass = spid.trim() + sppass.trim() + time.trim();
		String mypassword = "";
		try {
			java.security.MessageDigest md = java.security.MessageDigest
					.getInstance("MD5");
			byte[] array = md.digest(pass.getBytes());
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < array.length; ++i) {
				sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100)
						.substring(1, 3));
			}
			mypassword = sb.toString().trim();
		} catch (java.security.NoSuchAlgorithmException e) {
			logger.info("Java security", e);
		}
		return mypassword;
	}
}
