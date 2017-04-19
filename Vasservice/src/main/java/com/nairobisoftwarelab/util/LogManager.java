package com.nairobisoftwarelab.util;

import org.apache.log4j.PropertyConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

/**
 * Created by pc on 10/5/2016.
 */
public class LogManager implements ILogManager {
    private Logger logger;

    public LogManager(Object object) {
        File file = new File(System.getProperty("user.dir")
                + System.getProperty("file.separator") + "log4j.properties");
        Properties props = new Properties();
        try {
            props.load(new FileInputStream(file));
            PropertyConfigurator.configure(props);
            logger = LoggerFactory.getLogger(object.getClass());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void warning(String message) {
        logger.warn(message);
    }

    @Override
    public void debug(String message) {
        logger.debug(message);
    }

    @Override
    public void error(String message, Throwable throwable) {
        logger.error(message, throwable);
    }

    @Override
    public void info(String message) {
        logger.info(message);
    }
}
