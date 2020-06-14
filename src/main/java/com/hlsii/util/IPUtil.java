package com.hlsii.util;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * IP utility
 *
 */
public class IPUtil {
    private static final String dotRegx = "\\.";
    private IPUtil() {}

    /**
     * Check if the ip matches IP pattern (xxx.xxx.xxx.xxx, support * wildcard)
     *
     * Each ip segment can only contain one *, or a number less than 256.
     *
     * @param ip
     * @return true if matched
     */
    public static boolean matchIPPattern(String ip) {
        String regx = "[0-9]{1,3}";
        Pattern pattern = Pattern.compile(regx);
        String[] seg = ip.split(dotRegx);
        if (seg.length != 4) {
            return false;
        }
        for(int i = 0; i < seg.length; i++) {
            if ("*".equals(seg[i])) {
                continue;
            }
            Matcher matcher = pattern.matcher(seg[i]);
            if (!matcher.matches()) {
                return false;
            }
            if (Integer.parseInt(seg[i]) > 255) {
                return false;
            }
        }
        return true;
    }

    /**
     * Check if the ip in the IP pattern
     *
     * @param ip - the real IP
     * @param pattern - IP pattern (support *)
     * @return true if matched
     */
    public static boolean ipInPattern(String ip, String pattern) {
        String[] ipSeg = ip.split(dotRegx);
        String[] patternSeg = pattern.split(dotRegx);
        if (ipSeg.length != 4 || ipSeg.length != patternSeg.length) {
            return false;
        }
        for(int i = 0; i < ipSeg.length; i++) {
            if ("*".equals(patternSeg[i])) {
                continue;
            }
            if (Integer.parseInt(patternSeg[i]) != Integer.parseInt(ipSeg[i])) {
                return false;
            }
        }
        return true;
    }

    /**
     * Check if the host is an IP
     *
     * @param host - host name or IP
     * @return true if host matches xxx.xxx.xxx.xxx
     */
    public static boolean isIPAddresss(String host) {
        String ipRegex = "\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}";
        Pattern pattern = Pattern.compile(ipRegex);
        Matcher matcher = pattern.matcher(host);
        return matcher.matches();
    }

}
