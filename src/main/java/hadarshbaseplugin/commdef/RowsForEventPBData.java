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

package hadarshbaseplugin.commdef;

import org.apache.hadoop.hbase.util.Bytes;
import org.apache.log4j.Logger;
import org.epics.archiverappliance.ByteArray;
import org.epics.archiverappliance.Event;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * A class for storing multiple row PB data of a PV. each row should not bigger than the maxRowSizeInBytes;
 */
public class RowsForEventPBData {
    private static Logger logger = Logger.getLogger(RowsForEventPBData.class.getName());
    private int maxRowSizeInBytes = 32768; // One row can not bigger than this size.
    private List<Event> events;
    private List<rowData> rows = new ArrayList<>(); // The rows for stores PB data.
    private byte[] pVId; // the PV ID of the PV event.
    private long maxPeriodInOneRow;
    private String pvName; 
    /**
     * a class for storing one row data.
     */
    public class rowData {
        public long timestamp;
        public byte[] pb;

        public rowData(long timestamp, byte[] pb) {
            this.timestamp = timestamp;
            this.pb = pb;

        }

        public long getTimestamp() {
            return timestamp;
        }

        public byte[] getPb() {
            return pb;
        }
    }

    public RowsForEventPBData(String pvName, byte[] pVId, List<Event> events, int maxRowSizeInBytes, long maxPeriodInOneRow) {
        this.pvName = pvName;
        this.maxRowSizeInBytes = maxRowSizeInBytes;
        this.events = events;
        this.pVId = pVId;
        this.maxPeriodInOneRow = maxPeriodInOneRow;
    }
    
    public int getEventSize() {
        return events.size();
    }

    public String getPVName() {
        return pvName;
    }
    
    /**
     * The PV ID of the PV event.
     * 
     * @return The PV ID of the PV event.
     */
    public byte[] getpVId() {
        return pVId;
    }

    public List<rowData> getPBRowsOfSamples() throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        DataOutputStream output = new DataOutputStream(buffer);
        
        if(!rows.isEmpty()) {
           return rows;
        }
        
        long timestampSec = events.get(0).getEpochSeconds();
        for (Event event : events) {
            try {
                if (event.getRawForm() != null) {
                    ByteArray bar = event.getRawForm();
                    if (bar.isEmpty()) {
                        continue;
                    }
                    if ((output.size() + bar.toBytes().length < maxRowSizeInBytes)
                            && (event.getEpochSeconds() - timestampSec < maxPeriodInOneRow)) {
                        output.write(event.getRawForm().toBytes());
                        output.write(Bytes.toBytes("\n"));
                    } else {
                        // stores the event data to a new row if row size is too big or the time span is too long.
                        rowData row = new rowData(timestampSec, buffer.toByteArray());
                        rows.add(row);                        
                        output.close();
                        buffer.close();
                        buffer = new ByteArrayOutputStream();
                        output = new DataOutputStream(buffer);
                        
                        timestampSec = event.getEpochSeconds();
                        output.write(event.getRawForm().toBytes());
                        output.write(Bytes.toBytes("\n"));
                    }
                } else {
                    logger.warn("There is no RawForm data in the event! This event will be discard!");
                }
            } catch (Exception e) {
                logger.error("Write event buffer error" + e.getMessage());
            }
        }
        if (output.size() > 0) {
            rowData row = new rowData(timestampSec, buffer.toByteArray());
            rows.add(row);
        }                
        output.close();
        logger.debug("PV " + pvName +" create " + events.size() + " events PB. in "  + rows.size() + " rows.");
        return rows;
    }
}
