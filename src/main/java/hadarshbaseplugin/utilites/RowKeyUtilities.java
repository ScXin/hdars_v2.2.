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

package hadarshbaseplugin.utilites;

import hadarshbaseplugin.commdef.CommonConstants;
import hadarshbaseplugin.commdef.DownSamplingLevels;
import hadarshbaseplugin.commdef.DownSamplingMethods;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

public class RowKeyUtilities {
    /**
     * Create a row key for the Down sampling job table. The row key composed by: PVID + type+level
     * @param pvId  - PV ID
     * @param dwonSamplingMethod - down sampling type.
     * @param downSampleLevel - down sampling levle.
     * @return row key.
     */
    public static byte[] createDownSamplingJobTableRowKey(byte[] pvId, DownSamplingMethods dwonSamplingMethod,
            DownSamplingLevels downSampleLevel) {
        byte[] rowkey = new byte[CommonConstants.DOWN_SAMPLE_JOB_TABLE_ROW_KEY_LEN];
        System.arraycopy(pvId, 0, rowkey, 0, CommonConstants.DOWN_SAMPLE_JOB_TABLE_ROW_KEY_LEN - 1);
        byte typeLevel = (byte) (dwonSamplingMethod.getValueInRowKey() | downSampleLevel.getValueInRowKey());
        rowkey[CommonConstants.DOWN_SAMPLE_JOB_TABLE_ROW_KEY_LEN - 1] = typeLevel;
        return rowkey;
    }
    
    /**
     * get DownSamplingMethod from down sampling row key.
     * @param rowKey - row key of down sampling table.
     * @return the DownSamplingMethod
     */
    public static DownSamplingMethods getDownSamplingMethodFromDownSamplingRowKey(byte[]  rowKey) {
        if(rowKey == null || rowKey.length< CommonConstants.DOWN_SAMPLE_TABLE_ROW_KEY_LEN) {
            return null;
        }        
        byte typeLevel = rowKey[CommonConstants.PVID_SIZE];
        return DownSamplingMethods.getDownSamplingMethod((byte)(typeLevel&0xf0));
    }
    
    
    /**
     * Get the down sampling level from the rowkey
     * 
     * @param rowKey
     *            - the row key
     * @return down sampling level
     */
    public static DownSamplingLevels getDownSamplingLevleFromDownSamplingRowKey(byte[] rowKey) {
        if(rowKey == null || rowKey.length< CommonConstants.DOWN_SAMPLE_TABLE_ROW_KEY_LEN) {
            return null;
        }       
        return DownSamplingLevels.getLevel((byte) (rowKey[CommonConstants.PVID_SIZE] & 0x0f));
    }

    /**
     * get the time stamp in downSampledPV table row key.
     * @param rowKey - row key
     * @return time stamp in unit of second.
     */
    public static Long getTimeStampFromDownSamplingRowKey(byte[] rowKey) {
        if (rowKey != null && rowKey.length == CommonConstants.DOWN_SAMPLE_TABLE_ROW_KEY_LEN) {
            ByteBuffer btb = ByteBuffer.wrap(rowKey, CommonConstants.PVID_SIZE + CommonConstants.TYPE_LEVEL_SIZE, CommonConstants.PV_TIME_STAMP_LEN);            
           return ((long) btb.asIntBuffer().get());
        }
        return null;
    }
    
    /**
     * get the PV ID from the downSampledPV table row key.
     * @param rowKey - row key
     * @return  PV ID
     */
    public static byte[] getPVIdFromDownSamplingRowKey(byte[] rowKey) {
        if(rowKey == null || rowKey.length != CommonConstants.DOWN_SAMPLE_TABLE_ROW_KEY_LEN) {
            return null;
        }       
        byte[] pvId = new byte[CommonConstants.PVID_SIZE];
        System.arraycopy(rowKey, 0, pvId, 0,   CommonConstants.PVID_SIZE );
        return pvId;
    }

    /**
     * Generate a row key for DownSampledPV table. RowKey is composed by: PV ID(5 bytes) + Sampling Type(high 4 bits) + Down
     * Sampling Level (low 4 bits)+ PV Sampled Timestamp(4 bytes)
     * 
     * @param timeStamp
     *            - the time stamp of this row.
     * @param pVId
     *            - PV ID of this PV
     * @param level
     *            - Down sampling level.
     * @param downSamplingMethod
     *            - down sampling method.
     * @throws IOException if access byte array buffer error.
     */
    public static byte[] createDownSampledPVTableRowKey(int timeStamp, byte[] pVId, DownSamplingLevels level,
            DownSamplingMethods downSamplingMethod) throws IOException {
        byte[] typeLevel = { (byte) (downSamplingMethod.getValueInRowKey() | level.getValueInRowKey()) };
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        DataOutputStream output = new DataOutputStream(buffer);

        output.write(pVId);
        output.write(typeLevel);
        output.writeInt(timeStamp);
        return buffer.toByteArray();
    }
    

    /**
     * Generate a row key for PV table. RowKey is composed by: PV ID(5 bytes) + PV Sampled Timestamp(4 bytes)
     * 
     * @param timeStamp
     *            - the time stamp of this row.
     * @param pVId
     *            - PV ID of this PV
     * @throws IOException if access byte array buffer error.
     */
    public static byte[] createPVTableRowKey(int timeStamp, byte[] pVId) throws IOException {        
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        DataOutputStream output = new DataOutputStream(buffer);
        output.write(pVId);        
        output.writeInt(timeStamp);
        return buffer.toByteArray();
    }
}
