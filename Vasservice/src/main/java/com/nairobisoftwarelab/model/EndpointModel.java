package com.nairobisoftwarelab.model;

/**
 * Created by Martin on 26/01/2016.
 */
public class EndpointModel {
    private int id;
    private String endpoint_name;
    private String url;
    private String interface_name;
    private boolean status;

    public String getEndpointname() {
        return endpoint_name;
    }

    public void setEndpointname(String endpointname) {
        this.endpoint_name = endpointname;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getInterfacename() {
        return interface_name;
    }

    public void setInterfacename(String interfacename) {
        this.interface_name = interfacename;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
