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

import java.io.Closeable;

import edu.stanford.slac.archiverappliance.PB.EPICSEvent;
import org.epics.archiverappliance.Event;

/**
 * Similar to EventStream in Archiver Appliance but much more lightweight... *
 */
public interface EventStream extends Iterable<Event>, Closeable {
	public EPICSEvent.PayloadInfo getPayLoadInfo();
	public void onInfoChange(InfoChangeHandler handler);
}
