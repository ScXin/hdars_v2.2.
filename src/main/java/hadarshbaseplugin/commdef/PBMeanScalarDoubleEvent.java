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

import hadarshbaseplugin.pb.EPICSMeanEvent;
import edu.stanford.slac.archiverappliance.PB.data.PBParseException;
import edu.stanford.slac.archiverappliance.PB.utils.LineEscaper;
import org.apache.log4j.Logger;
import org.epics.archiverappliance.ByteArray;
import org.epics.archiverappliance.Event;
import org.epics.archiverappliance.common.POJOEvent;
import org.epics.archiverappliance.common.TimeUtils;
import org.epics.archiverappliance.common.YearSecondTimestamp;
import org.epics.archiverappliance.config.ArchDBRTypes;
import org.epics.archiverappliance.data.SampleValue;
import org.epics.archiverappliance.data.ScalarValue;

import java.io.ByteArrayOutputStream;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * This event only use to stores the average data for down sampling in double type. A mean event include a mean value of
 * the PV and the count of events.
 */
public class PBMeanScalarDoubleEvent implements Event {
    private static Logger logger = Logger.getLogger(PBMeanScalarDoubleEvent.class.getName());
    ByteArray bar = null;
    short year = 0;
    EPICSMeanEvent.MeanScalarDouble dbevent = null;

    public PBMeanScalarDoubleEvent(Timestamp timeStamp, double meanValue, long eventCount) {
        YearSecondTimestamp yst = TimeUtils.convertToYearSecondTimestamp(timeStamp);
        year = yst.getYear();
        EPICSMeanEvent.MeanScalarDouble.Builder builder = EPICSMeanEvent.MeanScalarDouble.newBuilder()
                .setSecondsintoyear(yst.getSecondsintoyear()).setNano(yst.getNanos()).setVal(meanValue)
                .setCount(eventCount);

        dbevent = builder.build();
        bar = new ByteArray(LineEscaper.escapeNewLines(dbevent.toByteArray()));
    }

    public PBMeanScalarDoubleEvent(short year, ByteArray bar) {
        this.year = year;
        this.bar = bar;
        unmarshallEventIfNull();
    }

    public PBMeanScalarDoubleEvent(PBMeanScalarDoubleEvent ev) {
        YearSecondTimestamp yst = TimeUtils.convertToYearSecondTimestamp(ev.getEventTimeStamp());
        year = yst.getYear();
        EPICSMeanEvent.MeanScalarDouble.Builder builder = EPICSMeanEvent.MeanScalarDouble.newBuilder()
                .setSecondsintoyear(yst.getSecondsintoyear()).setNano(yst.getNanos()).setVal(ev.getValue())
                .setCount(ev.getCount());
        dbevent = builder.build();
        bar = new ByteArray(LineEscaper.escapeNewLines(dbevent.toByteArray()));
    }

    public short getYear() {
        return year;
    }

    public int getSecondsIntoYear() {
        unmarshallEventIfNull();
        return dbevent.getSecondsintoyear();
    }

    @Override
    public long getEpochSeconds() {
        unmarshallEventIfNull();
        return TimeUtils.getStartOfYearInSeconds(year) + dbevent.getSecondsintoyear();
    }

    @Override
    public ByteArray getRawForm() {
        return bar;
    }

    @Override
    public SampleValue getSampleValue() {
        unmarshallEventIfNull();
        return new ScalarValue<Double>(dbevent.getVal());
    }

    public Double getValue() {
        unmarshallEventIfNull();
        return dbevent.getVal();
    }

    public long getCount() {
        unmarshallEventIfNull();
        return dbevent.getCount();
    }

    @Override
    public Event makeClone() {
        return new PBMeanScalarDoubleEvent(this);
    }

    @Override
    public Timestamp getEventTimeStamp() {
        unmarshallEventIfNull();
        return TimeUtils.convertFromYearSecondTimestamp(
                new YearSecondTimestamp(year, dbevent.getSecondsintoyear(), dbevent.getNano()));
    }

    @Override
    public ArchDBRTypes getDBRType() {
        return ArchDBRTypes.DBR_SCALAR_DOUBLE;
    }

    private void unmarshallEventIfNull() {
        try {
            if (dbevent == null) {
                dbevent = EPICSMeanEvent.MeanScalarDouble.newBuilder()
                        .mergeFrom(bar.inPlaceUnescape().unescapedData, bar.off, bar.unescapedLen).build();
            }
        } catch (Exception ex) {
            throw new PBParseException(bar.toBytes(), ex);
        }
    }

    public Event toScalarEvent(ArchDBRTypes eventType) {
        unmarshallEventIfNull();
        SampleValue convertValue = null;
        switch (eventType) {
        case DBR_SCALAR_BYTE:
            convertValue = new ScalarValue<Byte>((byte) ((Double) dbevent.getVal()).intValue());
            break;
        case DBR_SCALAR_DOUBLE:
            convertValue = new ScalarValue<Double>(dbevent.getVal());
            break;
        case DBR_SCALAR_FLOAT:
            convertValue = new ScalarValue<Float>((float) dbevent.getVal());
            break;
        case DBR_SCALAR_INT:
            convertValue = new ScalarValue<Integer>(((Double) dbevent.getVal()).intValue());
            break;
        case DBR_SCALAR_SHORT:
            convertValue = new ScalarValue<Short>((short) ((Double) dbevent.getVal()).intValue());
            break;
        default:
            break;
        }
        if (convertValue != null) {
            return new POJOEvent(eventType, getEventTimeStamp(), convertValue, 0, 0);
        }
        logger.error("Cann't convert EPICSMeanEvent to " + eventType.getClass().getName() + "!");
        return null;
    }

    /**
     * convert PB buffer into a PBMeanScalarDoubleEvent list;
     * 
     * @param year
     *            - the year of these PB buffer.
     * @param pb
     *            - the PB buffer.
     * @return the PBMeanScalarDoubleEvent list
     */
    public static List<PBMeanScalarDoubleEvent> pBArray2MinIntEventListConvert(short year, byte[] pb) {
        List<PBMeanScalarDoubleEvent> events = new ArrayList<>();
        if (pb == null) {
            logger.warn("The input PBMeanScalarDoubleEvent PB cannot be null!");
            return events;
        }

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

            for (byte[] bytes : arrays) {             
                ByteArray bar = new ByteArray(bytes);                
                PBMeanScalarDoubleEvent event = new PBMeanScalarDoubleEvent(year, bar);
                events.add(event);
            }
        } catch (Exception e) {
            logger.error("Exception deserializing the PBMeanScalarDoubleEvent PB list.", e);
            return new ArrayList<>();
        }
        return events;
    }
}
