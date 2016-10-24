package com.nairobisoftwarelab.Database;

import java.sql.Connection;

/**
 * Created by pc on 10/5/2016.
 */
public interface IDatabaseManager {
    Connection getConnection();

    void closeConnection();
}
