package com.hlsii.util;


import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import java.sql.Timestamp;

/**
 * Time utility
 *
 */
public class TimeUtil {
    private TimeUtil() {}

    /**
     * Convert ISO8601 time to time stamp
     *
     * @param tsstr - ISO8601 time
     * @return - time stamp
     */
    public static Timestamp convertFromISO8601String(String tsstr) {
        // Sample ISO8601 string 2011-02-01T08:00:00.000Z
        DateTimeFormatter fmt = ISODateTimeFormat.dateTime();
        DateTime dt = fmt.parseDateTime(tsstr);
        return new Timestamp(dt.getMillis());
    }

    public static String convertToISO8601String(Timestamp ts) {
        DateTimeFormatter fmt = ISODateTimeFormat.dateTime();
        DateTime dateTime = new DateTime(ts.getTime(), DateTimeZone.UTC);
        String retval = fmt.print(dateTime);
        return retval;
    }
}