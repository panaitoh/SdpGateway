package com.nairobisoftwarelab.sms.Exceptions;

/**
 * Created by pc on 10/18/2016.
 */
public class SdpEndpointException extends Exception {
    private String message;

    public SdpEndpointException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
