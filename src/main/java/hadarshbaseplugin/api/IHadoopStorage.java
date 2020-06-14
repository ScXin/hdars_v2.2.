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

package hadarshbaseplugin.api;

import cls.stat_information_plugin.StatInformation;

import  hadarshbaseplugin.commdef.PostProcessing;
import org.epics.archiverappliance.Event;
import org.epics.archiverappliance.config.ArchDBRTypes;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.util.List;

public interface IHadoopStorage {
    /**
     * Get the name of the storage implementation.
     * 
     * @return the name.
     */
    String getName();

    /**
     * Initialize the storage. e.g. for HBase, set up connection.
     * 
     * @param configFileStream  - a input stream of the configuration file. 
     * @return true is everything is ok, false if any error.
     * @throws IOException if access hbase failed
     */
    boolean initialize(InputStream configFileStream) throws IOException;
    
    /**
     * Initialize the storage. e.g. for HBase, set up connection.
     * 
     * @param configFile  - The path of configuration file. 
     * @return true is everything is ok, false if any error.
     * @throws IOException  if access hbase failed
     */
    boolean initialize(String configFile) throws IOException;

    /**
     * Store data into the HADARS data warehouse.
     * 
     * @param pvName
     *            the PV name.
     * @param eventList
     *            the list of events.
     * @param eventType
     *            the type of the event
     * @param metaData
     *            the meta of this event.
     * @return true if everything is ok, false if any error.
     * @throws IOException if access hbase failed
     */
    boolean appendData(String pvName, List<Event> eventList, ArchDBRTypes eventType, String metaData)
            throws IOException;

    /**
     * Store data into the HADARS data warehouse.
     * 
     * @param pvName
     *            the PV name.
     * @param eventsList
     *            the list of Event list.
     * @param eventType
     *            the type of the event
     * @param metaData
     *            the meta of this event.
     * @return true if everything is ok, false if any error.
     * @throws IOException
     */
    boolean appendDataList(String pvName, List<List<Event>> eventsList, ArchDBRTypes eventType, String metaData)
            throws IOException;

    /**
     * Get the PV meta information
     * 
     * @param pvName
     *            - the PV name;
     * @return the meta information in JSON string format.
     * @throws IOException
     *             - if access HBase error.
     */
    String getMeta(String pvName) throws IOException;

    /**
     * Get data from the HADARS data warehouse.
     * 
     * @param pvName
     *            the PV name.
     * @param startTime
     *            the start time of the retrieval data.
     * @param endTime
     *            the end time of the retrieval data.
     * @param downSamplingIdentify
     *            the downsampling method.
     * @param intervalSeconds
     *            the interval (seconds) of downsampling.
     * @param enableCache
     *            If true the HBase will create a block cache when scanning the data.
     * @param samplePeriod - Sample period of raw data.  
     * @return the list of the data.
     * @throws IOException
     */
    List<Event> getData(String pvName, Timestamp startTime, Timestamp endTime, PostProcessing downSamplingIdentify,
                        int intervalSeconds, boolean enableCache, double samplePeriod) throws IOException;

    /**
     * Get the down sampling interval (seconds) for the time span between the startTime and endTime of the given down
     * sampling type.
     * 
     * @param downSamplingType
     *            - down sampling type.
     * @param startTime
     *            - the start time.
     * @param endTime
     *            - then end time.
     * @return the value of down sampling interval (seconds). The return value is between 0 to 65535. For the down
     *         sampling type NONE the return value is 0.
     */
    Integer getSamplingInterval(PostProcessing downSamplingType, Timestamp startTime, Timestamp endTime, double samplePeriod);

    /**
     * Rename a PV name, the old name still exists.
     * 
     * @param oldName
     *            the old PV name.
     * @param newName
     *            the new PV name.
     */
    void renamePV(String oldName, String newName) throws IOException;

    /**
     * Delete the PV name from Hadoop. The PV name does not exist anymore. If the data without any PV name connected,
     * the data is deleted.
     * 
     * @param pvName
     *            the PV name.
     * @throws IOException
     */
    void deleteData(String pvName) throws IOException;

    /**
     * Get the first known event in the HADARS data warehouse.
     * 
     * @param pvName
     *            the PV name.
     * @return the first Event.
     * @throws IOException
     */
    Event getFirstKnownEvent(String pvName) throws IOException;

    /**
     * Get the last known event in the HADARS data warehouse.
     * 
     * @param pvName
     *            the PV name.
     * @return the last event.
     * @throws IOException
     */
    Event getLastKnownEvent(String pvName) throws IOException;

    /**
     * Get the total space of the HADARS data warehouse.
     * 
     * @return The total space.
     * @throws IOException
     */
    long getTotalSpace() throws IOException;

    /**
     * Get the usable space of the HADARS data warehouse.
     * 
     * @return the usable space.
     * @throws IOException
     */
    long getUsableSpace() throws IOException;

    /**
     * Get the consumed space by the PV in the HADARS data warehouse.
     * 
     * @param pvName
     *            The PV name.
     * @return the consumed space.
     * @throws IOException
     */
    long spaceConsumedByPV(String pvName) throws IOException;    
    
    /**
     * Get the statistical information for a PV
     * @param pvName - the PV name
     * @param startTime - the start time. 
     * @param endTime - the end time
     * @return state information 
     * @throws IOException  if access HBase failed.
     */
    StatInformation getStat(String pvName, Timestamp startTime, Timestamp endTime) throws IOException;
   
}
