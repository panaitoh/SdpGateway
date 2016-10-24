package com.nairobisoftwarelab.util;

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
        t.append(spId).append(password).append(timestamp);
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
