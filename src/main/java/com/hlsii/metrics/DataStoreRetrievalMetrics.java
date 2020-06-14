package com.hlsii.metrics;

/**
 * @author ScXin
 * @date 4/28/2020 12:10 AM
 */

/**
 * Metrics for one data store.
 */
public class DataStoreRetrievalMetrics {
    // the number of retrieval
    private long totalRetrieval = 0;

    // the number of retrieval events
    private long totalRetrievalEvents = 0;

    // the total time of retrieval (milliseconds)
    private long totalRetrievalTime = 0;

    // the max retrieval time (milliseconds)
    private long maxRetrievalTime = 0;

    // the min retrieval time (milliseconds)
    private long minRetrievalTime = 0;

    // the max retrieval velocity: events / seconds
    private double maxEventRetrievalVelocity = 0;

    // the min retrieval velocity: events / seconds
    private double minEventRetrievalVelocity = 0;

    private double sumOfVelocity = 0;

    public void addRetrievalMetrics(long ms, long number) {
        totalRetrieval++;
        totalRetrievalEvents += number;
        totalRetrievalTime += ms;
        if ((ms > maxRetrievalTime) || (maxRetrievalTime == 0)) {
            maxRetrievalTime = ms;
        }
        if ((ms < minRetrievalTime) || (minRetrievalTime == 0)) {
            minRetrievalTime = ms;
        }

        double velocity = (double) number / ((double) ms/1000);
        sumOfVelocity += velocity;
        if ((velocity > maxEventRetrievalVelocity) || (maxEventRetrievalVelocity == 0)) {
            maxEventRetrievalVelocity = velocity;
        }
        if ((velocity < minEventRetrievalVelocity) || (minEventRetrievalVelocity == 0)) {
            minEventRetrievalVelocity = velocity;
        }
    }

    public void reset() {
        totalRetrieval = 0;
        totalRetrievalEvents = 0;
        totalRetrievalTime = 0;
        maxRetrievalTime = 0;
        minRetrievalTime = 0;
        maxEventRetrievalVelocity = 0;
        minEventRetrievalVelocity = 0;
        sumOfVelocity = 0;
    }

    public long getTotalRetrieval() {
        return totalRetrieval;
    }

    public long getTotalRetrievalEvents() {
        return totalRetrievalEvents;
    }

    public double getAverageRetrievalEvents() {
        return (double) totalRetrievalEvents / totalRetrieval;
    }

    public double getAverageRetrievalTime() {
        return (double) totalRetrievalTime / totalRetrieval;
    }

    public long getMaxRetrievalTime() {
        return maxRetrievalTime;
    }

    public long getMinRetrievalTime() {
        return minRetrievalTime;
    }

    public double getAverageEventRetrievalVelocity() {
        return sumOfVelocity / totalRetrieval;
    }

    public double getMaxEventRetrievalVelocity() {
        return maxEventRetrievalVelocity;
    }

    public double getMinEventRetrievalVelocity() {
        return minEventRetrievalVelocity;
    }
}
