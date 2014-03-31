package com.mato.client;

import java.text.SimpleDateFormat;
import java.util.Date;

public class PasswordGenerator {

	public String getTimeStamp() {
		Date date = new Date();
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
		return df.format(date);
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
			SdpMain.logger.info("Java security", e);
		}

		return mypassword;
	}
}
