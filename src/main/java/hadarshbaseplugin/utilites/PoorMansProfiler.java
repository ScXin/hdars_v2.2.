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

import java.util.LinkedHashMap;

public class PoorMansProfiler {
    private LinkedHashMap<String, Long> steps = new LinkedHashMap<String, Long>();
    private long startingTimeMS = -1;
    private long latestMarkMS = -1;
    
    public PoorMansProfiler() { 
        this.startingTimeMS = System.currentTimeMillis();
    }

    public void mark(String location) { 
        latestMarkMS = System.currentTimeMillis();
        steps.put(location, latestMarkMS);
    }
    
    public String toString() { 
        StringBuilder buf = new StringBuilder();
        buf.append("\n");
        long previousTimeMS = startingTimeMS;
        for(String location : steps.keySet()) {
            Long currentTimeMS = steps.get(location);
            buf.append(location);
            buf.append(" --> ");
            buf.append(currentTimeMS - previousTimeMS);
            buf.append("(ms)\n");
            previousTimeMS = currentTimeMS;
        }
        buf.append("Total");
        buf.append(" --> ");
        buf.append(latestMarkMS - startingTimeMS);
        buf.append("(ms)\n");
        
        return buf.toString();
    }
    
    public long totalTimeMS() { 
        return latestMarkMS - startingTimeMS;
    }
}
