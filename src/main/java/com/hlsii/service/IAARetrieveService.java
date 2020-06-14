package com.hlsii.service;


import cls.stat_information_plugin.StatInformation;
import com.hlsii.commdef.PVDataFromStore;
import com.hlsii.commdef.PVDataStore;
import com.hlsii.commdef.RetrieveParms;
import com.hlsii.vo.RetrieveData;
import org.epics.archiverappliance.Event;
import org.epics.archiverappliance.config.PVTypeInfo;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface IAARetrieveService {
    /**
     * Initialize a Hazelcast client connected to the ArchivingAppliance.
     * @return
     *          true is everything is ok, false if any error.
     * @throws IOException
     */
    boolean initialize() throws IOException;

    /**
     * Retrieve data from AA over PB/HTTP
     *
     * @param pvName
     *          the PV name.
     * @param parms
     *          the {@link RetrieveParms}
     * @return  the {@link RetrieveData}
     */
    RetrieveData retrieveData(String pvName, RetrieveParms parms);

    /**
     * Retrieve data from AA over PB/HTTP
     *
     * @param pvName
     *          the PV name.
     * @param parms
     *          the {@link RetrieveParms}
     * @return  the {@link RetrieveData}
     */
    PVDataFromStore getData(String pvName, RetrieveParms parms);

    /**
     * Resolve data stores (AA, Hadoop) for retrieval.
     *
     * @param pvName
     *          the PV name.
     * @param start
     *          the start timestamp.
     * @param end
     *          the end timestamp.
     * @param firstKnownEventInHadoop
     *          the first known Event in Hadoop (Long Term Storage).
     * @return
     *          a list of {@link PVDataStore}
     */
    List<PVDataStore> resolveDataStore(String pvName, Timestamp start, Timestamp end, Event firstKnownEventInHadoop);

    /**
     * Get the PV event rate (events/second).
     *
     * @param pvName
     *          the PV name.
     * @return
     */
    float getEventRate(String pvName);

    /**
     * Get the PV sample period (second).
     *
     * @param pvName
     *          the PV name.
     * @return
     */
    float getSamplePeriod(String pvName);

    /**
     * Calculate the size when PV event value is translated to a string.
     *
     * @param pvName
     *          the PV name.
     * @return
     */
    long calculateEventValueSize(String pvName);


    /**
     * Check whether the PV is V4.
     *
     * @param pvName
     *          the PV name.
     * @return true if the PV is V4, false if the PV is V3.
     */
    boolean isV4(String pvName);

    /**
     * Get available AA (ip:port)
     *
     * @return a set of {@link String}, or null if any exception.
     */
    Set<String> getAvailableAA();

    /**
     * Get all archiving PV info
     *
     * @return a collection of all PV {@link PVTypeInfo}
     */
    Collection<PVTypeInfo> getAllPVInfo();

    /**
     * Get the statistical information for a PV
     * @param pvName - the PV name
     * @param startTime - the start time.
     * @param endTime - the end time
     * @return state information
     * @throws IOException  if access HBase failed.
     */
    StatInformation getStat(String pvName, Timestamp startTime, Timestamp endTime);
}