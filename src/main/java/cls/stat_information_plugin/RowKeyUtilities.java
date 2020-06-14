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

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

public class RowKeyUtilities {
   
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
