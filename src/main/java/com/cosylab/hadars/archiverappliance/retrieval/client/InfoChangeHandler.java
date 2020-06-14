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

import edu.stanford.slac.archiverappliance.PB.EPICSEvent;

/**
 * Handle info change events.
 * Events are returned older events first; so updates to metadata can come in later chunks.
 * @author mshankar
 *
 */
public interface InfoChangeHandler {
    public void handleInfoChange(EPICSEvent.PayloadInfo info);
}
