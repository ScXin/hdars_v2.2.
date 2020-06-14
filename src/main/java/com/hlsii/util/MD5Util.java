package com.hlsii.util;

import java.security.MessageDigest;

/**
 * MD5 encrypt utility
 *
 */
public class MD5Util {
    private MD5Util() {}

    // 16 hex digits
    private static final String[] hexDigits = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9",
            "A", "B", "C", "D", "E", "F"};

    /**
     * Encode a plain text to MD5 string
     *
     * @param inputStr - plain text
     * @return- MD5 encoded string
     */
    public static String md5(String inputStr){
        return encodeByMD5(inputStr);
    }

    /**
     * Encode a plain text to MD5 string
     *
     * @param originString - plain text
     * @return- MD5 encoded string
     */
    private static String encodeByMD5(String originString){
        if (originString!=null) {
            try {
                MessageDigest md5 = MessageDigest.getInstance("MD5");
                byte[] results = md5.digest(originString.getBytes());
                return byteArrayToHexString(results);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * Convert 128 bit value to 32 digits
     * @param b - 128 bit value
     * @return - digits
     */
    private static String byteArrayToHexString(byte[] b){
        StringBuilder resultSb = new StringBuilder();
        for(int i=0; i<b.length; i++){
            resultSb.append(byteToHexString(b[i]));
        }
        return resultSb.toString();
    }

    /**
     * Convert a byte value to hex digit
     *
     * @param b - byte value
     * @return - hex digit
     */
    private static String byteToHexString(byte b){
        int n = b;
        if (n < 0) n = 256 + n;
        int d1 = n / 16;
        int d2 = n % 16;
        return hexDigits[d1] + hexDigits[d2];
    }
}
