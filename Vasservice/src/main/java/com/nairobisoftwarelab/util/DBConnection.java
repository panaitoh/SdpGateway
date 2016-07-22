package com.nairobisoftwarelab.util;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.mchange.v2.c3p0.DataSources;
import com.mchange.v2.c3p0.PooledDataSource;
import com.nairobisoftwarelab.sms.LogManager;
import org.apache.log4j.Logger;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class DBConnection extends DatabaseProperties {
    private String _username;
    private String _password;
    private String _url;
    private int _maxPoolSize;
    private int _minPoolSize;
    private int _increment;
    private ComboPooledDataSource cpds;
    private Connection connection;

    private Logger logger;

    public DBConnection() {
        logger = new LogManager(this.getClass()).getLogger();
        this._username = getUsername();
        this._password = getPassword();
        this._url = getUrl();
        this._minPoolSize = getMinPoolSize();
        this._maxPoolSize = getMaxPoolSize();
        this._increment = getIncrement();
        cpds = new ComboPooledDataSource();
    }

    public PooledDataSource getDataSource() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            DataSource unpooledDs = DataSources.unpooledDataSource(getUrl(), _username, _password);

            Map overrides = new HashMap();
            overrides.put("maxStatements", "50");
            overrides.put("maxPoolSize", new Integer(_maxPoolSize));
            PooledDataSource pooledDs = (PooledDataSource) DataSources.pooledDataSource(unpooledDs, overrides);
            return pooledDs;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
    public Connection getConnection() {
        // closeConnection();
        Connection connection = null;
        try {
            connection = getDataSource().getConnection();
            return connection;
        }  catch (SQLException e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }


    private void closeConnection() {
        try {
            if (getDataSource() != null) {
                DataSources.destroy(getDataSource());
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
        }
    }
}
