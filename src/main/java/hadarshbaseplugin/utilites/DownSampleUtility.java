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

import hadarshbaseplugin.commdef.CommonConstants;
import hadarshbaseplugin.commdef.DownSamplingLevels;
import hadarshbaseplugin.commdef.DownSamplingMethods;
import hadarshbaseplugin.commdef.PBMeanScalarDoubleEvent;
import org.apache.log4j.Logger;
import org.epics.archiverappliance.Event;
import org.epics.archiverappliance.common.POJOEvent;
import org.epics.archiverappliance.config.ArchDBRTypes;
import org.epics.archiverappliance.data.DBRTimeEvent;
import org.epics.archiverappliance.data.ScalarValue;

import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Utility for create down sampling data.
 */
public class DownSampleUtility {

    private static Logger logger = Logger.getLogger(DownSampleUtility.class.getName());

    /**
     * split the event list into a map. the Map's key is the time stamp. The time stamp is round base on the level
     * sampling time span.
     * 
     * @param
     *            - the event list.
     * @param targetLevel
     *            - down sampling out put level.
     * @return the events split in a map.
     */
    public static Map<Integer, List<Event>> splitListBaseOnDownSamplingrLevel(List<Event> stream,
                                                                              DownSamplingLevels targetLevel) {
        int firstTimeStamp = (int) stream.get(0).getEpochSeconds();
        Map<Integer, List<Event>> groupMap = stream.stream().collect(Collectors.groupingBy(e -> {
            int t = ((int) (e.getEpochSeconds()) - firstTimeStamp) / targetLevel.getSamplingSeconds();
            return t * targetLevel.getSamplingSeconds() + firstTimeStamp;
        }));

        for (Map.Entry<Integer, List<Event>> entry : groupMap.entrySet()) {
            entry.getValue().sort(Comparator.comparingLong(Event::getEpochSeconds));
        }
        return groupMap;
    }

    /**
     * For a utility classes, a non-public constructor should be defined.
     */
    private DownSampleUtility() {}

    public static void calculateDownSamplingValues(int firstTimeStamp, DownSamplingLevels level,
                                                   List<Event> DownSampleEvents_First, List<Event> DownSampleEvents_Average, List<Event> DownSampleEvents_Max,
                                                   List<Event> DownSampleEvents_Min, Map<Integer, List<Event>> sampleGroups, boolean calculateFirst,
                                                   boolean calculateAverage, boolean calculateMax, boolean calculateMin) {
        logger.debug("start calculate Downsampling values.");
        for (Map.Entry<Integer, List<Event>> entry : sampleGroups.entrySet()) {
            if (!entry.getValue().isEmpty()) {
                List<Event> srclist = entry.getValue();
                Event firstEvent = srclist.get(0);
                long timestamp = firstEvent.getEpochSeconds() * 1000L;

                /*
                 * for the down sampling using first method. Only need to add the first event of the group to the result
                 * list.
                 */
                if (calculateFirst) {
                    addPOJOEvent(DownSampleEvents_First, firstEvent, firstEvent.getDBRType(),
                            firstEvent.getEventTimeStamp());
                }

                /*
                 * For other types of down sampling method, we only handle: DBR_SCALAR_BYTE, DBR_SCALAR_DOUBLE,
                 * DBR_SCALAR_FLOAT, DBR_SCALAR_INT, DBR_SCALAR_SHORT events. And the down sampling will use different
                 * get value method to get the correspond data from sampling data of the different event type.
                 */
                switch (firstEvent.getDBRType()) {
                case DBR_SCALAR_BYTE: {
                    if (calculateAverage) {
                        appendAverageForByteEvents(DownSampleEvents_Average, srclist, level);
                    }
                    if (calculateMax) {
                        appendMaxForByteEvents(DownSampleEvents_Max, srclist);
                    }
                    if (calculateMin) {
                        appendMinForByteEvents(DownSampleEvents_Min, srclist);
                    }
                    break;
                }
                case DBR_SCALAR_DOUBLE: {
                    if (calculateAverage) {
                        appendAverageForDoubleEvents(DownSampleEvents_Average, srclist, level);
                    }
                    if (calculateMax) {
                        appendMaxForDoubleEvents(DownSampleEvents_Max, srclist);
                    }
                    if (calculateMin) {
                        appendMinForDoubleEvents(DownSampleEvents_Min, srclist);
                    }
                    break;
                }

                case DBR_SCALAR_FLOAT: {
                    if (calculateAverage) {
                        appendAverageForFloatEvents(DownSampleEvents_Average, srclist, level);
                    }
                    if (calculateMax) {
                        appendMaxForFloatEvents(DownSampleEvents_Max, srclist);
                    }
                    if (calculateMin) {
                        appendMinForFloatEvents(DownSampleEvents_Min, srclist);
                    }
                    break;
                }
                case DBR_SCALAR_INT: {
                    if (calculateAverage) {
                        appendAverageForIntEvents(DownSampleEvents_Average,  srclist, level);
                    }
                    if (calculateMax) {
                        appendMaxForIntEvents(DownSampleEvents_Max, srclist);
                    }
                    if (calculateMin) {
                        appendMinForIntEvents(DownSampleEvents_Min, srclist);
                    }
                    break;
                }
                case DBR_SCALAR_SHORT: {
                    if (calculateAverage) {
                        appendAverageForShortEvents(DownSampleEvents_Average, srclist, level);
                    }
                    if (calculateMax) {
                        appendMaxForShortEvents(DownSampleEvents_Max, srclist);
                    }
                    if (calculateMin) {
                        appendMinForShortEvents(DownSampleEvents_Min, srclist);
                    }
                    break;
                }
                default:
                    break;
                }
            }
        }

        /*
         * All the result should be sorted in the time order.
         */
        if (calculateFirst) {
            DownSampleEvents_First.sort(Comparator.comparingLong(Event::getEpochSeconds));
        }
        if (calculateAverage) {
            DownSampleEvents_Average.sort(Comparator.comparingLong(Event::getEpochSeconds));
        }
        if (calculateMax) {
            DownSampleEvents_Max.sort(Comparator.comparingLong(Event::getEpochSeconds));
        }
        if (calculateMin) {
            DownSampleEvents_Min.sort(Comparator.comparingLong(Event::getEpochSeconds));
        }
        logger.debug("Calculating Downsampling values completed.");
    }

    /**
     * Create buffer for save the down sampled events.
     * 
     * @param createLevels
     *            - how many levels will be created.
     * @param downSapmlingMethods
     *            - the methods to create the downsampling event.
     * @return the buffer.
     */
    static Map<DownSamplingLevels, EnumMap<DownSamplingMethods, List<Event>>> createBufferForDownSampling(
            List<DownSamplingLevels> createLevels, Set<DownSamplingMethods> downSapmlingMethods) {
        /**
         * Create a map to store the down sampled data for each level.
         */
        Map<DownSamplingLevels, EnumMap<DownSamplingMethods, List<Event>>> downSamples = new HashMap<>();

        for (DownSamplingLevels level : createLevels) {
            EnumMap<DownSamplingMethods, List<Event>> samplesInLevel = new EnumMap<DownSamplingMethods, List<Event>>(
                    DownSamplingMethods.class);
            for (DownSamplingMethods m : downSapmlingMethods) {
                samplesInLevel.put(m, new ArrayList<Event>());
            }
            downSamples.put(level, samplesInLevel);
        }
        return downSamples;
    }

    /**
     * Create Down sample values base on the raw data.
     * 
     * @param stream
     *            - the raw data.
     * @param
     *            - the time stamp of first event.
     * @param pVId
     *            - the PVId
     * @param numberOfDownSampleLevels
     *            - the number of down sample levels.
     * @param pvType
     *            - PV type.
     * @return the down sample result.
     */
    public static Map<DownSamplingLevels, EnumMap<DownSamplingMethods, List<Event>>> createDownSampledDateForStream(
            List<Event> stream, byte[] pVId, int numberOfDownSampleLevels, ArchDBRTypes pvType,
            List<DownSamplingLevels> createDownsamplingLevels, Set<DownSamplingMethods> supportedMethods) {
        int firstTimeStamp = (int) stream.get(0).getEpochSeconds();
        Map<DownSamplingLevels, EnumMap<DownSamplingMethods, List<Event>>> downSampledEventsStrorage = createBufferForDownSampling(
                createDownsamplingLevels, supportedMethods);
        /**
         * base on the raw data to create down sampling events.
         */
        Map<Integer, List<Event>> sourceSampleGroups = null;
        Map<Integer, List<Event>> rawEventSampleGrops = DownSampleUtility.splitListBaseOnDownSamplingrLevel(stream,
                DownSamplingLevels.getLevel((byte) 0x00));
        DownSamplingLevels baseLevel = null;
        for (DownSamplingLevels lv : createDownsamplingLevels) {
            for (DownSamplingMethods m : supportedMethods) {
                if (lv.getValueInRowKey() == (byte) 0x00) {
                    sourceSampleGroups = rawEventSampleGrops;
                } else {
                    EnumMap<DownSamplingMethods, List<Event>> baseLevelSamples = downSampledEventsStrorage
                            .get(baseLevel);
                    List<Event> eventList = baseLevelSamples.get(m);
                    sourceSampleGroups = DownSampleUtility.splitListBaseOnDownSamplingrLevel(eventList, lv);
                }
                downSamplingForLevel(sourceSampleGroups, downSampledEventsStrorage, firstTimeStamp, lv,
                        m == DownSamplingMethods.FIRST, m == DownSamplingMethods.AVERAGE, m == DownSamplingMethods.MAX,
                        m == DownSamplingMethods.MIN);
            }
            baseLevel = lv;
        }
        return downSampledEventsStrorage;
    }

    static void downSamplingForLevel(Map<Integer, List<Event>> sampleGroups,
                                     Map<DownSamplingLevels, EnumMap<DownSamplingMethods, List<Event>>> downSamples, int firstTimeStamp,
                                     DownSamplingLevels level, boolean calculateFirst, boolean calculateAverage, boolean calculateMax,
                                     boolean calculateMin) {
        List<Event> DownSampleEvents_First = downSamples.get(level).get(DownSamplingMethods.FIRST);
        List<Event> DownSampleEvents_Average = downSamples.get(level).get(DownSamplingMethods.AVERAGE);
        List<Event> DownSampleEvents_Max = downSamples.get(level).get(DownSamplingMethods.MAX);
        List<Event> DownSampleEvents_Min = downSamples.get(level).get(DownSamplingMethods.MIN);
        DownSampleUtility.calculateDownSamplingValues(firstTimeStamp, level, DownSampleEvents_First,
                DownSampleEvents_Average, DownSampleEvents_Max, DownSampleEvents_Min, sampleGroups, calculateFirst,
                calculateAverage, calculateMax, calculateMin);
    }

    /**
     * Append the minimum short event of the source event list to the down sampled event list. 
     * @param downSampleEvents - a down sampled event list.
     * @param srclist - the source event list to be down sampled.
     */
    public static void appendMinForShortEvents(List<Event> downSampleEvents, List<Event> srclist) {
        Event m = srclist.stream().reduce((a,
                                           b) -> a.getSampleValue().getValue().shortValue() < b.getSampleValue().getValue().shortValue() ? a : b)
                .orElse(null);
        ArchDBRTypes eventType = ArchDBRTypes.DBR_SCALAR_SHORT;
        Timestamp timeStamp = srclist.get(0).getEventTimeStamp();
        addPOJOEvent(downSampleEvents, m, eventType, timeStamp);
    }

    private static void addPOJOEvent(List<Event> downSampleEvents, Event m, ArchDBRTypes eventType,
                                     Timestamp timeStamp) {
        if (m != null) {
            Event eventOut = new POJOEvent(eventType, timeStamp, m.getSampleValue(), 0, 0);
            downSampleEvents.add(eventOut);
        }
    }
    
    /**
     * Append the maximum short event of the source event list to the down sampled event list. 
     * @param downSampleEvents - a down sampled event list.
     * @param srclist - the source event list to be down sampled.
     */
    public static void appendMaxForShortEvents(List<Event> downSampleEvents, List<Event> srclist) {
        Event m = srclist.stream().reduce((a,
                                           b) -> a.getSampleValue().getValue().shortValue() > b.getSampleValue().getValue().shortValue() ? a : b)
                .orElse(null);
        ArchDBRTypes eventType = ArchDBRTypes.DBR_SCALAR_SHORT;
        Timestamp timeStamp = srclist.get(0).getEventTimeStamp();
        addPOJOEvent(downSampleEvents, m, eventType, timeStamp);
    }

    /**
     * Append the average short event of the source event list to the down sampled event list. 
     * @param downSampleEvents - a down sampled event list.
     * @param  - the source event list to be down sampled.
     * @param level - the down sampled level of the event in source event list.
     */
    public static void appendAverageForShortEvents(List<Event> downSampleEvents,
                                                   List<Event> srcDatalist, DownSamplingLevels level) {
        boolean isFirstLevel = (level.getValueInRowKey() == (byte) 0x00);
        Timestamp timeStamp = srcDatalist.get(0).getEventTimeStamp();
        PBMeanScalarDoubleEvent averageEvent = null;
        if (isFirstLevel) {
            OptionalDouble d = srcDatalist.stream().mapToInt(u -> u.getSampleValue().getValue().shortValue()).average();
            if (d.isPresent()) {
                double c = d.getAsDouble();
                int eventCount = downSampleEvents.size();
                averageEvent = new PBMeanScalarDoubleEvent(timeStamp, c, eventCount);
            }
        } else {
            averageEvent = calculateAverageBaseOnMeanEvents(srcDatalist, timeStamp);
        }
        if (averageEvent != null) {
            downSampleEvents.add(averageEvent);
        }
    }

    /**
     * Append the minimum integer event of the source event list to the down sampled event list. 
     * @param downSampleEvents - a down sampled event list.
     * @param srclist - the source event list to be down sampled.
     */
    public static void appendMinForIntEvents(List<Event> downSampleEvents, List<Event> srclist) {
        Event m = srclist.stream().reduce(
                (a, b) -> a.getSampleValue().getValue().intValue() < b.getSampleValue().getValue().intValue() ? a : b)
                .orElse(null);
        ArchDBRTypes eventType = ArchDBRTypes.DBR_SCALAR_INT;
        Timestamp timeStamp = srclist.get(0).getEventTimeStamp();
        addPOJOEvent(downSampleEvents, m, eventType, timeStamp);
    }

    /**
     * Append the maximum integer event of the source event list to the down sampled event list. 
     * @param downSampleEvents - a down sampled event list.
     * @param srclist - the source event list to be down sampled.
     */
    public static void appendMaxForIntEvents(List<Event> downSampleEvents, List<Event> srclist) {
        Event m = srclist.stream().reduce(
                (a, b) -> a.getSampleValue().getValue().intValue() > b.getSampleValue().getValue().intValue() ? a : b)
                .orElse(null);
        ArchDBRTypes eventType = ArchDBRTypes.DBR_SCALAR_INT;
        Timestamp timeStamp = srclist.get(0).getEventTimeStamp();
        addPOJOEvent(downSampleEvents, m, eventType, timeStamp);
    }

    /**
     * Append the average integer event of the source event list to the down sampled event list. 
     * @param downSampleEvents - a down sampled event list.
     * @param  - the source event list to be down sampled.
     * @param level - the down sampled level of the event in source event list.
     */
    public static void appendAverageForIntEvents(List<Event> downSampleEvents, List<Event> srcDatalist,
                                                 DownSamplingLevels level) {
        boolean isFirstLevel = (level.getValueInRowKey() == (byte) 0x00);
        Timestamp timeStamp = srcDatalist.get(0).getEventTimeStamp();
        PBMeanScalarDoubleEvent averageEvent = null;
        if (isFirstLevel) {
            OptionalDouble d = srcDatalist.stream().mapToDouble(u -> u.getSampleValue().getValue().intValue())
                    .average();
            if (d.isPresent()) {
                double c = d.getAsDouble();
                int eventCount = downSampleEvents.size();
                averageEvent = new PBMeanScalarDoubleEvent(timeStamp, c, eventCount);
            }
        } else {
            averageEvent = calculateAverageBaseOnMeanEvents(srcDatalist, timeStamp);
        }
        if (averageEvent != null) {
            downSampleEvents.add(averageEvent);
        }
    }

    /**
     * Append the minimum float event of the source event list to the down sampled event list. 
     * @param downSampleEvents - a down sampled event list.
     * @param srclist - the source event list to be down sampled.
     */
    public static void appendMinForFloatEvents(List<Event> downSampleEvents, List<Event> srclist) {
        Event m = srclist.stream().reduce((a,
                                           b) -> a.getSampleValue().getValue().floatValue() < b.getSampleValue().getValue().floatValue() ? a : b)
                .orElse(null);
        ArchDBRTypes eventType = ArchDBRTypes.DBR_SCALAR_FLOAT;
        Timestamp timeStamp = srclist.get(0).getEventTimeStamp();
        addPOJOEvent(downSampleEvents, m, eventType, timeStamp);
    }

    /**
     * Append the maximum float event of the source event list to the down sampled event list. 
     * @param downSampleEvents - a down sampled event list.
     * @param srclist - the source event list to be down sampled.
     */
    public static void appendMaxForFloatEvents(List<Event> downSampleEvents, List<Event> srclist) {
        Event m = srclist.stream().reduce((a,
                                           b) -> a.getSampleValue().getValue().floatValue() > b.getSampleValue().getValue().floatValue() ? a : b)
                .orElse(null);
        ArchDBRTypes eventType = ArchDBRTypes.DBR_SCALAR_FLOAT;
        Timestamp timeStamp = srclist.get(0).getEventTimeStamp();
        addPOJOEvent(downSampleEvents, m, eventType, timeStamp);
    }

    /**
     * Append the average float event of the source event list to the down sampled event list. 
     * @param downSampleEvents - a down sampled event list.
     * @param- the source event list to be down sampled.
     * @param level - the down sampled level of the event in source event list.
     */
    public static void appendAverageForFloatEvents(List<Event> downSampleEvents,
                                                   List<Event> srcDatalist, DownSamplingLevels level) {
        boolean isFirstLevel = (level.getValueInRowKey() == (byte) 0x00);
        Timestamp timeStamp = srcDatalist.get(0).getEventTimeStamp();
        PBMeanScalarDoubleEvent averageEvent = null;
        if (isFirstLevel) {
            OptionalDouble d = srcDatalist.stream().mapToDouble(u -> u.getSampleValue().getValue().floatValue())
                    .average();
            if (d.isPresent()) {
                double c = d.getAsDouble();
                int eventCount = downSampleEvents.size();
                averageEvent = new PBMeanScalarDoubleEvent(timeStamp, c, eventCount);
            }
        } else {
            averageEvent = calculateAverageBaseOnMeanEvents(srcDatalist, timeStamp);
        }
        if (averageEvent != null) {
            downSampleEvents.add(averageEvent);
        }
    }

    private static PBMeanScalarDoubleEvent calculateAverageBaseOnMeanEvents(List<Event> srcDatalist,
            Timestamp timeStamp) {
        PBMeanScalarDoubleEvent averageEvent;
        averageEvent = srcDatalist.stream().map(a -> ((PBMeanScalarDoubleEvent) a)).reduce((a, b) -> {
            return new PBMeanScalarDoubleEvent(timeStamp,
                    (a.getCount() * a.getValue() + b.getCount() * b.getValue()) / (a.getCount() + b.getCount()),
                    (a.getCount() + b.getCount()));
        }).orElse(null);
        return averageEvent;
    }

    /**
     * Append the minimum double event of the source event list to the down sampled event list. 
     * @param downSampleEvents - a down sampled event list.
     * @param srclist - the source event list to be down sampled.
     */
    public static void appendMinForDoubleEvents(List<Event> downSampleEvents, List<Event> srclist) {
        Event m = srclist.stream().reduce((a,
                                           b) -> a.getSampleValue().getValue().doubleValue() < b.getSampleValue().getValue().doubleValue() ? a : b)
                .orElse(null);
        ArchDBRTypes eventType = ArchDBRTypes.DBR_SCALAR_DOUBLE;
        Timestamp timeStamp = srclist.get(0).getEventTimeStamp();
        addPOJOEvent(downSampleEvents, m, eventType, timeStamp);
    }

    /**
     * Append the maximum double event of the source event list to the down sampled event list. 
     * @param downSampleEvents - a down sampled event list.
     * @param srclist - the source event list to be down sampled.
     */
    public static void appendMaxForDoubleEvents(List<Event> downSampleEvents, List<Event> srclist) {
        Event m = srclist.stream().reduce((a,
                                           b) -> a.getSampleValue().getValue().doubleValue() > b.getSampleValue().getValue().doubleValue() ? a : b)
                .orElse(null);
        ArchDBRTypes eventType = ArchDBRTypes.DBR_SCALAR_DOUBLE;
        Timestamp timeStamp = srclist.get(0).getEventTimeStamp();
        addPOJOEvent(downSampleEvents, m, eventType, timeStamp);
    }

    /**
     * Append the average double event of the source event list to the down sampled event list. 
     * @param downSampleEvents - a down sampled event list.
     * @param - the source event list to be down sampled.
     * @param level - the down sampled level of the event in source event list.
     */
    public static void appendAverageForDoubleEvents(List<Event> downSampleEvents,
                                                    List<Event> srcDatalist, DownSamplingLevels level) {
        boolean isFirstLevel = (level.getValueInRowKey() == (byte) 0x00);
        Timestamp timeStamp = srcDatalist.get(0).getEventTimeStamp();
        PBMeanScalarDoubleEvent averageEvent = null;
        if (isFirstLevel) {
            OptionalDouble d = srcDatalist.stream().mapToDouble(u -> u.getSampleValue().getValue().doubleValue())
                    .average();
            if (d.isPresent()) {
                double c = d.getAsDouble();
                int eventCount = downSampleEvents.size();
                averageEvent = new PBMeanScalarDoubleEvent(timeStamp, c, eventCount);
            }
        } else {
            averageEvent = calculateAverageBaseOnMeanEvents(srcDatalist, timeStamp);
        }
        if (averageEvent != null) {
            downSampleEvents.add(averageEvent);
        }
    }

    /**
     * Append the minimum bye event of the source event list to the down sampled event list. 
     * @param downSampleEvents - a down sampled event list.
     * @param srclist - the source event list to be down sampled.
     */
    public static void appendMinForByteEvents(List<Event> downSampleEvents, List<Event> srclist) {
        Event m = srclist.stream().reduce(
                (a, b) -> a.getSampleValue().getValue().byteValue() < b.getSampleValue().getValue().byteValue() ? a : b)
                .orElse(null);
        ArchDBRTypes eventType = ArchDBRTypes.DBR_SCALAR_BYTE;
        Timestamp timeStamp = srclist.get(0).getEventTimeStamp();
        addPOJOEvent(downSampleEvents, m, eventType, timeStamp);
    }

    /**
     * Append the maximum bye event of the source event list to the down sampled event list. 
     * @param downSampleEvents - a down sampled event list.
     * @param srclist - the source event list to be down sampled.
     */
    public static void appendMaxForByteEvents(List<Event> downSampleEvents, List<Event> srclist) {
        Event m = srclist.stream().reduce(
                (a, b) -> a.getSampleValue().getValue().byteValue() > b.getSampleValue().getValue().byteValue() ? a : b)
                .orElse(null);
        ArchDBRTypes eventType = ArchDBRTypes.DBR_SCALAR_BYTE;
        Timestamp timeStamp = srclist.get(0).getEventTimeStamp();
        addPOJOEvent(downSampleEvents, m, eventType, timeStamp);
    }

    /**
     * Append the average byte event of the source event list to the down sampled event list. 
     * @param downSampleEvents - a down sampled event list.
     * @param  - the source event list to be down sampled.
     * @param level - the down sampled level of the event in source event list.
     */
    public static void appendAverageForByteEvents(List<Event> downSampleEvents,
                                                  List<Event> srcDatalist, DownSamplingLevels level) {        
        boolean isFirstLevel = (level.getValueInRowKey() == (byte) 0x00);
        Timestamp timeStamp = srcDatalist.get(0).getEventTimeStamp();
        PBMeanScalarDoubleEvent averageEvent = null;
        if (isFirstLevel) {
            OptionalDouble d = srcDatalist.stream().mapToInt(u -> u.getSampleValue().getValue().byteValue()).average();
            if (d.isPresent()) {
                double c = d.getAsDouble();
                int eventCount = downSampleEvents.size();
                averageEvent = new PBMeanScalarDoubleEvent(timeStamp, c, eventCount);
            }
        } else {
            averageEvent = calculateAverageBaseOnMeanEvents(srcDatalist, timeStamp);
        }
        if (averageEvent != null) {
            downSampleEvents.add(averageEvent);
        }
    }

    /**
     * Get the down sampling method from the rowkey
     * 
     * @param rowKey
     *            - the row key
     * @return down sampling method
     */
    public static DownSamplingMethods getDownSamplingTypeFromRowkey(byte[] rowKey) {
        return DownSamplingMethods.getDownSamplingMethod((byte) (rowKey[CommonConstants.PVID_SIZE] & 0xf0));
    }

    /**
     * Get the down sampling method from the rowkey
     * 
     * @param rowKey
     *            - the row key
     * @return down sampling level
     */
    public static DownSamplingLevels getDownSamplingLevleFromRowkey(byte[] rowKey) {
        return DownSamplingLevels.getLevel((byte) (rowKey[CommonConstants.PVID_SIZE] & 0xf0));
    }

    /**
     * find the minimum event in the short event list.
     * 
     * @param dlist
     *            - the short event list.
     * @return the minimum event.
     */
    public static Event createMinForShortEvents(List<Event> dlist) {
        return dlist.stream().reduce((a,
                b) -> a.getSampleValue().getValue().shortValue() < b.getSampleValue().getValue().shortValue() ? a : b)
                .orElse(null);
    }

    /**
     * find the maximum event in the short event list.
     * 
     * @param dlist
     *            - the short event list.
     * @return the maximum event.
     */
    public static Event createMaxForShortEvents(List<Event> dlist) {
        return dlist.stream().reduce((a,
                b) -> a.getSampleValue().getValue().shortValue() > b.getSampleValue().getValue().shortValue() ? a : b)
                .orElse(null);
    }

    /**
     * create a average event for the short event list.
     * 
     * @param dlist
     *            - the short event list.
     * @return the average event.
     */
    public static Event createAverageForShortEvents(List<Event> dlist) {
        Event firstEvent = dlist.get(0);
        long timestamp = firstEvent.getEpochSeconds() * 1000;
        OptionalDouble c = dlist.stream().mapToDouble(u -> u.getSampleValue().getValue().shortValue()).average();
        if (c.isPresent()) {
            Short v = (short) c.orElse(0);
            POJOEvent ev = null;
            ev = new POJOEvent(ArchDBRTypes.DBR_SCALAR_SHORT, new Timestamp(timestamp),
                    new ScalarValue<Short>((short) v), ((DBRTimeEvent) firstEvent).getStatus(),
                    ((DBRTimeEvent) firstEvent).getSeverity());
            return ev;
        }
        return null;
    }

    /**
     * find the minimum event in the integer event list.
     * 
     * @param dlist
     *            - the integer event list.
     * @return the minimum event.
     */
    public static Event createMinForIntEvents(List<Event> dlist) {
        return dlist.stream().reduce(
                (a, b) -> a.getSampleValue().getValue().intValue() < b.getSampleValue().getValue().intValue() ? a : b)
                .orElse(null);
    }

    /**
     * find the maximum event in the integer event list.
     * 
     * @param dlist
     *            - the integer event list.
     * @return the maximum event.
     */
    public static Event createMaxForIntEvents(List<Event> dlist) {
        return dlist.stream().reduce(
                (a, b) -> a.getSampleValue().getValue().intValue() > b.getSampleValue().getValue().intValue() ? a : b)
                .orElse(null);
    }

    /**
     * create a average event for the integer event list.
     * 
     * @param dlist
     *            - the integer event list.
     * @return the average event.
     */
    public static Event createAverageForIntEvents(List<Event> dlist) {
        Event firstEvent = dlist.get(0);
        long timestamp = firstEvent.getEpochSeconds() * 1000;
        OptionalDouble c = dlist.stream().mapToDouble(u -> u.getSampleValue().getValue().intValue()).average();
        if (c.isPresent()) {
            Integer v = (int) c.orElse(0);
            POJOEvent ev = null;
            ev = new POJOEvent(ArchDBRTypes.DBR_SCALAR_INT, new Timestamp(timestamp), new ScalarValue<Integer>(v),
                    ((DBRTimeEvent) firstEvent).getStatus(), ((DBRTimeEvent) firstEvent).getSeverity());
            return ev;
        }
        return null;
    }

    /**
     * find the minimum event in the float event list.
     * 
     * @param dlist
     *            - the float event list.
     * @return the minimum event.
     */
    public static Event createMinForFloatEvents(List<Event> dlist) {
        return dlist.stream().reduce((a,
                b) -> a.getSampleValue().getValue().floatValue() < b.getSampleValue().getValue().floatValue() ? a : b)
                .orElse(null);
    }

    /**
     * find the maximum event in the float event list.
     * 
     * @param dlist
     *            - the float event list.
     * @return the minimum event.
     */
    public static Event createMaxForFloatEvents(List<Event> dlist) {
        return dlist.stream().reduce((a,
                b) -> a.getSampleValue().getValue().floatValue() > b.getSampleValue().getValue().floatValue() ? a : b)
                .orElse(null);
    }

    /**
     * create a average event for the float event list.
     * 
     * @param dlist
     *            - the float event list.
     * @return the average event.
     */
    public static Event createAverageForFloatEvents(List<Event> dlist) {
        Event firstEvent = dlist.get(0);
        long timestamp = firstEvent.getEpochSeconds() * 1000;
        OptionalDouble c = dlist.stream().mapToDouble(u -> u.getSampleValue().getValue().floatValue()).average();
        if (c.isPresent()) {
            Float v = (float) c.orElse(0);
            POJOEvent ev = null;
            ev = new POJOEvent(ArchDBRTypes.DBR_SCALAR_FLOAT, new Timestamp(timestamp), new ScalarValue<Float>(v),
                    ((DBRTimeEvent) firstEvent).getStatus(), ((DBRTimeEvent) firstEvent).getSeverity());
            return ev;
        }
        return null;
    }

    /**
     * find the minimum event in the double event list.
     * 
     * @param dlist
     *            - the double event list.
     * @return the minimum event.
     */
    public static Event createMinForDoubleEvents(List<Event> dlist) {
        return dlist.stream().reduce((a,
                b) -> a.getSampleValue().getValue().doubleValue() < b.getSampleValue().getValue().doubleValue() ? a : b)
                .orElse(null);
    }

    /**
     * find the maximum event in the double event list.
     * 
     * @param dlist
     *            - the double event list.
     * @return the maximum event.
     */
    public static Event createMaxForDoubleEvents(List<Event> dlist) {
        return dlist.stream().reduce((a,
                b) -> a.getSampleValue().getValue().doubleValue() > b.getSampleValue().getValue().doubleValue() ? a : b)
                .orElse(null);
    }

    /**
     * create a average event for the double event list.
     * 
     * @param dlist
     *            - the double event list.
     * @return the average event.
     */
    public static Event createAverageForDoubleEvents(List<Event> dlist) {
        Event firstEvent = dlist.get(0);
        long timestamp = firstEvent.getEpochSeconds() * 1000;
        OptionalDouble c = dlist.stream().mapToDouble(u -> u.getSampleValue().getValue().doubleValue()).average();
        if (c.isPresent()) {
            Double v = c.orElse(0);
            POJOEvent ev = null;
            ev = new POJOEvent(ArchDBRTypes.DBR_SCALAR_DOUBLE, new Timestamp(timestamp), new ScalarValue<Double>(v),
                    ((DBRTimeEvent) firstEvent).getStatus(), ((DBRTimeEvent) firstEvent).getSeverity());
            return ev;
        }
        return null;
    }

    /**
     * find the minimum event in the byte event list.
     * 
     * @param dlist
     *            - the byte event list.
     * @return the minimum event.
     */
    public static Event createMinForByteEvents(List<Event> dlist) {
        return dlist.stream().reduce(
                (a, b) -> a.getSampleValue().getValue().byteValue() < b.getSampleValue().getValue().byteValue() ? a : b)
                .orElse(null);
    }

    /**
     * find the maximum event in the byte event list.
     * 
     * @param dlist
     *            - the byte event list.
     * @return the maximum event.
     */
    public static Event createMaxForByteEvents(List<Event> dlist) {
        return dlist.stream().reduce(
                (a, b) -> a.getSampleValue().getValue().byteValue() > b.getSampleValue().getValue().byteValue() ? a : b)
                .orElse(null);
    }

    /**
     * create a average event for the byte event list.
     * 
     * @param dlist
     *            - the byte event list.
     * @return the average event.
     */
    public static Event createAverageForByteEvents(List<Event> dlist) {
        Event firstEvent = dlist.get(0);
        long timestamp = firstEvent.getEpochSeconds() * 1000;
        OptionalDouble c = dlist.stream().mapToInt(u -> u.getSampleValue().getValue().byteValue()).average();
        if (c.isPresent()) {
            Byte v = (byte) c.orElse(0);
            POJOEvent ev = null;
            ev = new POJOEvent(ArchDBRTypes.DBR_SCALAR_BYTE, new Timestamp(timestamp), new ScalarValue<Byte>(v),
                    ((DBRTimeEvent) firstEvent).getStatus(), ((DBRTimeEvent) firstEvent).getSeverity());
            return ev;
        }
        return null;
    }
}
