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

import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;

import java.io.IOException;
import java.util.List;

/**
 * utility for managing the HBase table;
 */
public class HBaseTableManager {

    Connection conn;

    /**
     * constructor.
     * 
     * @param hBaseConn
     *            a connection to the HBase.
     */
    public HBaseTableManager(Connection hBaseConn) {
        conn = hBaseConn;
    }

    /**
     * create tables with column families.
     *
     * @tableName table name.
     *
     * @family column families.
     */
    public void createTable(TableName tableName, List<byte[]> columnFamilys, byte[][] regionSplitKey)
            throws IOException {
        Admin admin = conn.getAdmin();
        if (!admin.tableExists(tableName)) {
            TableDescriptorBuilder tdb = TableDescriptorBuilder.newBuilder(tableName);
            ColumnFamilyDescriptorBuilder cdb;
            ColumnFamilyDescriptor cfd;
            for (byte[] columnFamily : columnFamilys) {
                cdb = ColumnFamilyDescriptorBuilder.newBuilder(columnFamily);
                cfd = cdb.build();
                tdb.setColumnFamily(cfd);
            }
            TableDescriptor td = tdb.build();
            if (regionSplitKey == null || regionSplitKey.length <= 0) {
                admin.createTable(td);
            } else {
                admin.createTable(td, regionSplitKey);
            }
            admin.enableTableAsync(tableName);
        } else {
            System.out.println("table '" + tableName +"' already exist");
        }
    }
}
