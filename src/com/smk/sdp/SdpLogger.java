package com.smk.sdp;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class SdpLogger {
	public static Logger LOGGER = null;

	public SdpLogger(Object obj) {
		File file = new File(System.getProperty("user.dir")
				+ System.getProperty("file.separator") + "log4j.properties");
		Properties props = new Properties();
		try {
			props.load(new FileInputStream(file));
			PropertyConfigurator.configure(props);
			LOGGER = Logger.getLogger(obj.toString());
		} catch (Exception e) {
			LOGGER.debug("SDP Properties", e);
		}
		LOGGER.debug("Sdp notification starting");
	}
}
