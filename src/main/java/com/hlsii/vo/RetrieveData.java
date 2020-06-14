package com.hlsii.vo;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.Serializable;

/**
 * Retrieve data for a PV
 */
public class RetrieveData implements Serializable {
    private static final long serialVersionUID = 1L;



    private String pvName;

    // meta data has the name of the PV, and waveform indicates if the PV is waveform.
    private JSONObject meta;

    // data has field millis (timestamp) and val. When the PV has waveform, val is an array
    // of double value, otherwise it is a double value.
    private JSONArray data;



    public RetrieveData(String pvName) {
        this.pvName = pvName;
    }

    public RetrieveData(String pvName, JSONObject meta, JSONArray data) {
        super();
        this.pvName = pvName;
        this.meta = meta;
        this.data = data;
    }

    public JSONObject getMeta() {
        return meta;
    }

    public void setMeta(JSONObject meta) {
        this.meta = meta;
    }

    public JSONArray getData() {
        return data;
    }

    public void setData(JSONArray data) {
        this.data = data;
    }

    public void addData(JSONArray newData) {
        if (newData != null) {
            if (data == null) {
                // null from AA.
                data = new JSONArray();
            }
            // merge data which are gotten from Hadoop.
            data.addAll(newData);
        }
    }

    public String getPvName() {
        return pvName;
    }
}