/*
 * HADARS - Cosylab Hadoop-based Accelerator Data Archiver and Retrieval System
 * Copyright (c) 2018 Cosylab d.d.
 *
 * mailto:info AT cosylab DOT com
 * Gerbiceva 64, 1000 Ljubljana, Slovenia
 *
 * This software is distributed under the terms found
 * in file LICENSE-CSL-2.0.txt that is included with this distribution.
 */


package cls.stat_information_plugin;

public class CommonConstants {   
    public static final int PVID_SIZE = 4;
    public static final int PV_TIME_STAMP_LEN = 4;
    public static final int PVDEL_TIME_SIZE = 4; // The number of bytes for record delete time in the PV ID table.
    public static final int PV_TABLE_ROWKEY_LEN = 8;
    public static final int DOWN_SAMPLE_TABLE_ROW_KEY_LEN = 9;
    public static final int DOWN_SAMPLE_JOB_TABLE_ROW_KEY_LEN = 5;
    public static final int TYPE_LEVEL_SIZE = 1;    
    public static final int MAX_ROW_NUMBER_PER_SCAN = 10000;
    public static final int MAX_ROW_NUMBER_PER_WRITE = 10000;
}
