package com.nairobisoftwarelab.util;

/**
 * Created by pc on 10/18/2016.
 */
public enum Status {
    STATUS_PENDING(0),
    STATUS_READY(1),
    STATUS_ACTIVE(2),
    STOP_PENDING(3),
    STATUS_STOPPED(4);


    private final int status;

    Status(int status) {
        this.status = status;
    }

    public int getStatus() {
        return this.status;
    }
}
