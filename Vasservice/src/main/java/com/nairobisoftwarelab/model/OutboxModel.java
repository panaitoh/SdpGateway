package com.nairobisoftwarelab.model;

/**
 * Created by pc on 10/18/2016.
 */
public class OutboxModel {
    private int id;
    private String spid;
    private String password;
    private String message;
    private String senderaddress;
    private String oa;
    private String fa;
    private String linkid;
    private String serviceid;
    private String sentDate;
    private String correlator;
    private String requestidentifier;
    private String deliverystatus;
    private String deliverydate;
    private String subreqid;
    private String traceuniqueid;
    private String status;
    private String smsserviceactivationnumber;

    public String getSmsServiceActivationNumber() {
        return smsserviceactivationnumber;
    }

    public void setSmsServiceActivationNumber(String smsServiceActivationNumber) {
        this.smsserviceactivationnumber = smsServiceActivationNumber;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSenderAddress() {
        return senderaddress;
    }

    public void setSenderAddress(String senderAddress) {
        this.senderaddress = senderAddress;
    }

    public String getOa() {
        return oa;
    }

    public void setOa(String oa) {
        this.oa = oa;
    }

    public String getFa() {
        return fa;
    }

    public void setFa(String fa) {
        this.fa = fa;
    }

    public String getLinkid() {
        return linkid;
    }

    public void setLinkid(String linkid) {
        this.linkid = linkid;
    }

    public String getServiceid() {
        return serviceid;
    }

    public void setServiceid(String serviceid) {
        this.serviceid = serviceid;
    }

    public String getSentDate() {
        return sentDate;
    }

    public void setSentDate(String sentDate) {
        this.sentDate = sentDate;
    }

    public String getCorrelator() {
        return correlator;
    }

    public void setCorrelator(String correlator) {
        this.correlator = correlator;
    }

    public String getRequestIdentifier() {
        return requestidentifier;
    }

    public void setRequestIdentifier(String requestIdentifier) {
        this.requestidentifier = requestIdentifier;
    }

    public String getDeliveryStatus() {
        return deliverystatus;
    }

    public void setDeliveryStatus(String deliveryStatus) {
        this.deliverystatus = deliveryStatus;
    }

    public String getDeliveryDate() {
        return deliverydate;
    }

    public void setDeliveryDate(String deliveryDate) {
        this.deliverydate = deliveryDate;
    }

    public String getSubreqid() {
        return subreqid;
    }

    public void setSubreqid(String subreqid) {
        this.subreqid = subreqid;
    }

    public String getTraceuniqueid() {
        return traceuniqueid;
    }

    public void setTraceuniqueid(String traceuniqueid) {
        this.traceuniqueid = traceuniqueid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
