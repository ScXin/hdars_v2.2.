package com.hlsii.util;

import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * Network utility
 *
 */
public class NetworkUtil {
    private static final String UNKNOWN = "unknown";
    private static final String COMMAS = ",";
    private NetworkUtil() {}

    /**
     * Get remote IP address from the request
     *
     * @param request - HttpServletRequest
     * @return - remote IP
     */
    public static String getRemoteIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip != null && ip.length() != 0 && !UNKNOWN.equalsIgnoreCase(ip) && ip.indexOf(COMMAS) != -1) {
            // Each proxy will have one more IP, the first one is the real remote IP.
            ip = ip.split(COMMAS)[0];
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }

        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }

        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    /**
     * Get all request parameters for a HttpServletRequest
     *
     * @param req
     *            - HttpServletRequest
     * @return - all request parameters
     */
    @SuppressWarnings("unchecked")
    public static Map<String, List<String>> getRequestParms(HttpServletRequest req) {
        Map<String, List<String>> parmsMap = new HashMap<>();
        Enumeration<String> parms = req.getParameterNames();
        while (parms.hasMoreElements()) {
            String paramName = parms.nextElement();
            List<String> paramValues = Arrays.asList(req.getParameterValues(paramName));
            if (!paramValues.isEmpty()) {
                if (parmsMap.containsKey(paramName)) {
                    paramValues.addAll(parmsMap.get(paramName));
                }
                parmsMap.put(paramName, paramValues);
            }
        }
        return parmsMap;
    }

    /**
     * Get the request uri (not include the host) for a HttpServletRequest
     *
     * @param req - HttpServletRequest
     * @param rootPath - the rootPath before the URI
     * @return - request uri
     */
    public static String getRequireURI(HttpServletRequest req, String rootPath) {
        String uri = req.getRequestURI();
        if (StringUtils.isNotEmpty(rootPath)) {
            int startIdx = uri.indexOf(rootPath);
            uri = uri.substring(startIdx + rootPath.length());
        }
        return uri;
    }
}
