package com.nairobisoftwarelab.model;

/**
 * Created by Martin on 26/01/2016.
 */
public class EndpointModel {
    private int id;
    private String endpointname;
    private String url;
    private String interfacename;
    private boolean status;

    public String getEndpointname() {
        return endpointname;
    }

    public void setEndpointname(String endpointname) {
        this.endpointname = endpointname;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getInterfacename() {
        return interfacename;
    }

    public void setInterfacename(String interfacename) {
        this.interfacename = interfacename;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
