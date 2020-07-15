package com.hlsii.util;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hlsii.commdef.MultiplePVDataString;
import com.hlsii.commdef.RetrieveParms;
import com.hlsii.vo.RetrieveData;

import java.io.*;
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
}
