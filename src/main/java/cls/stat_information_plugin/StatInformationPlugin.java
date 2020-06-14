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

import cls.hadarsstatcoprocessor.pb.Stat;
import cls.hadarsstatcoprocessor.pb.Stat.StatResponse;
import com.google.protobuf.ByteString;
import com.google.protobuf.ServiceException;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.client.coprocessor.Batch;
import org.apache.hadoop.hbase.ipc.BlockingRpcCallback;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Map;

/**
 * for get the statistical information by calling HBase Coprocessor.
 */
public class StatInformationPlugin {
    private final Connection conn;
    private static Logger logger = Logger.getLogger(StatInformationPlugin.class.getName());
    private final TableName tableName;
    private final int rawDataRowMaxPeriod;
    
    public StatInformationPlugin(Connection conn, String pvTableName, int rawDataRowMaxPeriod) {
        this.conn = conn;
        this.tableName = TableName.valueOf(pvTableName);
        this.rawDataRowMaxPeriod = rawDataRowMaxPeriod;
    }

    /**
     * Get the statistical information for a PV
     * 
     * @param pvName
     *            - the PV ID
     * @param startTime
     *            - the start time.
     * @param endTime
     *            - the end time
     * @return state information
     * @throws IOException
     *             if access HBase failed.
     */
    public StatInformation getStat(byte[] pvID, Timestamp startTimeStamp, Timestamp endTimeStamp, int pvType) throws IOException {
        Table table = conn.getTable(tableName);
        long count = 0L;
        double sum = 0.0;
        double squareSum = 0.0;
        double mean = 0.0;
        double deviation = 0.0;
        double rms = 0.0;
        double max = Double.MIN_VALUE;
        double min = Double.MAX_VALUE;
        int startTime = (int) (startTimeStamp.getTime() / 1000);
        int stopTime = (int) (endTimeStamp.getTime() / 1000);
        int startTimeAdjusted = startTime - rawDataRowMaxPeriod;

        byte[] startKey = RowKeyUtilities.createPVTableRowKey(startTimeAdjusted, pvID);
        byte[] stopKey = RowKeyUtilities.createPVTableRowKey(stopTime, pvID);

        final Stat.StatRequest request = Stat.StatRequest.newBuilder().setStartTime(startTime).setStopTime(stopTime)
                .setPvID(ByteString.copyFrom(pvID)).setPvType(pvType).setScanAdditionTime(rawDataRowMaxPeriod).build();

        try {
            Map<byte[], Stat.StatResponse> results = table.coprocessorService(Stat.StatService.class, startKey, stopKey,
                    new Batch.Call<Stat.StatService, Stat.StatResponse>() {
                        @Override
                        public Stat.StatResponse call(Stat.StatService aggregate) throws IOException {
                            com.google.protobuf.RpcCallback<Stat.StatResponse> rpcCallback = new ResponseCallBackOfHbase();
                            aggregate.getStat(null, request, rpcCallback);
                            Stat.StatResponse response = ((ResponseCallBackOfHbase) rpcCallback).get();
                            return response;
                        }
                    });

            for (Stat.StatResponse response : results.values()) {
                if (!response.getSuccess()) {
                    System.out.println("Error at:" + response.getError());
                    continue;
                }
                count += response.getCount();
                sum += response.getSum();
                squareSum += response.getSquareSum();
                if (max < response.getMax())
                    max = response.getMax();
                if (min > response.getMin())
                    min = response.getMin();
            }
            if (count > 0) {
                mean = sum / count;
                rms = Math.sqrt(squareSum / count);
                deviation = Math.sqrt(squareSum / count - Math.pow(mean, 2));
            }

            return ImmutableStatInformation.builder().count(count).sum(sum).squareSum(squareSum).mean(mean)
                    .deviation(deviation).rms(rms).max(max).min(min).build();

        } catch (ServiceException e) {

            logger.error("calcurate stat ServiceException:" + e + e.getMessage());
        } catch (Throwable e) {
            logger.error("calcurate stat Throwable:" + e + e.getMessage());
        }
        return null;
    }

    class ResponseCallBackOfHbase implements com.google.protobuf.RpcCallback<Stat.StatResponse> {
        BlockingRpcCallback<Stat.StatResponse> cb = new BlockingRpcCallback<Stat.StatResponse>();

        @Override
        public void run(StatResponse parameter) {
            cb.run(parameter);
        }

        public synchronized Stat.StatResponse get() throws IOException {
            return cb.get();
        }
    }
}
