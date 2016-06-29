package com.smk.sdp.model;

/**
 * Created by Martin on 26/01/2016.
 */
public class Endpoint {
    private int id;
    private String endpointName;
    private String url;
    private String interfacename;
    private int status;

    public Endpoint(int id,String endpointName,String url, String interfaceName, int status){
        this.id = id;
        this.endpointName = endpointName;
        this.url = url;
        this.interfacename = interfaceName;
        this.status =  status;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEndpointName() {
        return endpointName;
    }

    public void setEndpointName(String endpointName) {
        this.endpointName = endpointName;
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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
