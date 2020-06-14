package com.hlsii.util;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Properties;

/**
 * Site specific configuration utility
 *
 */
public class SiteConfigUtil {
    private static final Logger logger = LoggerFactory.getLogger(SiteConfigUtil.class);

    // Appliance status checking interval
    private static int applianceRefreshInterval;

    // HBase health checking interval(ms)
    private static int hbaseCheckingInterval;

    // HADARS distributes the archiving PV
    private static boolean archivingPVDistributedByHadars;

    //Use AA live mode for operation status update
    private static boolean aaLiveMode;

    // HBase URL
    private static String hbaseURL;

    // DataWarehouse Host
    private static String zookeeperHost;
    // DataWarehouse port
    private static int zookeeperPort;

    // time slot for downloading PV data
    private static int timeSlotForDownload;

    // PV data download speed (bytes/second), include data retrieving overhead.
    private static long pvDataDownloadSpeed;

    // PV data download transfer length calculation factor.
    // PV data download transfer length = (PV data size) * (the number of PV) * (factor)
    private static double pvDataDownloadTransferLengthCalculationFactor;

    static {
        loadConfig();
    }

    private SiteConfigUtil() {

    }

    public static int getApplianceRefreshInterval() {
        return applianceRefreshInterval;
    }
    public static int getHbaseCheckingInterval() {
        return hbaseCheckingInterval;
    }
    public static boolean getArchivingPVDistributedByHadars() {
        return archivingPVDistributedByHadars;
    }
    public static boolean getAaLiveMode() {
        return aaLiveMode;
    }
    public static String getHbaseUrl() {
        return hbaseURL;
    }
    public static String getZookeeperHost() {
        return zookeeperHost;
    }
    public static int getZookeeperPort() {
        return zookeeperPort;
    }
    public static int getTimeSlotForDownload() {
        return timeSlotForDownload;
    }
    public static long getPvDataDownloadSpeed() {
        return pvDataDownloadSpeed;
    }
    public static double getPvDataDownloadTransferLengthCalculationFactor() {
        return pvDataDownloadTransferLengthCalculationFactor;
    }

    private static void loadConfig() {
        try (InputStream is = new FileInputStream(new File(ConfigUtil.getConfigFilesDir() +
                File.separator + "siteconfig.properties"))) {
            Properties props = new Properties();
            props.load(is);
            String applianceRefreshIntervalStr = props.getProperty("Appliance_Refresh_Interval", "3000");
            String hbaseCheckingIntervalStr = props.getProperty("HBase_Checking_Interval", "5000");
            String archivingPVDistributedByHadarsStr = props.getProperty("ArchivingPV_Distributed_by_Hadars", "false");
            String aaLiveModeStr = props.getProperty("AA_Live_Mode", "false");
            hbaseURL = props.getProperty("HBase_Url");
            zookeeperHost = props.getProperty("Zookeeper_Host", "192.168.0.132");
            String zookeeperPortStr = props.getProperty("Zookeeper_Port", "2181");
            String timeSlotForDownloadStr = props.getProperty("Time_Slot_Download", "1800");
            String speedStr = props.getProperty("PV_Data_Download_Speed", "100000000");
            String transferLengthCalFactorStr = props.getProperty("PV_Data_Download_Size_Calculation_Factor", "1");
            applianceRefreshInterval = Integer.parseInt(applianceRefreshIntervalStr);
            hbaseCheckingInterval = Integer.parseInt(hbaseCheckingIntervalStr);
            archivingPVDistributedByHadars = Boolean.valueOf(archivingPVDistributedByHadarsStr);
            aaLiveMode = Boolean.valueOf(aaLiveModeStr);
            zookeeperPort = Integer.parseInt(zookeeperPortStr);
            timeSlotForDownload = Integer.parseInt(timeSlotForDownloadStr);
            pvDataDownloadSpeed = Integer.parseInt(speedStr);
            pvDataDownloadTransferLengthCalculationFactor = Double.parseDouble(transferLengthCalFactorStr);
        } catch (FileNotFoundException e) {
            logger.error("File siteconfig.properties not existed");
        } catch (IOException e) {
            logger.error("IO Exception at open siteconfig.properties");
        }
    }

}