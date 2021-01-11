package com.hlsii.service;


import cls.stat_information_plugin.ImmutableStatInformation;
import cls.stat_information_plugin.StatInformation;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cosylab.hadars.archiverappliance.retrieval.client.EventStream;
import com.cosylab.hadars.archiverappliance.retrieval.client.RawDataRetrieval;
import hadarshbaseplugin.commdef.PostProcessing;
import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.client.config.XmlClientConfigBuilder;
import com.hazelcast.core.HazelcastInstance;
import com.hlsii.commdef.Appliance;
import com.hlsii.commdef.PVDataFromStore;
import com.hlsii.commdef.PVDataStore;
import com.hlsii.commdef.RetrieveParms;
import com.hlsii.util.ApplianceParser;
import com.hlsii.util.EventUtil;
import com.hlsii.vo.RetrieveData;
import org.apache.commons.lang3.text.StrLookup;
import org.apache.commons.lang3.text.StrSubstitutor;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.epics.archiverappliance.Event;
import org.epics.archiverappliance.common.PartitionGranularity;
import org.epics.archiverappliance.config.ApplianceInfo;
import org.epics.archiverappliance.config.PVTypeInfo;
import org.epics.archiverappliance.utils.ui.URIUtils;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PreDestroy;
import java.io.IOException;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.URI;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.text.MessageFormat;
import java.util.*;
import java.util.stream.Collectors;

import static com.hlsii.util.HttpConnector.sendRequestToRemote;
import static com.hlsii.util.TimeUtil.convertToISO8601String;

/**
 * Implement IAARetrieveService
 */
@Service
public class AARetrieveServiceImplementation implements IAARetrieveService {
    private static Logger logger = Logger.getLogger(AARetrieveServiceImplementation.class.getName());

    private Map<String, ApplianceInfo> pv2appliancemapping = null;
    private Map<String, PVTypeInfo> typeInfos = null;
    private HazelcastInstance shutdownHzInstance;

    @Autowired
    private IHazelcastClusterStateMonitor hazelcastClusterStateMonitor;


    @Override
    public boolean initialize() throws IOException {
        HazelcastInstance hzInstance;

        // Set the thread count to control how may threads this library spawns.
        Properties hzThreadCounts = new Properties();
        if (System.getenv().containsKey("ARCHAPPL_ALL_APPS_ON_ONE_JVM")) {
          //  logger.info("Reducing the generic clustering thread counts.");
            hzThreadCounts.put("hazelcast.clientengine.thread.count", "2");
            hzThreadCounts.put("hazelcast.operation.generic.thread.count", "2");
            hzThreadCounts.put("hazelcast.operation.thread.count", "2");
        }

        try {
            logger.debug("Initializing a Hazelcast client.");

            ClientConfig clientConfig = new XmlClientConfigBuilder().build();
            clientConfig.getGroupConfig().setName("archappl");
            clientConfig.getGroupConfig().setPassword("archappl");
            clientConfig.setExecutorPoolSize(4);

            List<String> addressList = getServerAddress();

            if (addressList == null || addressList.isEmpty()) {
                logger.error("Parse AA configuration failure.");
                return false;
            }

            clientConfig.getNetworkConfig().setAddresses(addressList);
            clientConfig.getNetworkConfig().setConnectionAttemptLimit(0); // reconnect forever

            clientConfig.setProperty("hazelcast.logging.type", "log4j");

            if (!hzThreadCounts.isEmpty()) {
                logger.info("Reducing the generic clustering thread counts.");
                clientConfig.getProperties().putAll(hzThreadCounts);
            }

            if (!logger.isDebugEnabled()) {
                // The client code logs some SEVERE exceptions on shutdown when deploying on the same Tomcat container.
                // These exceptions are confusing; ideally, we would not have to set the log levels like so.
                Logger.getLogger("com.hazelcast.client.spi.impl.ClusterListenerThread").setLevel(Level.OFF);
                Logger.getLogger("com.hazelcast.client.spi.ClientPartitionService").setLevel(Level.OFF);
            }
            logger.info(MessageFormat.format("client network config conn attempt limit: {0}" +
                            "client network config conn attempt period: {1}" +
                            "client network config conn timeout: {2}" +
                            "client network config addresses: {3}" +
                            "client network config is redo: {4}" +
                            "client config properties: {5}",
                    clientConfig.getNetworkConfig().getConnectionAttemptLimit(),
                    clientConfig.getNetworkConfig().getConnectionAttemptPeriod(),
                    clientConfig.getNetworkConfig().getConnectionTimeout(),
                    clientConfig.getNetworkConfig().getAddresses().stream().map(Object::toString).collect(Collectors.joining(",")),
                    clientConfig.getNetworkConfig().isRedoOperation(),
                    clientConfig.getProperties().toString()));

            hzInstance = HazelcastClient.newHazelcastClient(clientConfig);

            // TODO: Add destroy method.
            //final HazelcastInstance shutdownHzInstance = hzInstance;
            shutdownHzInstance = hzInstance;

            hazelcastClusterStateMonitor.initialize(hzInstance);
            /*
            protected LinkedList<Runnable> shutdownHooks = new LinkedList<Runnable>();
            final HazelcastInstance shutdownHzInstance = hzinstance;
                shutdownHooks.add(0, new Runnable() {
                @Override
                public void run() {
                    logger.debug("Shutting down clustering instance in webapp " + warFile.toString());
                    shutdownHzInstance.shutdown();
                }
            );
            */

        } catch (Exception ex) {
            logger.error("Exception adding client to cluster", ex);
            throw new IOException(ex);
        }

        pv2appliancemapping = hzInstance.getMap("pv2appliancemapping");
        typeInfos = hzInstance.getMap("typeinfo");

        return true;
    }

    /**
     * Get all configured ArchiverAppliance
     *
     * @return a list of {@link Appliance}
     */
    private List<String> getServerAddress() throws Exception {
        List<String> addressList = new ArrayList<>();

        List<Appliance> appliances = ApplianceParser.getAppliances();

        if (appliances == null || appliances.isEmpty()) {
            logger.error("No Archiver Appliance configured.");
            return addressList;
        }

        try {
            for (Appliance appliance : appliances) {
                if (appliance != null && appliance.getClusterInetport() != null && !appliance.getClusterInetport().isEmpty()) {
                    String clusterInetPortStr = appliance.getClusterInetport();
                    logger.debug(MessageFormat.format("Cluster_inetport {0} for appliance {1}",
                            clusterInetPortStr, appliance.getIdentity()));
                    String[] addressParts = clusterInetPortStr.split(":");
                    String hostName = addressParts[0];
                    try {
                        InetAddress inetAddress = InetAddress.getByName(hostName);
                        if (!hostName.equals("localhost") && inetAddress.isLoopbackAddress()) {
                            logger.info(MessageFormat.format("Address for this appliance -- {0} is a loopback address. " +
                                            "Changing this to 127.0.0.1 to clustering happy",
                                    inetAddress.toString()));
                            hostName = InetAddress.getByName("127.0.0.1").getHostAddress();
                        }
                    } catch (Exception ex) {
                        logger.info(MessageFormat.format("hostName {0} is not reachable. Use it directly.", hostName));
                    }

                    String serverAddress = hostName + ":" + Integer.parseInt(addressParts[1]);
                    logger.info(MessageFormat.format("Add Hazelcast server address {0}", serverAddress));
                    addressList.add(serverAddress);
                }
            }
            return addressList;

        } catch (Exception ex) {
            logger.error("Parse AA address failure.", ex);
            return null;
        }
    }

    @Override
    public RetrieveData retrieveData(String pvName, RetrieveParms parms) {
        ApplianceInfo applianceInfo = pv2appliancemapping.get(pvName);
        if (applianceInfo == null) {
            logger.error(MessageFormat.format("The PV {0} is not archived in any appliance.", pvName));
            return null;
        }

        // fetch PV data from AA.

        RetrieveData retrieveData = new RetrieveData(pvName);

        String downSamplingPVName = constructDownSamplingPVName(pvName, parms.getPostProcessIdentity(),
                parms.getIntervalSeconds());
        //logger.debug(MessageFormat.format("Retrieve data from AA with PV and downsampling name: {0}", downSamplingPVName));

        HashMap<String, String> otherParams = new HashMap<>();
        otherParams.put("fetchLatestMetadata", parms.getFetchLatestMetadata() ? "true" : "false");

        // Get PV data from AA over PB/HTTP
        RawDataRetrieval rawDataRetrieval = new RawDataRetrieval(applianceInfo.getDataRetrievalURL());
        EventStream stream = rawDataRetrieval.getDataForPV(downSamplingPVName, parms.getFrom(), parms.getTo(),
                false, otherParams);

        // check whether the event stream is null.
        if (stream == null) {
           // logger.error(MessageFormat.format("Get null event stream for the PV {0}.", pvName));
            return null;
        }

        // Construct meta data JSON.
        JSONObject metadata = GenerateMetaJSON(pvName, stream);
        if (metadata != null) {
            retrieveData.setMeta(metadata);
        }

        // Construct data JSON.
        JSONArray data = new JSONArray();
        long totalValues = 0;
        boolean v4Flag = isV4(pvName);
        try {
            for (Event dbrEvent : stream) {
                JSONObject eventObject = RetrieveServiceImplementation.Event2JSON(pvName, dbrEvent, parms.getPvDataFormat(), v4Flag);
                if (eventObject != null) {
                    data.add(eventObject);
                }
                totalValues++;
            }
            //  logger.debug(MessageFormat.format("Get {0} events for the PV {1} from AA.", totalValues, pvName));

            retrieveData.setData(data);
        } finally {
            try {
                stream.close();
            } catch (Exception ex) {
                logger.error(MessageFormat.format("Exception: close a event stream for PV {0}.", pvName));
            }
        }
        return retrieveData;
    }

    @Override
    public PVDataFromStore getData(String pvName, RetrieveParms parms) {
        RetrieveData pvData = retrieveData(pvName, parms);
        if (pvData == null) {
            return null;
        }
        return new PVDataFromStore(pvName, PVDataStore.AA, pvData);
    }

    /**
     * Construct a downSampling PV name according to: PV name, downSampling method and interval.
     *
     * @param pvName               the PV name.
     * @param downSamplingIdentify the downSampling method.
     * @param intervalSeconds      the downSampling interval (second).
     * @return the construct name string.
     */
    private String constructDownSamplingPVName(String pvName, PostProcessing downSamplingIdentify, int intervalSeconds) {
        if (downSamplingIdentify == PostProcessing.NONE) {
            return pvName;
        }
        return downSamplingIdentify.toString() + "_" + intervalSeconds + "(" + pvName + ")";
    }


    /**
     * Generate meta data JSONObject from {@link EventStream}
     *
     * @param pvName the PV name.
     * @param stream the {@link EventStream}
     * @return the meta data JSONObject or null if there is any error.
     */
    private JSONObject GenerateMetaJSON(String pvName, EventStream stream) {
        JSONObject metaObject = new JSONObject();

        try {
            List<edu.stanford.slac.archiverappliance.PB.EPICSEvent.FieldValue> headerList =
                    stream.getPayLoadInfo().getHeadersList();

            for (edu.stanford.slac.archiverappliance.PB.EPICSEvent.FieldValue fieldValue : headerList) {
                metaObject.put(fieldValue.getName(), fieldValue.getVal());
            }
            metaObject.put("name", stream.getPayLoadInfo().getPvname());
        } catch (Exception ex) {
            logger.error(MessageFormat.format("Exception: Generate meta data for PV {0} from stream.", pvName), ex);
            return null;
        }

        return metaObject;
    }

    @Override
    public List<PVDataStore> resolveDataStore(String pvName, Timestamp start, Timestamp end, Event firstKnownEventInHadoop) {
        List<PVDataStore> dataStores = new ArrayList<>();

        if (firstKnownEventInHadoop != null) {
            // if firstKnownEventInHadoop is not null, resolve data stores according it.

            if (firstKnownEventInHadoop.getEventTimeStamp().after(start)) {
                dataStores.add(PVDataStore.AA);
            }

            if (firstKnownEventInHadoop.getEventTimeStamp().before(end)) {
                dataStores.add(PVDataStore.HADOOP);
            }

        } else {
            // firstKnownEventInHadoop is null, retrieve data from AA always.

            dataStores.add(PVDataStore.AA);

            // check whether there is data in Hadoop according to policy.
            try {
                PVTypeInfo pvTypeInfo = typeInfos.get(pvName);
                if (pvTypeInfo == null) {
                    // if PVTypeInfo cannot be got, add Hadoop store anyway.
                    dataStores.add(PVDataStore.HADOOP);
                } else {
                    DateTime dateTime = new DateTime(DateTimeZone.UTC);
                    Timestamp currentTimestamp = new Timestamp(dateTime.getMillis());
                    // if currentTimestamp is later than start, and the time different is larger than the Hadoop partition,
                    // add Hadoop store.
                    if (currentTimestamp.after(start) &&
                            (currentTimestamp.getTime() - start.getTime() >= getHadoopPartition(pvTypeInfo))) {
                        dataStores.add(PVDataStore.HADOOP);
                    }
                }
            } catch (Exception ex) {
                logger.error("Exception on get PVTypeInfo from hazelcast for PV " + pvName, ex);
                dataStores.add(PVDataStore.HADOOP);
            }
        }
        return dataStores;
    }

    @Override
    public float getEventRate(String pvName) {
        float rate = 0.0f;
        try {
            rate = 1 / getSamplePeriod(pvName);
        } catch (Exception ex) {
            logger.error("Cannot get the event rate, return 0.", ex);
        }
        return rate;
    }

    @Override
    public float getSamplePeriod(String pvName) {
        float period = 0.0f;
        PVTypeInfo pvTypeInfo = typeInfos.get(pvName);
        /**
         * There are two rates in pvTypeInfo: pvTypeInfo.getSamplingPeriod() and pvTypeInfo.getComputedEventRate()
         * 1) getSamplingPeriod(): This is the sample period defined in the policy file.
         * 2) getComputedEventRate(): This is the actual event rate. This is calculated according to the number of event
         *    which is generated when the PV value changed if the sampling mode is "monitor".
         *
         * Why getSamplingPeriod() is returned?
         * 1) Each PV value changing generates one event.
         * 2) The event is written into the SampleBuffer.
         * 3) The size of SampleBuffer is calculated according to
         *    3-1) the sample period defined in the policy file,
         *    3-2) the period of moving data from SampleBuffer to dataStores[0]
         *    3-3) one event size.
         * 4) If the SampleBuffer is full, when new event is written, the oldest event is dropped, new event is added.
         * 5) So, if the getComputedEventRate() is larger than getSamplingPeriod(), some events are dropped.
         * 6) Therefore, treating getSamplingPeriod() as the event rate is reasonable.
         */
        if (pvTypeInfo == null) {
            logger.info("Cannot get the event sample period, return 0. pv = " + pvName);
        } else {
            period = pvTypeInfo.getSamplingPeriod();
        }
        return period;
    }

    @Override
    public long calculateEventValueSize(String pvName) {
        long size = 0L;
        try {
            PVTypeInfo pvTypeInfo = typeInfos.get(pvName);
            size = 1L * pvTypeInfo.getElementCount() * EventUtil.getDbrTypeSize(pvTypeInfo.getDBRType());
        } catch (Exception ex) {
            logger.error("Cannot calculate the event value size, return 0.", ex);
        }
        return size;
    }

    @Override
    public boolean isV4(String pvName) {
        boolean isv4 = false;
        try {
            PVTypeInfo pvTypeInfo = typeInfos.get(pvName);
            if (pvTypeInfo != null) {
                isv4 = pvTypeInfo.isUsePVAccess();
            } else {
                logger.error("PVTypeInfo is null for PV " + pvName);
            }
        } catch (Exception ex) {
            logger.error("cannot get PVTypeInfo for PV " + pvName, ex);
        }
        return isv4;
    }

    /**
     * Get the Hadoop Partition in milliseconds.
     *
     * @param pvTypeInfo the {@link PVTypeInfo}
     * @return the milliseconds of the Hadoop. 0 if there is no Hadoop in the data store or error.
     */
    private static long getHadoopPartition(PVTypeInfo pvTypeInfo) {
        PartitionGranularity partitionGranularity = PartitionGranularity.PARTITION_5MIN;
        int holdETLForPartions = 0;

        boolean hadoopExist = false;
        for (String store : pvTypeInfo.getDataStores()) {
            try {
                String srcURIStr = expandMacros(store);
                URI srcURI = new URI(srcURIStr);
                String pluginIdentifier = srcURI.getScheme();
                if (pluginIdentifier.equalsIgnoreCase("hadoop")) {
                    // The Hadoop is in the data store, use the previous data store to calculate the Hadoop partition.
                    hadoopExist = true;
                    break;
                }

                HashMap<String, String> queryNVPairs = URIUtils.parseQueryString(srcURI);
                if (queryNVPairs.containsKey("partitionGranularity")) {
                    partitionGranularity = PartitionGranularity.valueOf(queryNVPairs.get("partitionGranularity"));
                } else {
                    logger.error(MessageFormat.format("partitionGranularity is not filled in data store: {0}", store));
                    return 0;
                }

                if (queryNVPairs.containsKey("hold")) {
                    holdETLForPartions = Integer.parseInt(queryNVPairs.get("hold"));
                }
            } catch (Exception ex) {
                logger.error(ex);
                return 0;
            }
        }

        //logger.debug(MessageFormat.format("partitionGranularity: {0}, holdETLForPartions: {1}", partitionGranularity, holdETLForPartions));

        if (hadoopExist) {
            return partitionGranularity.getApproxSecondsPerChunk() * holdETLForPartions * 1000;
        }

        logger.info("Not hadoop in the data store.");
        return 0;
    }

    /**
     * Expands macros in the plugin definition strings.
     * Checks java.system.properties first (passed in with a -D to the JVM)
     * Then checks the environment (for example, using export in Linux).
     * If we are not able to match in either place, we return as is.
     * <p>
     * For example, if we did <code>export ARCHAPPL_SHORT_TERM_FOLDER=/dev/test</code>, and then used
     * <code>pbraw://${ARCHAPPL_SHORT_TERM_FOLDER}<code> in the policy datastore definition,
     * these would be expanded into <code>pbraw:///dev/test<code></code>
     *
     * @param srcURIStr
     * @return
     */
    private static String expandMacros(String srcURIStr) {
        StrSubstitutor sub = new StrSubstitutor(new StrLookup<String>() {
            @Override
            public String lookup(String name) {
                String valueFromProps = System.getProperty(name);
                if (valueFromProps != null) {
                    if (logger.isDebugEnabled())
                       // logger.debug("Resolving " + name + " from system properties into " + valueFromProps);
                    return valueFromProps;
                }
                String valueFromEnvironment = System.getenv(name);
                if (valueFromEnvironment != null) {
                    if (logger.isDebugEnabled())
                      //  logger.debug("Resolving " + name + " from system environment into " + valueFromEnvironment);
                    return valueFromEnvironment;
                }
                logger.error("Unable to find " + name + " in either the java system properties or the system " +
                        "environment. Returning as is without expanding");
                return name;
            }
        });
        return sub.replace(srcURIStr);
    }

    @PreDestroy
    public void destroy() {
        logger.info("Receive shutdown signal.");
        if (shutdownHzInstance != null) {
            logger.info("Shutting down clustering instance in HADARS.");
            shutdownHzInstance.shutdown();
        }
    }

    @Override
    public Set<String> getAvailableAA() {
        return hazelcastClusterStateMonitor.getAvailableAA();
    }

    @Override
    public Collection<PVTypeInfo> getAllPVInfo() {
        return typeInfos.values();
    }

    @Override
    public StatInformation getStat(String pvName, Timestamp startTime, Timestamp endTime) {
        ApplianceInfo applianceInfo = pv2appliancemapping.get(pvName);
        if (applianceInfo == null) {
            logger.error(MessageFormat.format("The PV {0} is not archived in any appliance.", pvName));
            return ImmutableStatInformation.builder().count(0).sum(0).squareSum(0).mean(0).deviation(0).rms(0).max(0)
                    .min(0).build();
        }

        StringWriter buf = new StringWriter();
        String encode;
        try {
            encode = URLEncoder.encode(pvName, "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            encode = pvName;
        }
        buf.append(applianceInfo.getDataRetrievalURL())
                .append("/data/getData.stat")
                .append("?pv=").append(encode)
                .append("&from=").append(convertToISO8601String(startTime))
                .append("&to=").append(convertToISO8601String(endTime));

        String statURL = buf.toString();
        // logger.info("URL to fetch stat data for PV " + pvName + " is " + statURL);

        try {
            String statJsonString = sendRequestToRemote(statURL);
            // { "count": 3014, "sum": 45214.01540840646, "squareSum": 1282440.5212190528,
            // "mean": 15.00133225229147, "deviation": 14.160547063426653, "rms": 20.62751881801611,
            // "max": 34.97672348429877, "min": -4.976723483511744 }
            JSONObject statData = JSONObject.parseObject(statJsonString);
            if (statData != null) {
                return ImmutableStatInformation.builder()
                        .count(Long.parseLong(statData.get("count").toString()))
                        .sum(Double.parseDouble(statData.get("sum").toString()))
                        .squareSum(Double.parseDouble(statData.get("squareSum").toString()))
                        .mean(Double.parseDouble(statData.get("mean").toString()))
                        .deviation(Double.parseDouble(statData.get("deviation").toString()))
                        .rms(Double.parseDouble(statData.get("rms").toString()))
                        .max(Double.parseDouble(statData.get("max").toString()))
                        .min(Double.parseDouble(statData.get("min").toString()))
                        .build();
            }
        } catch (Exception ex) {
            logger.warn("Exception fetching stat data for pv " + pvName, ex);
        }
        return ImmutableStatInformation.builder().count(0).sum(0).squareSum(0).mean(0).deviation(0).rms(0).max(0)
                .min(0).build();
    }

}