package com.nairobisoftwarelab.Database;

import com.google.gson.Gson;
import com.nairobisoftwarelab.util.ILogManager;
import com.nairobisoftwarelab.util.LogManager;
import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by pc on 10/5/2016.
 */
public class QueryRunner<T> {
    private Connection connection;
    private String query;
    private Gson gson;
    private ILogManager logManager;

    public QueryRunner(Connection conn, String q) {
        this.connection = conn;
        this.query = q;
        gson = new Gson();
        logManager = new LogManager(this);
    }

    public QueryRunner(Connection conn) {
        this.connection = conn;
    }

    public List<T> getList(Type type) {
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            String json = getFormattedResult(resultSet);
            List<T> ques = gson.fromJson(json, type);

            return ques;
        } catch (SQLException e) {
            logManager.error(e.getMessage(), e);
        }
        return null;
    }

    public String getFormattedResult(ResultSet rs) {
        try {
            JSONArray jsonArray = new JSONArray();
            ResultSetMetaData rsMeta = rs.getMetaData();
            int columnCnt = rsMeta.getColumnCount();
            List<String> columnNames = new ArrayList<String>();
            for (int i = 1; i <= columnCnt; i++) {
                columnNames.add(rsMeta.getColumnName(i).toUpperCase());
            }
            while (rs.next()) { // convert each object to an human readable JSON object
                JSONObject obj = new JSONObject();
                for (int i = 1; i <= columnCnt; i++) {
                    String key = columnNames.get(i - 1);
                    Object value = rs.getString(i);
                    obj.put(key.toLowerCase(), value);
                }
                jsonArray.put(obj);
            }
            return jsonArray.toString();
        } catch (Exception e) {
            logManager.error(e.getMessage(), e);
        } finally {
            try {
                rs.close();
            } catch (SQLException e) {
                logManager.error(e.getMessage(), e);
            }
        }
        return null;
    }


    public void excuteQuery(String query) {
        try {
            Statement statement = connection.createStatement();
            boolean success = statement.execute(query);

        } catch (SQLException e) {
            logManager.error(e.getMessage(), e);
        }
    }
}
