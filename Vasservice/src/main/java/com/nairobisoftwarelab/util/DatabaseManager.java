
package com.nairobisoftwarelab.util;

import com.google.gson.reflect.TypeToken;
import com.nairobisoftwarelab.Database.QueryRunner;

import java.lang.reflect.Type;
import java.sql.Connection;
import java.util.List;


/**
 * Created by Martin on 23/01/2016.
 */

public class DatabaseManager<T> {
    private Type type = new TypeToken<List<T>>() {
    }.getType();

    protected List<T> getAll(Connection connection, String query) {
        QueryRunner<T> runner = new QueryRunner<>(connection, query);
        return runner.getList(type);
    }

    protected T get(Connection connection, String query) {
        List<T> items = getAll(connection, query);

        if (items != null && items.size() > 0) {
            return items.get(0);
        }
        return null;
    }
}
