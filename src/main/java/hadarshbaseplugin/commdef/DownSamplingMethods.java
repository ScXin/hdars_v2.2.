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

/**
 * Define the methods for down sampling. 
 * This value will be used in row key of the down sampled PV table.
 */
public enum DownSamplingMethods {
    RAW((byte)0x00,false),      // The raw samples, no down sampling implemented.
    FIRST((byte)0x10,true),    // The first sample in the sampling time span. 
    AVERAGE((byte)0x20,true),  // The average value of all the samples in the sampling time span.
    MAX((byte)0x30,true),      // The max value of all the samples in the sampling time span.
    MIN((byte)0x40,true);      // The min value of all the samples in the sampling time span.    

    private byte valueInRowKey;
    private boolean isDownSamplingType;
    DownSamplingMethods(byte valueInRowKey, boolean isDownSamplingType)
    {
        this.valueInRowKey = valueInRowKey;
        this.isDownSamplingType = isDownSamplingType;
    }
    
    public static DownSamplingMethods getDownSamplingMethod(byte valueInRowkey) {
        for(DownSamplingMethods v:DownSamplingMethods.values()) {
            if(v.getValueInRowKey() == valueInRowkey) {
                return v;
            }
        }
        return null;
    }
    
    /**
     * Get the byte value for create row key. For the raw, just value return 0x00;
     * @return The byte value for create row key 
     */
    public byte getValueInRowKey() {
        return valueInRowKey;
    }
    
    /**
     * Get the value to specify current type is a down sampling type or raw type.
     * @return the value to specify current type is a down sampling type or raw type.
     */
    public boolean isDownSamplingType() {
        return isDownSamplingType;
    }

}
