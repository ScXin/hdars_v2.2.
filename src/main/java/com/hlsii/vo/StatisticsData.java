package com.hlsii.vo;

/**
 * Statistics Data for a PV
 */
public class StatisticsData extends BaseVO {
    private static final long serialVersionUID = 1L;
    private boolean hasData;
    private double mean;
    private double deviation;
    private double rms;
    private double max;
    private double min;





    public StatisticsData() {
        super();
        hasData = false;
    }

    public StatisticsData(boolean hasData) {
        this();
        this.hasData = hasData;
    }

    public StatisticsData(double mean, double deviation, double rms, double max, double min) {
        this(true);
        this.mean = mean;
        this.deviation = deviation;
        this.rms = rms;
        this.max = max;
        this.min = min;
    }

    public boolean getHasData() {
        return hasData;
    }

    public double getMean() {
        return mean;
    }

    public double getDeviation() {
        return deviation;
    }

    public double getRms() {
        return rms;
    }

    public double getMax() {
        return max;
    }

    public double getMin() {
        return min;
    }

    public void setHasData(boolean hasData) {
        this.hasData = hasData;
    }

    public void setMean(double mean) {
        this.mean = mean;
    }

    public void setDeviation(double deviation) {
        this.deviation = deviation;
    }

    public void setRms(double rms) {
        this.rms = rms;
    }

    public void setMax(double max) {
        this.max = max;
    }

    public void setMin(double min) {
        this.min = min;
    }
}