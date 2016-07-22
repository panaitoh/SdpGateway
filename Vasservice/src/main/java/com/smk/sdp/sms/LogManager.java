package com.smk.sdp.sms;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class LogManager {
	private Logger LOGGER = null;

	public LogManager(Object obj) {
		String separator = System.getProperty("file.separator");

		File file = new File(System.getProperty("user.dir") + separator + "Vasservice" + separator
				+ separator + "log4j.properties");
		Properties props = new Properties();
		try {
			props.load(new FileInputStream(file));
			PropertyConfigurator.configure(props);
			LOGGER = Logger.getLogger(obj.toString());
		} catch (Exception e) {
			////LOGGER.error(e.getMessage(), e);
			e.printStackTrace();
		}
	}

	public Logger getLogger(){
		return LOGGER;
	}
}
