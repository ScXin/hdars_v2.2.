package com.hlsii.metrics;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hlsii.commdef.PVDataFromStore;
import com.hlsii.commdef.PVDataStore;
import com.hlsii.vo.RetrieveData;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author ScXin
 * @date 4/28/2020 12:08 AM
 */

@Service
public class RetrievalMetricsImplementation implements IRetrievalMetrics {
    private static Logger logger = Logger.getLogger(RetrievalMetricsImplementation.class.getName());

    private HashMap<PVDataStore, DataStoreRetrievalMetrics> dataStoreMetricsMap = new HashMap<>();

    private ScheduledThreadPoolExecutor executor = null;
    private final Object lock = new Object();

    public RetrievalMetricsImplementation() {
        executor = (ScheduledThreadPoolExecutor) Executors.newScheduledThreadPool(2);
        dataStoreMetricsMap.put(PVDataStore.HADARS, new DataStoreRetrievalMetrics());
        dataStoreMetricsMap.put(PVDataStore.AA, new DataStoreRetrievalMetrics());
        dataStoreMetricsMap.put(PVDataStore.HADOOP, new DataStoreRetrievalMetrics());
    }

    @Override
    public void addRetrievalMetrics(PVDataStore dataStore, long ms, List<RetrieveData> retrieveDataList) {
        if (dataStore == null) {
            logger.error("dataStore is null. Ignore the metrics.");
            return;
        }
        if (retrieveDataList == null) {
            logger.error("retrieveDataList is null. Ignore the metrics.");
            return;
        }

        DataStoreRetrievalMetrics metrics = dataStoreMetricsMap.get(dataStore);
        if (metrics == null) {
            logger.error(MessageFormat.format("The data store {0} is unknown. Ignore the metrics.",
                    dataStore.toString()));
            return;
        }

        long numberOfEvents = 0;
        for (RetrieveData retrieveData : retrieveDataList) {
            if (retrieveData != null) {
                JSONArray jsonArray = retrieveData.getData();
                if (jsonArray != null) {
                    numberOfEvents += jsonArray.size();
                }
            }
        }

        final long number = numberOfEvents;
        executor.schedule(() -> {
            synchronized (lock) {
                metrics.addRetrievalMetrics(ms, number);
            }
        }, 0, TimeUnit.SECONDS);
    }

    @Override
    public void addRetrievalMetrics(PVDataStore dataStore, long ms, RetrieveData retrieveData) {
        if (dataStore == null) {
            logger.error("dataStore is null. Ignore the metrics.");
            return;
        }
        if (retrieveData == null) {
            logger.error("retrieveData is null. Ignore the metrics.");
            return;
        }

        DataStoreRetrievalMetrics metrics = dataStoreMetricsMap.get(dataStore);
        if (metrics == null) {
            logger.error(MessageFormat.format("The data store {0} is unknown. Ignore the metrics.",
                    dataStore.toString()));
            return;
        }

        JSONArray jsonArray = retrieveData.getData();
        long numberOfEvents = 0;
        if (jsonArray != null) {
            numberOfEvents = jsonArray.size();
        }

        final long number = numberOfEvents;
        executor.schedule(() -> {
            synchronized (lock) {
                metrics.addRetrievalMetrics(ms, number);
            }
        }, 0, TimeUnit.SECONDS);

    }

    @Override
    public void addRetrievalMetrics(PVDataStore dataStore, long ms, JSONArray dataJsonArray) {
        if (dataStore == null) {
            logger.error("dataStore is null. Ignore the metrics.");
            return;
        }
        if (dataJsonArray == null) {
            logger.error("retrieveData is null. Ignore the metrics.");
            return;
        }

        DataStoreRetrievalMetrics metrics = dataStoreMetricsMap.get(dataStore);
        if (metrics == null) {
            logger.error(MessageFormat.format("The data store {0} is unknown. Ignore the metrics.",
                    dataStore.toString()));
            return;
        }

        final long number = dataJsonArray.size();
        executor.schedule(() -> {
            synchronized (lock) {
                metrics.addRetrievalMetrics(ms, number);
            }
        }, 0, TimeUnit.SECONDS);
    }

    @Override
    public void addRetrievalMetrics(long ms, PVDataFromStore pvDataFromStore) {
        if (pvDataFromStore == null) {
           // logger.error("pvDataFromStore is null. Ignore the metrics.");
            return;
        }

        if (pvDataFromStore.getDataStore() == null) {
            //logger.error("pvDataFromStore.getDataStore() is null. Ignore the metrics.");
            return;
        }

        RetrieveData retrieveData = pvDataFromStore.getRetrieveData();
        if (retrieveData == null) {
            //logger.error("pvDataFromStore.getDataStore() is null. Ignore the metrics.");
            return;
        }

        DataStoreRetrievalMetrics metrics = dataStoreMetricsMap.get(pvDataFromStore.getDataStore());
        if (metrics == null) {
            //logger.error(MessageFormat.format("The data store {0} is unknown. Ignore the metrics.",
              //      pvDataFromStore.getDataStore().toString()));
            return;
        }

        JSONArray jsonArray = retrieveData.getData();
        long numberOfEvents = 0;
        if (jsonArray != null) {
            numberOfEvents = jsonArray.size();
        }

        final long number = numberOfEvents;

        executor.schedule(() -> {
            synchronized (lock) {
                metrics.addRetrievalMetrics(ms, number);
            }
        }, 0, TimeUnit.SECONDS);
    }

    @Override
    public void reset() {
        dataStoreMetricsMap.get(PVDataStore.AA).reset();
        dataStoreMetricsMap.get(PVDataStore.HADOOP).reset();
        dataStoreMetricsMap.get(PVDataStore.HADARS).reset();
    }

    public long getTotalRetrieval(PVDataStore dataStore) {
        return dataStoreMetricsMap.get(dataStore).getTotalRetrieval();
    }

    @Override
    public long getTotalRetrievalEvents(PVDataStore dataStore) {
        return dataStoreMetricsMap.get(dataStore).getTotalRetrievalEvents();
    }

    @Override
    public long getTotalRetrievalBytes(PVDataStore dataStore) {
        // TODO: retrieval bytes
        return 0;
    }

    @Override
    public double getAverageRetrievalTime(PVDataStore dataStore) {
        return dataStoreMetricsMap.get(dataStore).getAverageRetrievalTime();
    }

    @Override
    public long getMaxRetrievalTime(PVDataStore dataStore) {
        return dataStoreMetricsMap.get(dataStore).getMaxRetrievalTime();
    }

    @Override
    public long getMinRetrievalTime(PVDataStore dataStore) {
        return dataStoreMetricsMap.get(dataStore).getMinRetrievalTime();
    }

    @Override
    public double getAverageEventRetrievalVelocity(PVDataStore dataStore) {
        return dataStoreMetricsMap.get(dataStore).getAverageEventRetrievalVelocity();
    }

    @Override
    public double getMaxEventRetrievalVelocity(PVDataStore dataStore) {
        return dataStoreMetricsMap.get(dataStore).getMaxEventRetrievalVelocity();
    }

    @Override
    public double getMinEventRetrievalVelocity(PVDataStore dataStore) {
        return dataStoreMetricsMap.get(dataStore).getMinEventRetrievalVelocity();
    }

    @Override
    public String toString() {
        StringBuilder buf = new StringBuilder();
        for (PVDataStore pvDataStore : dataStoreMetricsMap.keySet()) {
            buf.append("\n\n").append(pvDataStore.toString()).append(":\n");
            buf.append("\n").append("getTotalRetrieval: ").append(getTotalRetrieval(pvDataStore));
            buf.append("\n").append("getTotalRetrievalEvents: ").append(getTotalRetrievalEvents(pvDataStore));
            buf.append("\n").append("getTotalRetrievalBytes: ").append(getTotalRetrievalBytes(pvDataStore));
            buf.append("\n").append("getAverageRetrievalTime(millisecond): ").append(getAverageRetrievalTime(pvDataStore));
            buf.append("\n").append("getMaxRetrievalTime(millisecond): ").append(getMaxRetrievalTime(pvDataStore));
            buf.append("\n").append("getMinRetrievalTime(millisecond): ").append(getMinRetrievalTime(pvDataStore));
            buf.append("\n").append("getAverageEventRetrievalVelocity(events/second): ").append(getAverageEventRetrievalVelocity(pvDataStore));
            buf.append("\n").append("getMaxEventRetrievalVelocity(events/second): ").append(getMaxEventRetrievalVelocity(pvDataStore));
            buf.append("\n").append("getMinEventRetrievalVelocity(events/second): ").append(getMinEventRetrievalVelocity(pvDataStore));
        }
        return buf.toString();
    }

    @Override
    public JSONArray getMetrics() {
        PVDataStore[] dataStores = new PVDataStore[] {PVDataStore.HADOOP, PVDataStore.AA, PVDataStore.HADARS};
        JSONArray metrics = new JSONArray();
        for (PVDataStore pvDataStore : dataStores) {
            JSONObject metric = new JSONObject();
            metric.put("dataStore", pvDataStore == PVDataStore.HADARS? "Total" : pvDataStore.toString());
            List<JSONObject> metricObjs = new ArrayList<>();
            JSONObject metricObj = new JSONObject();
            metricObj.put("name", "Total Retrieval");
            metricObj.put("value", getTotalRetrieval(pvDataStore));
            metricObjs.add(metricObj);
            metricObj = new JSONObject();
            metricObj.put("name", "Total Retrieval Events");
            metricObj.put("value", getTotalRetrievalEvents(pvDataStore));
            metricObjs.add(metricObj);
            // TODO
        	/*metricObj = new JSONObject();
        	metricObj.put("name", "Total Retrieval Bytes");
        	metricObj.put("value", getTotalRetrievalBytes(pvDataStore));
        	metricObjs.add(metricObj);*/
            metricObj = new JSONObject();
            metricObj.put("name", "Average Retrieval Time(ms)");
            metricObj.put("value", getAverageRetrievalTime(pvDataStore));
            metricObjs.add(metricObj);
            metricObj = new JSONObject();
            metricObj.put("name", "Max Retrieval Time(ms)");
            metricObj.put("value", getMaxRetrievalTime(pvDataStore));
            metricObjs.add(metricObj);
            metricObj = new JSONObject();
            metricObj.put("name", "Min Retrieval Time(ms)");
            metricObj.put("value", getMinRetrievalTime(pvDataStore));
            metricObjs.add(metricObj);
            metricObj = new JSONObject();
            metricObj.put("name", "Average Event Retrieval Velocity(events/s)");
            metricObj.put("value", getAverageEventRetrievalVelocity(pvDataStore));
            metricObjs.add(metricObj);
            metricObj = new JSONObject();
            metricObj.put("name", "Max Event Retrieval Velocity(events/s)");
            metricObj.put("value", getMaxEventRetrievalVelocity(pvDataStore));
            metricObjs.add(metricObj);
            metricObj = new JSONObject();
            metricObj.put("name", "Min Event Retrieval Velocity(events/s)");
            metricObj.put("value", getMinEventRetrievalVelocity(pvDataStore));
            metricObjs.add(metricObj);
            metric.put("metrics", metricObjs);
            metrics.add(metric);
        }
        return metrics;
    }

}

