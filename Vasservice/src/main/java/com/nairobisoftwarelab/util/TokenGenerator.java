package com.nairobisoftwarelab.util;

import java.security.DigestException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/***
 * Generates security token for sdp access
 */
public class TokenGenerator {
    private ILogManager logManager;
    private String token;

    public TokenGenerator(String spId, String password, String timestamp) {
        logManager = new LogManager(this);
        token = getToken(spId, password, timestamp);
    }


    private String getToken(String spId, String password, String timestamp) {
        StringBuilder t = new StringBuilder();
        t.append(spId.trim()).append(password.trim()).append(timestamp.trim());
        System.out.println(t.toString());
        try {
            java.security.MessageDigest md = java.security.MessageDigest
                    .getInstance("MD5");
            byte[] array = md.digest(t.toString().getBytes());
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < array.length; ++i) {
                sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100)
                        .substring(1, 3));
            }
            return sb.toString().trim();
        } catch (java.security.NoSuchAlgorithmException e) {
            logManager.error(e.getMessage(), e);
        }
        return null;
    }

    public String getToken() {
        return token;
    }
}
