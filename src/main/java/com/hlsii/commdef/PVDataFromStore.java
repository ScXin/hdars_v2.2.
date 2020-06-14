package com.hlsii.commdef;


import com.hlsii.vo.RetrieveData;

public class PVDataFromStore {

    private String pvName;
    private PVDataStore dataStore;
    private RetrieveData retrieveData;

    public String getPvName() {
        return pvName;
    }

    public void setPvName(String pvName) {
        this.pvName = pvName;
    }

    public PVDataStore getDataStore() {
        return dataStore;
    }

    public void setDataStore(PVDataStore dataStore) {
        this.dataStore = dataStore;
    }

    public RetrieveData getRetrieveData() {
        return retrieveData;
    }

    public void setRetrieveData(RetrieveData retrieveData) {
        this.retrieveData = retrieveData;
    }


    public PVDataFromStore(String pvName, PVDataStore dataStore, RetrieveData retrieveData) {
        this.pvName = pvName;
        this.dataStore = dataStore;
        this.retrieveData = retrieveData;
    }
}
