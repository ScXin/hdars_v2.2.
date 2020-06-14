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

import hadarshbaseplugin.commdef.DownSamplingLevels;
import hadarshbaseplugin.commdef.DownSamplingMethods;

import java.util.EnumMap;
import java.util.Map;

/**
 * A buffer for storing the:
 * 1. Original samples in PB format.
 * 2. PV Id of the PV.
 * 3. Samples in PB format for down sampling level 1-5. 
 */
public class PVSamplesForStorage {
	private byte[] pVId;
	private byte[] originalSamples;
	private Map<DownSamplingLevels, EnumMap<DownSamplingMethods, byte[]>> downSamples;
	public PVSamplesForStorage(byte[] pVId,byte[] originalSamples,Map<DownSamplingLevels, EnumMap<DownSamplingMethods, byte[]>> downSamples) {
		this.pVId = pVId;
		this.originalSamples = originalSamples;
		this.downSamples = downSamples;
	}
	
	public byte[] getpVId() {
		return pVId;
	}
	
	public byte[] getOriginalSamples() {
		return originalSamples;
	}
	
	public Map<DownSamplingLevels, EnumMap<DownSamplingMethods, byte[]>> getDownSamples() {
		return downSamples;
	}	
}
