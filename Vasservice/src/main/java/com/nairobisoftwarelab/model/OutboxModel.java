package com.nairobisoftwarelab.model;

/**
 * Created by pc on 10/18/2016.
 */
public class OutboxModel {
    private int id;
    private String spid;
    private String password;
    private String message;
    private String sender_address;
    private String oa;
    private String fa;
    private String link_id;
    private String service_id;
    private String sentDate;
    private String correlator;
    private String request_identifier;
    private String delivery_status;
    private String delivery_date;
    private String sub_req_id;
    private String trace_unique_id;
    private String status;
    private String ssan;

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

    public String getSender_address() {
        return sender_address;
    }

    public void setSender_address(String sender_address) {
        this.sender_address = sender_address;
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

    public String getLink_id() {
        return link_id;
    }

    public void setLink_id(String link_id) {
        this.link_id = link_id;
    }

    public String getService_id() {
        return service_id;
    }

    public void setService_id(String service_id) {
        this.service_id = service_id;
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

    public String getRequest_identifier() {
        return request_identifier;
    }

    public void setRequest_identifier(String request_identifier) {
        this.request_identifier = request_identifier;
    }

    public String getDelivery_status() {
        return delivery_status;
    }

    public void setDelivery_status(String delivery_status) {
        this.delivery_status = delivery_status;
    }

    public String getDelivery_date() {
        return delivery_date;
    }

    public void setDelivery_date(String delivery_date) {
        this.delivery_date = delivery_date;
    }

    public String getSub_req_id() {
        return sub_req_id;
    }

    public void setSub_req_id(String sub_req_id) {
        this.sub_req_id = sub_req_id;
    }

    public String getTrace_unique_id() {
        return trace_unique_id;
    }

    public void setTrace_unique_id(String trace_unique_id) {
        this.trace_unique_id = trace_unique_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSsan() {
        return ssan;
    }

    public void setSsan(String ssan) {
        this.ssan = ssan;
    }
}
