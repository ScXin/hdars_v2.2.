/*
 * HADARS - Cosylab Hadoop-based Accelerator Data Archiver and Retrieval System
 * Copyright (c) 2019 Cosylab d.d.
 *
 * mailto:info AT cosylab DOT com
 * Gerbiceva 64, 1000 Ljubljana, Slovenia
 *
 * This software is distributed under the terms found
 * in file LICENSE-CSL-2.0.txt that is included with this distribution.
 */

package hadarshbaseplugin.commdef;

import java.util.ArrayList;
import java.util.List;

/**
 * To define the down sampling levels. 
 * The raw data is not down sampled data, they do not be included in these levels.
 */
public class DownSamplingLevels {
    private static List<DownSamplingLevels> levels = new ArrayList<>();
    private static HadarsHbaseConfiguration config;
    
    private byte valueInRowKey; // This value is used with the DownSamplingMethods to compose a byte for the row key.
    private int samplingSeconds; // Sampling period for create one down sampling sample. 
    private int maxPeriodInOneRow; // The maximum time span in one row in this level.

   

    private int maxReturnEventsForQuery; // the maximum number of events will return for a query.
    
    /**
     * constructor.
     * 
     * @param samplingSeconds
     *            - the Sampling period for create one down sampling sample
     * @param valueInRowKey
     *            - This value is used with the DownSamplingMethods to compose a byte for the row key.
     * @param maxReturnEventsForQuery
     *            - the maximum number of events will return for a query.
     */
    private DownSamplingLevels(int samplingSeconds, byte valueInRowKey, int maxReturnEventsForQuery) {
        this.samplingSeconds = samplingSeconds;
        this.valueInRowKey = valueInRowKey;
        this.maxReturnEventsForQuery = maxReturnEventsForQuery;
        this.maxPeriodInOneRow = samplingSeconds * maxReturnEventsForQuery;       
    }

    /**
     * Initialize all the levels by configuration file.
     * 
     * @param levelPeriods
     *            - a list of time periods for levels.
     * @param maxReturnEventsForQuery
     *            - the maximum number of events will return for a query.
     */
    public static void InitializeLevels(HadarsHbaseConfiguration cfg) {       
        config = cfg;
        int i = 0;
        levels.clear();
        for (Integer v :  config.downSampleLevels()) {
            levels.add(new DownSamplingLevels(v, (byte) i++, config.maxReturnEventsForQuery()));
        }
    }

    /**
     * get the number of levels.
     * @return  the number of levels.
     */
    public static int getLevelCount() {
        return levels.size();
    }
    
    /**
     * get all the levels.
     * @return all the levels
     */
    public static List<DownSamplingLevels> getLevels() {
        return levels;
    }
    
    /**
     * get level by a given value.
     * 
     * @param valueInRowKey
     *            - the value in row key. It is a value between 0-8.
     * @return the Level
     */
    public static DownSamplingLevels getLevel(byte valueInRowKey) {
        for (DownSamplingLevels v : DownSamplingLevels.levels) {
            if (v.getValueInRowKey() == valueInRowKey) {
                return v;
            }
        }
        return null;
    }
   
    /**
     * The maximum time span in one row in this level.
     * 
     * @return The maximum time span in one row in this level.
     */
    public int getmaxPeriodInOneRow() {
        return maxPeriodInOneRow;
    }

    /**
     * Sampling period for create one down sampling sample 
     * 
     * @return Sampling period for create one down sampling sample
     */
    public int getSamplingSeconds() {
        return samplingSeconds;
    }

    /**
     * This level value is used with the DownSamplingMethods to compose a byte for the row key.
     * 
     * @return level value in row key
     */
    public byte getValueInRowKey() {
        return valueInRowKey;
    }

    /**
     * the maximum number of events will return for a query.
     * 
     * @return the maximum number of events will return for a query.
     */
    public int getMaxReturnEventsForQuery() {
        return maxReturnEventsForQuery;
    }

}
