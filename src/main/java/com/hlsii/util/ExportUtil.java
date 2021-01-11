package com.hlsii.util;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hlsii.commdef.MultiplePVDataString;
import com.hlsii.commdef.PVDataFormat;
import com.hlsii.commdef.RetrieveParms;
import com.hlsii.vo.RetrieveData;
import org.apache.log4j.Logger;

import java.io.*;
import java.sql.Timestamp;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class ExportUtil {

    /**
     * 写入文件头信息
     *
     * @throws IOException
     */
    private static Logger logger = Logger.getLogger(ExportUtil.class);
    public static boolean exportHeader(File file, String headerStr) throws IOException {
        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(file, true)));
        try {
            out.write(headerStr);
            out.write(0x0A);
            out.flush();
            out.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 写入文件头
     */

    public static boolean exportHeader(File file, String pvName, String startTime, String endTime) throws IOException {
        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(file, true)));
        try {
            out.write("\r\n\r\n\r\n");
            out.write("--------------------------------" + "\r\n");
            out.write(pvName + "\r\n");
            out.write("start time：" + startTime + "    end time：" + endTime + "\r\n");
            out.write("-----time---------------value---" + "\r\n");
            out.write("\r\n\r\n\r\n");
            out.flush();
            out.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }





    /**
     * 向文件中写入历史数据
     * @return
     * @throws IOException
     */
    public static boolean exportPv(File file, List<RetrieveData>retrieveDataList, RetrieveParms retrieveParms, ArrayList<String> pvNameList) throws IOException {
        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(file, true)));
        TreeMap<Long, MultiplePVDataString> pvDataStringTreeMap = new TreeMap<>();
        try {
            for(RetrieveData retrieveData:retrieveDataList) {
                if (retrieveData.getData() != null && !retrieveData.getData().isEmpty()) {
                    //
                    String pvName = retrieveData.getPvName();

                    JSONArray dataArray = retrieveData.getData();
                    for (Object o : dataArray) {
                        JSONObject jsonObject = (JSONObject) o;
                        Long key = MultiplePVDataString.getKey(retrieveParms.getPvDataFormat(), jsonObject);
                        if (key != null) {
                            MultiplePVDataString pvDataString = pvDataStringTreeMap.get(key);
                            if (pvDataString == null) {
                                pvDataString = new MultiplePVDataString(pvNameList, retrieveParms.getPvDataFormat());
                                pvDataStringTreeMap.put(key, pvDataString);
                            }
                            pvDataString.addPVData(pvName, jsonObject);
                        }
                    }
                }
            }
                if (!pvDataStringTreeMap.isEmpty()) {
                    // add PV data string into the queue
                    for (Object aSet : pvDataStringTreeMap.entrySet()) {
                        Map.Entry me = (Map.Entry) aSet;
//                                    outputStream.write(me.getKey().toString().getBytes());
////                                    outputStream.write(",".getBytes());
                        out.write(me.getValue().toString());
                        out.write(0x0A);
                    }
                }
            out.flush();
            out.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    public static boolean exportSinglePVData(File file, RetrieveData retrieveData, RetrieveParms retrieveParms, String pvName) throws FileNotFoundException {
        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(file, true)));
        PVDataFormat pvDataFormat = retrieveParms.getPvDataFormat();

        try {

            if (retrieveData.getData() != null && !retrieveData.getData().isEmpty()) {
                JSONArray dataArray = retrieveData.getData();
                for (Object o : dataArray) {
                    JSONObject jsonObject = (JSONObject) o;
                    long ms = 0;
                    if (pvDataFormat == PVDataFormat.QW) {
                        ms = Long.parseLong(jsonObject.getString("millis"));
                    } else if (pvDataFormat == PVDataFormat.JSON) {
                        long s = Long.parseLong(jsonObject.getString("secs"));
                        long n = Long.parseLong(jsonObject.getString("nanos"));
                        ms = s * 1000 + n / 1000000;
                    } else {
                        logger.error(MessageFormat.format("pvDataFormat {0} is not supported. return empty string", pvDataFormat));
                    }
                    Timestamp ts = new Timestamp(ms);
                    StringBuilder dataStr = new StringBuilder();
                    dataStr.append(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(ts));
                    dataStr.append(",").append(jsonObject.getString("val"));
                    out.write(dataStr.toString());
                    out.write(0x0A);
                }
            }
            out.flush();
            out.close();
            return true;
        } catch (Exception ex) {
            return false;
        }
    }
}
