/*
 * HADARS - Cosylab Hadoop-based Accelerator Data Archiver and Retrieval System
 * Copyright (c) 2018 Cosylab d.d.
 *
 * mailto:info AT cosylab DOT com
 * Gerbiceva 64, 1000 Ljubljana, Slovenia
 *
 * This software is distributed under the terms found
 * in file LICENSE-CSL-2.0.txt that is included with this distribution.
 */

package com.cosylab.hadars.archiverappliance.retrieval.client;


import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import javax.xml.bind.DatatypeConverter;
import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;

/**
 * Client side class for retrieving data from the appliance archiver using the PB over HTTP protocol.
 *
 * @author mshankar
 */
public class RawDataRetrieval implements DataRetrieval {
    private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(RawDataRetrieval.class.getName());
    private String retrievalURL;

    public RawDataRetrieval(String retrievalURL) {
        this.retrievalURL = retrievalURL + "/data/getData.raw";
    }

    @Override
    public EventStream getDataForPV(String pvName, Timestamp startTime, Timestamp endTime) {
        return getDataForPV(pvName, startTime, endTime, false, null);
    }

    @Override
    public EventStream getDataForPV(String pvName, Timestamp startTime, Timestamp endTime, boolean useReducedDataSet) {
        return getDataForPV(pvName, startTime, endTime, useReducedDataSet, null);
    }

    @Override
    public EventStream getDataForPV(String pvName, Timestamp startTime, Timestamp endTime, boolean useReducedDataSet, HashMap<String, String> otherParams) {
        // We'll use java.net for now.
        StringWriter buf = new StringWriter();
        String encode;
        try {
            encode=URLEncoder.encode(pvName, "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            encode=pvName;
        }
        buf.append(retrievalURL);
        if(retrievalURL.contains("?")) {
            buf.append("&pv=").append(encode);
        } else {
            buf.append("?pv=").append(encode);
        }
        buf.append("&from=").append(convertToISO8601String(startTime))
                .append("&to=").append(convertToISO8601String(endTime));
        if(useReducedDataSet) {
            buf.append("&usereduced=true");
        }
        if(otherParams != null) {
            for(String key : otherParams.keySet()) {
                buf.append("&");
                buf.append(key);
                buf.append("=");
                buf.append(otherParams.get(key));
            }
        }
        String getURL = buf.toString();
        //logger.info("URL to fetch data is " + getURL);
        try {
            URL url = new URL(getURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            if(connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream is = new BufferedInputStream(connection.getInputStream());
                if(is.available() <= 0) return null;
                return new InputStreamBackedEventStream(is);
            } else {
              //  logger.info("No data found for PV " + pvName + " + using URL " + url.toString());
                return null;
            }
        } catch(Exception ex) {
            logger.error("Exception fetching data from URL " + getURL, ex);
        }
        return null;
    }

    public static String convertToUTC(Timestamp time) {
        try {
            Calendar c = GregorianCalendar.getInstance();
            c.setTime(time);
            return URLEncoder.encode(DatatypeConverter.printDateTime(c), "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            logger.error("Cannot encode times into UTF-8", ex);
            return null;
        }
    }

    public static String convertToISO8601String(java.sql.Timestamp ts) {
        DateTimeFormatter fmt = ISODateTimeFormat.dateTime();
        DateTime dateTime = new DateTime(ts.getTime(), DateTimeZone.UTC);
        String retval = fmt.print(dateTime);
        return retval;
    }
}