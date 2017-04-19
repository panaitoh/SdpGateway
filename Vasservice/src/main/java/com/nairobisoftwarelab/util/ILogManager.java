package com.nairobisoftwarelab.util;

/**
 * Created by pc on 10/5/2016.
 */
public interface ILogManager {
    void warning(String message);

    void debug(String message);

    void error(String message, Throwable throwable);

    void info(String message);
}
