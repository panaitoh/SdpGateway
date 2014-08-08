package com.smk.sdp;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.smk.sdp.smssender.StartSMSSender;

public class PasswordGenerator {

	public static void main(String args[]) {
		Calendar calendar2 = Calendar.getInstance();
		calendar2.setTime(new Date());
		calendar2.add(Calendar.HOUR_OF_DAY, +7);

		Date t = calendar2.getTime();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		System.out.println(df.format(t));
	}

	public static String getTimeStamp() {
		Calendar calendar2 = Calendar.getInstance();
		calendar2.setTime(new Date());
		calendar2.add(Calendar.HOUR_OF_DAY, +7);

		Date date = calendar2.getTime();

		// Date date = new Date();
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
		return df.format(date);
	}

	public static String getPassword(String spid, String sppass, String time) {
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
			StartSMSSender.LOGGER.info("Java security", e);
		}
		
		System.out.println("Password = " + mypassword);
		return mypassword;
	}
}
