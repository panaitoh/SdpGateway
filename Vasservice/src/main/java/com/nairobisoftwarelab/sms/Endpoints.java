package com.nairobisoftwarelab.sms;

import com.google.gson.reflect.TypeToken;
import com.nairobisoftwarelab.Database.QueryRunner;
import com.nairobisoftwarelab.model.EndpointModel;
import com.nairobisoftwarelab.util.ILogManager;
import com.nairobisoftwarelab.util.LogManager;
import com.nairobisoftwarelab.util.Status;

import java.lang.reflect.Type;
import java.sql.Connection;
import java.util.List;

public class Endpoints {
    public static Endpoints getInstance = new Endpoints();
    private ILogManager logManager;
    private List<EndpointModel> endpoint;
    private Type type = new TypeToken<List<EndpointModel>>() {
    }.getType();

    private Endpoints() {
        logManager = new LogManager(this);
    }

    public List<EndpointModel> getEndPoints(Connection connection) {
        if (endpoint != null) {
            return endpoint;
        }

        String query = "SELECT id, endpoint_name, url, interface_name, status FROM vasmaster_service.endpoints WHERE status = " + Status.STATUS_ACTIVE;
        QueryRunner<EndpointModel> queryRunner = new QueryRunner<>(connection, query);
        endpoint = queryRunner.getList(type);
        return endpoint;

    }
}
