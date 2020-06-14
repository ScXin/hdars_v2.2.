package com.hlsii.metrics;

/**
 * @author Shangcong Xin
 * @date 4/1/20
 */

import com.alibaba.fastjson.JSONArray;
import com.hlsii.commdef.PVDataFromStore;
import com.hlsii.commdef.PVDataStore;
import com.hlsii.vo.RetrieveData;

import java.util.List;

/**
 * Define HDARS retrieval (download is not included.) metrics.
 */
public interface IRetrievalMetrics {
    /**
     * Add metrics.
     * @param dataStore the data store name.
     * @param ms the time.
     * @param retrieveDataList the data.
     */
    void addRetrievalMetrics(PVDataStore dataStore, long ms, List<RetrieveData> retrieveDataList);

    /**
     * Add metrics.
     * @param dataStore the data store name.
     * @param ms the time.
     * @param retrieveData the data.
     */
    void addRetrievalMetrics(PVDataStore dataStore, long ms, RetrieveData retrieveData);

    /**
     * Add metrics.
     * @param dataStore the data store name.
     * @param ms the time.
     * @param dataJsonArray the data.
     */
    void addRetrievalMetrics(PVDataStore dataStore, long ms, JSONArray dataJsonArray);

    /**
     * Add metrics.
     * @param ms the time.
     * @param pvDataFromStore the data.
     */
    void addRetrievalMetrics(long ms, PVDataFromStore pvDataFromStore);

    /**
     * Reset all counts
     */
    void reset();

    /**
     * Get the total number of retrieval.
     * @return the number of retrieval
     */
    long getTotalRetrieval(PVDataStore dataStore);

    /**
     * Get the total number of retrieval events.
     * @return the number of retrieval events.
     */
    long getTotalRetrievalEvents(PVDataStore dataStore);

    /**
     * Get the total number of retrieval bytes.
     * @return the number of retrieval bytes.
     */
    long getTotalRetrievalBytes(PVDataStore dataStore);

    /**
     * Get the average time of retrieval.
     * @return the average time (millisecond).
     */
    double getAverageRetrievalTime(PVDataStore dataStore);

    /**
     * Get the max time of retrieval.
     * @return the max time (millisecond).
     */
    long getMaxRetrievalTime(PVDataStore dataStore);

    /**
     * Get the min time of retrieval.
     * @return the min time (millisecond).
     */
    long getMinRetrievalTime(PVDataStore dataStore);

    /**
     * Get the average velocity of retrieval.
     * @return the average time (millisecond).
     */
    double getAverageEventRetrievalVelocity(PVDataStore dataStore);

    /**
     * Get the max velocity of retrieval.
     * @return the max time (millisecond).
     */
    double getMaxEventRetrievalVelocity(PVDataStore dataStore);

    /**
     * Get the min velocity of retrieval.
     * @return the min time (millisecond).
     */
    double getMinEventRetrievalVelocity(PVDataStore dataStore);

    /**
     * Get all metrics in JSON format
     * @return
     */
    JSONArray getMetrics();

}