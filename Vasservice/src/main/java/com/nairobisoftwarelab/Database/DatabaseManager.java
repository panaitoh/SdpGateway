package com.nairobisoftwarelab.Database;

import com.nairobisoftwarelab.util.ILogManager;
import com.nairobisoftwarelab.util.LogManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Created by Martin on 23/01/2016.
 */
public class DatabaseManager implements IDatabaseManager {

    public static DatabaseManager getInstance = new DatabaseManager();
    private ILogManager logManager;
    private Connection connection;
    private String username;
    private String password;
    private String server;
    private String database;
    private String port;

    private DatabaseManager() {
        logManager = new LogManager(this);
        try {
            String path = System.getProperty("user.dir");
            File file = new File(path + System.getProperty("file.separator") + "config.conf");
            Properties props = new Properties();
            props.load(new FileReader(file));
            setUsername(props.getProperty("database-username"));
            setPassword(props.getProperty("database-password"));
            setServer(props.getProperty("database-host"));
            setDatabase(props.getProperty("database-name"));
            setPort(props.getProperty("database-port"));
        } catch (FileNotFoundException e) {
            logManager.error(e.getMessage(), e);
        } catch (IOException e) {
            logManager.error(e.getMessage(), e);
        }
    }

    @Override
    public Connection getConnection() {
        try {

            if (connection != null) {
                System.err.println("Old connection");
                return connection;
            }

            System.err.println("New connection");
            String url = "jdbc:jtds:sqlserver://" + getServer() + ":" + getPort() + "/" + getDatabase() + ";instance=SQLEXPRESS;TDS=7.0;user=" + getUsername() + ";password=" + getPassword();
            logManager.debug("Database URL => " + url);
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            connection = DriverManager.getConnection(url);
            return connection;
        } catch (ClassNotFoundException e) {
            logManager.error(e.getMessage(), e);
        } catch (SQLException e) {
            logManager.error(e.getMessage(), e);
        }

        return null;
    }

    @Override
    public void closeConnection() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException ex) {
            logManager.error(ex.getMessage(), ex);
        }
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
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
}
