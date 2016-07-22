package com.nairobisoftwarelab.util;

import com.nairobisoftwarelab.sms.LogManager;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by Martin on 23/01/2016.
 */
public abstract class DatabaseProperties {
    private String username;
    private String password;
    private String url;

    private int maxPoolSize;
    private int minPoolSize;
    private int increment;

    private Logger logger;

    public DatabaseProperties() {
        logger = new LogManager(this.getClass()).getLogger();
        try {
            String path = System.getProperty("user.dir");
            File file = new File(path + System.getProperty("file.separator") + "Vasservice/config.conf");
            Properties props = new Properties();
            props.load(new FileReader(file));
            setUsername(props.getProperty("database-username"));
            setPassword(props.getProperty("database-password"));
            String server = props.getProperty("database-host");
            String database = props.getProperty("database-name");
            String port = props.getProperty("database-port");

            String increment = props.getProperty("pool-increment");
            if (increment == null) {
                setIncrement(1);
            } else {
                setIncrement(Integer.parseInt(increment));
            }

            String maxpoolsize = props.getProperty("max-pool-size");
            if (maxpoolsize == null) {
                setMaxPoolSize(10);
            } else {
                setMaxPoolSize(Integer.parseInt(maxpoolsize));
            }

            String minpoolsize = props.getProperty("min-pool-size");
            if (minpoolsize == null) {
                setMinPoolSize(5);
            } else {
                setMinPoolSize(Integer.parseInt(minpoolsize));
            }

            String params ="useUnicode=true&characterEncoding=UTF-8&rewriteBatchedStatements=true";
            String url = "jdbc:mysql://" + server + ":" + port + "/" + database + "?" + params;
            setUrl(url);
        } catch (FileNotFoundException e) {
            logger.error(e.getMessage(),  e);
        } catch (IOException e) {
            logger.error(e.getMessage(),  e);
        }
    }

    public int getMaxPoolSize() {
        return maxPoolSize;
    }

    public void setMaxPoolSize(int maxPoolSize) {
        this.maxPoolSize = maxPoolSize;
    }

    public int getMinPoolSize() {
        return minPoolSize;
    }

    public void setMinPoolSize(int minPoolSize) {
        this.minPoolSize = minPoolSize;
    }

    public int getIncrement() {
        return increment;
    }

    public void setIncrement(int increment) {
        this.increment = increment;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
