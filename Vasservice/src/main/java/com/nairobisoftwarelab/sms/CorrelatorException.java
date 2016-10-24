package com.nairobisoftwarelab.sms;

/**
 * Created by pc on 10/18/2016.
 */
public class CorrelatorException extends Exception {
    private String message;

    public CorrelatorException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
