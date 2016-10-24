package com.nairobisoftwarelab.model;

/**
 * Created by pc on 10/18/2016.
 */
public class ServiceModel {
    private int id;
    private String serviceid;
    private String smsserviceactivationnumber;
    private String criteria;
    private String spid;
    private String password;
    private String correlator;
    private int status;
    private String datecreated;
    private String dateactivated;
    private String datedeactivated;

    public String getServiceid() {
        return serviceid;
    }

    public void setServiceid(String serviceid) {
        this.serviceid = serviceid;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSmsServiceActivationNumber() {
        return smsserviceactivationnumber;
    }

    public void setSmsservicesctivationnumber(String smsservicesctivationnumber) {
        this.smsserviceactivationnumber = smsservicesctivationnumber;
    }

    public String getCriteria() {
        return criteria;
    }

    public void setCriteria(String criteria) {
        this.criteria = criteria;
    }

    public String getSpid() {
        return spid;
    }

    public void setSpid(String spid) {
        this.spid = spid;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCorrelator() {
        return correlator;
    }

    public void setCorrelator(String correlator) {
        this.correlator = correlator;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getDateCreated() {
        return datecreated;
    }

    public void setDateCreated(String dateCreated) {
        this.datecreated = dateCreated;
    }

    public String getDateActivated() {
        return dateactivated;
    }

    public void setDateActivated(String dateActivated) {
        this.dateactivated = dateActivated;
    }

    public String getDateDeactivated() {
        return datedeactivated;
    }

    public void setDateDeactivated(String dateDeactivated) {
        this.datedeactivated = dateDeactivated;
    }
}
