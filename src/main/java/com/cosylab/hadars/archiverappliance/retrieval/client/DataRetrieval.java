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

package com.cosylab.hadars.archiverappliance.retrieval.client;

import java.sql.Timestamp;
import java.util.HashMap;

/**
 * Main interface for client retrieving data using the PB/HTTP protocol.
 * @author mshankar
 */
public interface DataRetrieval {
	/**
	 * Get data for PV pvName from starttime to endtime. By default, we expect to get raw data.
	 * @param pvName
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public EventStream getDataForPV(String pvName, Timestamp startTime, Timestamp endTime);
	/**
	 * Get data for PV pvName from starttime to endtime using the system defined sparsification operator.
	 * @param pvName
	 * @param startTime
	 * @param endTime
	 * @param useReducedDataSet - If true, use the server defined sparsification operator...
	 * @return
	 */
	public EventStream getDataForPV(String pvName, Timestamp startTime, Timestamp endTime, boolean useReducedDataSet);

	/**
	 * Get data for PV pvName from starttime to endtime using the system defined sparsification operator; pass additional params in the HTTP call.
	 * @param pvName
	 * @param startTime
	 * @param endTime
	 * @param useReducedDataSet - If true, use the server defined sparsification operator...
	 * @param otherParams - Any other name/value pairs that are passed onto the server. 
	 * @return
	 */
	public EventStream getDataForPV(String pvName, Timestamp startTime, Timestamp endTime, boolean useReducedDataSet, HashMap<String, String> otherParams);
}
