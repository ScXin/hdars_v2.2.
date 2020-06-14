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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.guava.GuavaModule;
import org.immutables.value.Value;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 *  The configuration for the client to access the HBase.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Value.Immutable
@JsonDeserialize(as = ImmutableHadarsHbaseConfiguration.class)
public interface HadarsHbaseConfiguration {
    
    /**
     * the host name of HBase zookeeper quorum. Default is "hadars-cdh-master.host.com". 
     * @return the host name of HBase zookeeper quorum.
     */
    String zookeeperQuorum();
    
    /**
     * the number of zookeeper client Port; Default is 2181.
     * @return the number of zookeeper client Port;
     */
    int zookeeperClientPort();
    
    /**
     * the PV table name
     * @return the PV table name
     */
    String pvTableName(); //default is "PV";  The name "PV_4_dev" is for development and testing.
    
    /**
     * The name of down sampled PV table. Default is "DownsampledPV". 
     * @return
     */
    String downsampledPVTableName();
    
    /**
     * the PV ID table name;
     * @return the PV ID table name;
     */
    String pvIDTableName(); //default is "PVId"; The name "PVId_4_dev" is for development and testing.
    /**
     * the UUID table name 
     * @return the UUID table name
     */
    String pvUUIDTableName(); //default is "PVUnId"; The name "PVUnId_4_dev" is for development and testing.
    
    /**
     * The name of down sample job table. Default is "DSJob"
     * @return
     */
    String jobTableName();
    
    /**
     * The row number will be cached. In the HBase this value is 100 by default. 4000 is suggested.
     * @return  The row number will be cached.
     */
    int cacheRowsWhenScan();  
    
    /**
     * The maximum seconds for the raw data row. 
     * @return The maximum seconds for the raw data row.
     */
    int maxSecondsForRawSampleRow();
    
    /**
     * A list to define the time period for different levels of down sampling. 
     * @return A list to define the time period for different levels of down sampling.
     */    
    List<Integer> downSampleLevels();
    
    /**
     * To indicate how many down sample levels will be created, when putting raw data to HBase.  
     * @return  The value to indicate how many down sample levels will be created, when putting raw data to HBase.
     */
     int rawDataCreateDownSampleLevelNumber();
    /**
     * The maximum size of one row in the HBase. The unit is byte.
     * @return the maximum size of one row in the HBase.
     */
    int maxRowSize();
    
    /**
     * max number for rows one put action on HBase. 
     * @return max number for rows one put action on HBase.
     */
    int maxNumberOfPutRows();
    /**
     * The maximum return events for one retrieval request.
     * @return The maximum return events for one retrieval query.
     */
    int maxReturnEventsForQuery();
    
    /**
     * The number of bits use to split region for PV table.
     * @return The number of bits use to split region.
     */
    int regionSplitBits4PVTable();
    
    /**
     * The number of bits use to split region for Down Sampled PV table.
     * @return The number of bits use to split region.
     */
    int regionSplitBits4DownSampledTable();
    
    public static HadarsHbaseConfiguration fromStream(InputStream istream) throws IOException {

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.registerModule(new GuavaModule());
        System.out.println(istream);
//        System.out.println("678");
        HadarsHbaseConfiguration config = mapper.readValue(istream, ImmutableHadarsHbaseConfiguration.class);
//        System.out.println("789");
        if (config == null) {
            throw new IOException("Invalid configuration file.");
        }

        return config;
    }
}
