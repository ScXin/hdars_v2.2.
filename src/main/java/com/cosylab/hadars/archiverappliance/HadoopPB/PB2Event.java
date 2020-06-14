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

package com.cosylab.hadars.archiverappliance.HadoopPB;

import edu.stanford.slac.archiverappliance.PB.data.DBR2PBTypeMapping;
import edu.stanford.slac.archiverappliance.PB.utils.LineEscaper;
import org.apache.log4j.Logger;
import org.epics.archiverappliance.ByteArray;
import org.epics.archiverappliance.Event;
import org.epics.archiverappliance.config.ArchDBRTypes;
import org.epics.archiverappliance.data.DBRTimeEvent;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

/**
 * Util class for converting between PB and Event.
 */
public class PB2Event {
    private static Logger logger = Logger.getLogger(PB2Event.class.getName());

    /**
     * Convert a PB to a Event.
     *
     * @param type
     *          The type of the event
     * @param year
     *          The year of the event because the PB doesn't have it.
     * @param pb
     *          The Event serialization byte.
     * @return
     *          The Event or null if any error.
     */
    public static Event PB2EventConvert(ArchDBRTypes type, short year, byte[] pb) {
        if (pb == null) {
            logger.warn("The input pb cannot be null!");
            return null;
        }

        try {
            Constructor<? extends DBRTimeEvent> unmarshallingConstructor = DBR2PBTypeMapping.getPBClassFor(type).
                    getUnmarshallingFromByteArrayConstructor();

            return unmarshallingConstructor.newInstance(year, new ByteArray(pb));
        } catch(Exception e) {
            logger.error("Exception deserializing the PB.", e);
        }

        return null;
    }

    /**
     * Convert a PB list to a Event list.
     *
     * @param type
     *          The type of the event
     * @param year
     *          The year of the event because the PB doesn't have it.
     * @param pb
     *          The Event serialization byte which contains many events which is divided by "0x0A".
     * @return
     *          The list of Event or null if any error.
     */
    public static List<Event> PBList2EventListConvert(ArchDBRTypes type, short year, byte[] pb) {
        if (pb == null) {
            logger.warn("The input pb cannot be null!");
            return null;
        }

        ArrayList<Event> events = new ArrayList<>();

        try {
            List<byte[]> arrays = new ArrayList<>();
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            int currentReadPosition = 0;
            int start = currentReadPosition;

            while (currentReadPosition < pb.length) {
                if (pb[currentReadPosition] == LineEscaper.NEWLINE_CHAR) {
                    out.write(pb, start, currentReadPosition - start);
                    arrays.add(out.toByteArray());
                    out.reset();
                    start = currentReadPosition + 1;
                }
                currentReadPosition++;
            }

            if (currentReadPosition > start) {
                out.write(pb, start, currentReadPosition - start);
                arrays.add(out.toByteArray());
            }

            Constructor<? extends DBRTimeEvent> unmarshallingConstructor = DBR2PBTypeMapping.getPBClassFor(type).
                    getUnmarshallingFromByteArrayConstructor();

            for (byte[] bytes : arrays) {
                DBRTimeEvent event = unmarshallingConstructor.newInstance(year, new ByteArray(bytes));
                events.add(event);
            }

        } catch(Exception e) {
            logger.error("Exception deserializing the PB list.", e);
            return null;
        }

        return events;
    }
}
