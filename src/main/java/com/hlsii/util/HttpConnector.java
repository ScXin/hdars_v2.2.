package com.hlsii.util;


import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * HTTP connection utility to send a request to remote
 *
 */
public class HttpConnector {
    private static Logger logger = LoggerFactory.getLogger(HttpConnector.class);

    private HttpConnector() {}

    /**
     * Send the request to remote url
     *
     * @param requestUrl - request url
     * @param parms - parameters
     * @param method - request method
     * @return - Response body text
     * @throws IOException
     */
    public static String sendRequestToRemote(String requestUrl, Map<String, List<String>> parms, String method)
            throws IOException {
        StringBuilder urlSb = new StringBuilder(requestUrl);
        String respBody = null;
        HttpClient httpClient = new HttpClient();
        HttpMethod httpMethod;

        if ("GET".equalsIgnoreCase(method) || parms.size() == 0) {
            int parmCount = 0;
            for (Map.Entry<String, List<String>> entry : parms.entrySet()) {
                for(String parmVal : entry.getValue()) {
                    if (parmCount == 0) {
                        urlSb.append("?");
                    } else if (parmCount > 0) {
                        urlSb.append("&");
                    }
                    parmCount++;
                    urlSb.append(entry.getKey()).append("=").append(parmVal);
                }
            }
            httpMethod = new GetMethod(urlSb.toString());
        } else {
            PostMethod postMethod = new PostMethod(urlSb.toString());
            postMethod.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
            for (Map.Entry<String, List<String>> entry : parms.entrySet()) {
                for(String val : entry.getValue()) {
                    postMethod.addParameter(new NameValuePair(entry.getKey(), val));
                }
            }
            httpMethod = postMethod;
        }
        httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(300000);
        httpClient.getHttpConnectionManager().getParams().setSoTimeout(300000);
        try {
            httpClient.executeMethod(httpMethod);
            InputStream inputStream = httpMethod.getResponseBodyAsStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
            StringBuffer stringBuffer = new StringBuffer();
            String str = "";
            while((str = br.readLine()) != null) {
                stringBuffer.append(str);
            }
            respBody = stringBuffer.toString();
        }
        catch (Exception e) {
            logger.error("Exception at  {}, e: {}", requestUrl, e.toString());
            throw e;
        }
        return respBody;
    }

    /**
     * Send the request to remote url
     *
     * @param requestUrl - request url
     * @param parms - parameters
     * @return - Response body text
     * @throws IOException
     */
    public static String sendRequestToRemote(String requestUrl, Map<String, List<String>> parms) throws IOException {
        return sendRequestToRemote(requestUrl, parms, "GET");
    }

    /**
     * Send the request to remote url
     *
     * @param requestUrl - request url
     * @return - Response body text
     * @throws IOException
     */
    public static String sendRequestToRemote(String requestUrl) throws IOException {
        Map<String, List<String>> parms = new HashMap<>();
        return sendRequestToRemote(requestUrl, parms);
    }
}