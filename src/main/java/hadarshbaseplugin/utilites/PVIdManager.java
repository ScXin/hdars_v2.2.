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
package hadarshbaseplugin.utilites;

import hadarshbaseplugin.commdef.CommonConstants;
import hadarshbaseplugin.commdef.HadarsHbaseConfiguration;
import hadarshbaseplugin.commdef.PvInfo;
import edu.stanford.slac.archiverappliance.PB.EPICSEvent.PayloadType;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.log4j.Logger;
import org.epics.archiverappliance.config.ArchDBRTypes;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.io.IOException;
import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Manager the PV ID. Provide methods to query, delete or Generate a PV ID for a PV.
 */
public class PVIdManager {
    private Connection conn = null;
    private Map<String, PvInfo> pvInfors = new HashMap<>();
    private List<Long> pvIdList = new ArrayList<>();
    private static SecureRandom secureRandom = new SecureRandom();
    private HadarsHbaseConfiguration config;
    private static TableName pVIdTable;
    private static TableName uniquePVIdTable;
    private static Logger logger = Logger.getLogger(PVIdManager.class.getName());
    private DateTime seed;

    /**
     * The Constructor. To initialize the random seed.
     */
    public PVIdManager() {
        seed = new DateTime(DateTimeZone.UTC);

    }

    /**
     * To cache the PV ID table. To schedule a task to update the local cache periodically.
     * 
     * @param hBaseConn
     *            a connection to the HBase.
     * @return if not exception return true;
     * @throws IOException
     * 
     */
    public void initialize(Connection hBaseConn,HadarsHbaseConfiguration config) throws IOException {
        this.config = config;
        pVIdTable = TableName.valueOf(config.pvIDTableName());
        uniquePVIdTable = TableName.valueOf(config.pvUUIDTableName());
        conn = hBaseConn;
        // When multiple AA server start at a same time, to prevent they using a same time stamp as the random seed, add
        // the unique local host name to the seed.
        secureRandom.setSeed(Bytes.toBytes("" + seed.getMillis() + (InetAddress.getLocalHost()).getHostName()));
        byte[] columnFamily = new byte[] { 'p' };
        List<byte[]> cfList = new ArrayList<>();
        cfList.add(columnFamily);
        HBaseTableManager tableMgr = new HBaseTableManager(conn);
        tableMgr.createTable(pVIdTable, cfList, null);
        tableMgr.createTable(uniquePVIdTable, cfList, null);
        loadPVInfoFromHBase();       
    }

    private synchronized boolean loadPVInfoFromHBase() throws IOException {
        pvInfors.clear();
        // Read the PV ID table:
        Table table = conn.getTable(pVIdTable);
        Scan scan = new Scan();
        ResultScanner rsacn = table.getScanner(scan);
        for (Result rs : rsacn) {
            String pvN = Bytes.toString(rs.getRow());
            PvInfo info = new PvInfo();
            info.setPvName(pvN);
            Cell[] cells = rs.rawCells();
            for (Cell cell : cells) {
                readPVInfoFromCell(info, cell);
            }
            pvInfors.put(pvN, info);
        }
        // To create a list to cache all the PV IDs.
        getUIDList();

        return true;
    }

    private void getUIDList() throws IOException {        
        Table table = conn.getTable(uniquePVIdTable);
        Scan scan = new Scan();
        ResultScanner rsacn = table.getScanner(scan);
        for (Result rs : rsacn) {
            byte[] pvID =  rs.getRow();
            pvIdList.add(transform4BytesToLong(pvID));
        }
    }

    private void readPVInfoFromCell(PvInfo info, Cell cell) {
        byte qualifier = cell.getQualifierArray()[cell.getQualifierOffset()];
        switch (qualifier) {
        case 'i': {
            byte[] id = new byte[CommonConstants.PVID_SIZE];
            System.arraycopy(cell.getValueArray(), cell.getValueOffset(), id, 0, CommonConstants.PVID_SIZE);
            info.setPvId(id);
            break;
        }
        case 't': {
            byte pvType = cell.getValueArray()[cell.getValueOffset()];
            info.setPvType(ArchDBRTypes.valueOf(PayloadType.valueOf(pvType)));
            break;
        }
        case 'd': {
            byte[] delTime = new byte[CommonConstants.PVDEL_TIME_SIZE];
            System.arraycopy(cell.getValueArray(), cell.getValueOffset(), delTime, 0, CommonConstants.PVDEL_TIME_SIZE);
            ByteBuffer bb = ByteBuffer.wrap(delTime); // The deleted time is in unit of second.
            int deletetime = bb.asIntBuffer().get();
            info.setDeletedTime(deletetime);
            break;
        }
        case 'm': {
            byte[] metaInfo = new byte[cell.getValueLength()];
            System.arraycopy(cell.getValueArray(), cell.getValueOffset(), metaInfo, 0, cell.getValueLength());
            info.setMetaOfPV(Bytes.toString(metaInfo));
            break;
        }
        default:
            logger.debug("There is a unknow column in the PVId table.");
            break;
        }
    }

    private long transform4BytesToLong(byte[] bytes) {
        long value = 0;
        for (int i = 0; i < bytes.length; i++) {
            value <<= 8;
            value |= bytes[i] & 0xFF;
        }
        return value;
    }

    /**
     * Create a PVInfo object for given PV. The PVID will be append to the PVId Table on the HBase.
     * 
     * @param pvName
     *            - the PV name.
     * @param pvType
     *            - the PV type
     * @return PVInfo object for given PV.
     * @throws IOException
     *             if access the HBase failed.
     */
    public synchronized PvInfo createPVInfo(String pvName, ArchDBRTypes pvType, String metaOfPV) throws IOException {
        if (conn == null) {
            logger.error("The HBase connection is null, can not create PV ID");
        }
        PvInfo info;
        info = new PvInfo();
        info.setPvName(pvName);
        byte[] pvId = generatePVId();
        if(pvId.length <= 0) {
            return null;
        }
        
        info.setPvId(pvId);
        byte payloadType = (byte) pvType.getIntegerMap();
        info.setPvType(pvType);
        info.setMetaOfPV(metaOfPV);

        // append to HBase PVId table:
        Table table = conn.getTable(pVIdTable);
        List<Mutation> mutations = new ArrayList<>();
        byte[] rowKey = Bytes.toBytes(pvName);
        Put put = new Put(rowKey);
        put.addColumn(Bytes.toBytes("p"), Bytes.toBytes("i"), pvId);

        byte[] pt = { payloadType };
        put.addColumn(Bytes.toBytes("p"), Bytes.toBytes("t"), pt);
        
        if(metaOfPV!=null) {
            put.addColumn(Bytes.toBytes("p"), Bytes.toBytes("m"), Bytes.toBytes(metaOfPV));
        }
        
        mutations.add(put);        

        boolean mutateSuccess = table.checkAndMutate(rowKey, Bytes.toBytes("p")).qualifier(Bytes.toBytes("i"))
                .ifNotExists().thenMutate(RowMutations.of(mutations));

        // If the PV is add by another host at a same time. We should get the information to update local cache.
        if (!mutateSuccess) {
            Get get = new Get(rowKey);
            Result set = table.get(get);
            for (Cell cell : set.rawCells()) {
                readPVInfoFromCell(info, cell);
            }
        }
        table.close();

        // update local caches:
        pvInfors.put(pvName, info);
        long value = transform4BytesToLong(info.getPvId());
        pvIdList.add(value);

        logger.info("add PVID for:" + pvName + " to HBase Success!");
        return info;
    }

    /**
     * Generate a PVID (4bytes). PVID is: Salting Number + PV Identification code
     * 
     * @return the PV ID.
     * @throws IOException
     */
    private byte[] generatePVId() throws IOException {
        
        int maxReTryCounter = 100;
        Table table = conn.getTable(uniquePVIdTable);
        boolean putSuccess = false;
        byte[] bytes = null;
        int putCounter = 0;
        do {
            bytes = new byte[CommonConstants.PVID_SIZE];
            long value = 0;
            do {
                secureRandom.nextBytes(bytes); // create PV ID 
                value = transform4BytesToLong(bytes);
            } while (pvIdList.contains(value)); // assuring that the UID hasn't been assigned yet.

            Put put = new Put(bytes);
            // For the row, there is at least one column in it:
            put.addColumn(Bytes.toBytes("p"), Bytes.toBytes("r"), new byte[]{1});
            putSuccess = table.checkAndMutate(bytes, Bytes.toBytes("p")).qualifier(Bytes.toBytes("r")).ifNotExists()
                    .thenPut(put); // If another AA server already use this PV ID this put will failed.
            putCounter++;
        } while (!putSuccess && putCounter < maxReTryCounter);
        table.close();
        
        if(!putSuccess) {
            logger.error("Can not create a unique PV ID.");
            return new byte[0];
        }
        return bytes;
    }

    /**
     * Rename a PV for the Archiver system. note: Rename a PV on the Archiver system will not deleted the old PV
     * information. So we can not delete the old PV name here.
     * 
     * @param
     *            - the PVInfo of the PV.
     * @param newName
     *            - the new name of this PV
     * @throws IOException
     *             if access HBase failed.
     */
    public synchronized void reNamePv(PvInfo oldPVInfo, String newName) throws IOException {
        if (conn == null) {
            logger.error("The HBase connection is null, can not recover the delted PV.");
            return;
        }

        PvInfo info;
        info = new PvInfo();
        info.setPvName(newName);
        info.setPvId(oldPVInfo.getPvId());
        info.setPvType(oldPVInfo.getPvType());

        // append to HBase PVId table:
        Table table = conn.getTable(pVIdTable);
        List<Mutation> mutations = new ArrayList<>();
        byte[] rowKey = Bytes.toBytes(newName);
        Put put = new Put(rowKey);
        put.addColumn(Bytes.toBytes("p"), Bytes.toBytes("i"), info.getPvId());

        byte[] pt = { (byte) oldPVInfo.getPvType().getIntegerMap() };
        put.addColumn(Bytes.toBytes("p"), Bytes.toBytes("t"), pt);
        
        String metaOfPV = oldPVInfo.getMetaOfPV();
        if(metaOfPV != null) {
            put.addColumn(Bytes.toBytes("p"), Bytes.toBytes("m"),  Bytes.toBytes(metaOfPV));
        }        
        
        if(oldPVInfo.getDeletedTime() != 0) {
            put.addColumn(Bytes.toBytes("p"), Bytes.toBytes("d"),  Bytes.toBytes(oldPVInfo.getDeletedTime()));
        }
        
        mutations.add(put);
        if (table.checkAndMutate(rowKey, Bytes.toBytes("p")).ifNotExists().thenMutate(RowMutations.of(mutations))) {
            logger.info("rename the " + oldPVInfo.getPvName() + " to :" + newName
                    + " failed. The name already exist on the PVId table!");
            table.close();
            return;
        }
        
        table.close();
        // update local caches:
        pvInfors.put(newName, info);
        logger.info("rename the " + oldPVInfo.getPvName() + " to :" + newName + " Success!");
    }

    /**
     * Query the PV ID by the PV name. If there no this PV in the local cache, this function will query the PVId Table
     * on the HBase.
     * 
     * @param pvName
     *            - the PV name
     * @return the PVInfo object if exist, otherwise return null.
     * @throws IOException
     *             if access HBase failed.
     */
    public synchronized PvInfo getPVInfo(String pvName) throws IOException {
        if (pvInfors.containsKey(pvName)) {
            return pvInfors.get(pvName);
        } else {
            loadPVInfoFromHBase();
            if (pvInfors.containsKey(pvName)) {
                return pvInfors.get(pvName);
            }
        }
        return null;
    }
    
    public synchronized Map<String, PvInfo>  getPVInfoList() throws IOException{
        loadPVInfoFromHBase();
        return pvInfors;
    }

    /**
     * Recover a deleted PV.
     * 
     * @param info
     *            - the PVInfo of the PV.
     * @throws IOException
     *             if access HBase failed.
     */
    public synchronized void reCoverDeltedPv(PvInfo info) throws IOException {
        if (conn == null) {
            logger.error("The HBase connection is null, can not recover the delted PV.");
            return;
        }
        // append to HBase PVId table:
        Table table = conn.getTable(pVIdTable);
        List<Mutation> mutations = new ArrayList<>();
        byte[] rowKey = Bytes.toBytes(info.getPvName());
        Put put = new Put(rowKey);
        put.addColumn(Bytes.toBytes("p"), Bytes.toBytes("i"), info.getPvId());

        byte[] pt = { (byte) info.getPvType().getIntegerMap() };
        put.addColumn(Bytes.toBytes("p"), Bytes.toBytes("t"), pt);

        int dt = 0;
        put.addColumn(Bytes.toBytes("p"), Bytes.toBytes("d"), Bytes.toBytes(dt));
        mutations.add(put);
        table.checkAndMutate(rowKey, Bytes.toBytes("p")).qualifier(Bytes.toBytes("i")).ifEquals(info.getPvId())
                .thenMutate(RowMutations.of(mutations));
        table.close();
        info.setDeletedTime(0);
        // update local caches:
        pvInfors.put(info.getPvName(), info);
        // PV ID is no changed, so the pvIdList doesn't need modification.
    }

    /**
     * delete a PV
     * 
     * @param info
     *            - the PVInfo of the PV
     * @throws IOException
     *             if access HBase failed.
     */
    public synchronized void deltedPv(PvInfo info) throws IOException {
        if (conn == null) {
            logger.error("The HBase connection is null, can not delete the delted PV.");
            return;
        }
        // append to HBase PVId table:
        Table table = conn.getTable(pVIdTable);
        List<Mutation> mutations = new ArrayList<>();
        byte[] rowKey = Bytes.toBytes(info.getPvName());
        Put put = new Put(rowKey);
        put.addColumn(Bytes.toBytes("p"), Bytes.toBytes("i"), info.getPvId());

        byte[] pt = { (byte) info.getPvType().getIntegerMap() };
        put.addColumn(Bytes.toBytes("p"), Bytes.toBytes("t"), pt);

        DateTime tm = new DateTime(DateTimeZone.UTC);
        int delTm = (int) (tm.getMillis() / 1000);
        byte[] dt = Bytes.toBytes(delTm);
        put.addColumn(Bytes.toBytes("p"), Bytes.toBytes("d"), dt);
        mutations.add(put);

        table.checkAndMutate(rowKey, Bytes.toBytes("p")).qualifier(Bytes.toBytes("i")).ifEquals(info.getPvId())
                .thenMutate(RowMutations.of(mutations));
        table.close();

        info.setDeletedTime(delTm);
        // update local caches:
        pvInfors.put(info.getPvName(), info);
        // We remain the PV ID in the pvIdList, prevent re-use this ID.
    }
    
    
    /**
     * update the value in the PV ID table.
     * 
     * @param info
     *            - the new PVInfo of the PV.
     *            
     * @throws IOException
     *             if access HBase failed.
     */
    public synchronized void updatePV(PvInfo info) throws IOException {
        if (conn == null) {
            logger.error("The HBase connection is null, can not recover the delted PV.");
            return;
        }
        
        // append to HBase PVId table:
        Table table = conn.getTable(pVIdTable);
        List<Mutation> mutations = new ArrayList<>();
        byte[] rowKey = Bytes.toBytes(info.getPvName());
        Put put = new Put(rowKey);
        put.addColumn(Bytes.toBytes("p"), Bytes.toBytes("i"), info.getPvId());

        byte[] pt = { (byte) info.getPvType().getIntegerMap() };
        put.addColumn(Bytes.toBytes("p"), Bytes.toBytes("t"), pt);
        
        String metaOfPV = info.getMetaOfPV();
        if(metaOfPV != null) {
            put.addColumn(Bytes.toBytes("p"), Bytes.toBytes("m"),  Bytes.toBytes(metaOfPV));
        }        
        
        if(info.getDeletedTime() != 0) {
            put.addColumn(Bytes.toBytes("p"), Bytes.toBytes("d"),  Bytes.toBytes(info.getDeletedTime()));
        }
        mutations.add(put);
        
        if (table.checkAndMutate(rowKey, Bytes.toBytes("p")).qualifier(Bytes.toBytes("i")).ifEquals(info.getPvId())
                .thenMutate(RowMutations.of(mutations))) {
            logger.info("update the " + info.getPvName() +" failed. The name already exist on the PVId table!");
            table.close();
            return;
        }
        
        table.close();
        // update local caches:
        pvInfors.put(info.getPvName(), info);
        logger.info("The " + info.getPvName() + " update Success!");
    }


}
