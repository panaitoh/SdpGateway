package com.nairobisoftwarelab.util;

import com.mchange.v2.c3p0.DataSources;
import com.mchange.v2.c3p0.PooledDataSource;

import javax.sql.DataSource;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class DBConnection extends DatabaseProperties {
    private static PooledDataSource dataSource;

    static {
        try {

            String path = System.getProperty("user.dir");
            File file = new File(path + System.getProperty("file.separator") + "config.conf");
            Properties props = new Properties();
            props.load(new FileReader(file));
            String username = props.getProperty("database-username");
            String password = props.getProperty("database-password");
            String server = props.getProperty("database-host");
            String database = props.getProperty("database-name");
            String port = props.getProperty("database-port");

            String increment = props.getProperty("pool-increment");

            String params = "useUnicode=true&characterEncoding=UTF-8&rewriteBatchedStatements=true";
            String url = "jdbc:mysql://" + server + ":" + port + "/" + database + "?" + params;


            Class.forName("com.mysql.jdbc.Driver");
            DataSource unpooledDs = DataSources.unpooledDataSource(url, username, password);

            Map overrides = new HashMap();
            overrides.put("maxStatements", "50");
            // overrides.put("maxPoolSize", new Integer(getMaxPoolSize()));
            dataSource = (PooledDataSource) DataSources.pooledDataSource(unpooledDs, overrides);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() {
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
