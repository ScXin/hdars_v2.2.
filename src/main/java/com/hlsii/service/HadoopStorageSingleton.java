package com.hlsii.service;

import hadarshbaseplugin.HadoopStorageHBaseImpl;
import hadarshbaseplugin.api.IHadoopStorage;
import com.hlsii.commdef.Constants;
import com.hlsii.util.ConfigUtil;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;

/**
 * Implement singleton for HadoopStorage interface implementation.
 */
public class HadoopStorageSingleton {
    private static Logger logger = Logger.getLogger(HadoopStorageSingleton.class.getName());

    private static volatile IHadoopStorage hadoopStorage = null;

    public static IHadoopStorage getHadoopStorage() {
        return initialize();
    }

    public static IHadoopStorage initialize() {
        synchronized(HadoopStorageSingleton.class) {
            if (hadoopStorage == null) {
                hadoopStorage = new HadoopStorageHBaseImpl();
            }
            String hbaseSettingFile = ConfigUtil.getConfigFilesDir() + File.separator + "hbaseSetting.json";
            logger.info(MessageFormat.format("Try to get the value of the environment variable {0}",
                    Constants.HADARS_CONFIG_DIR));
            logger.info(MessageFormat.format("Initialize HadoopStorageHBaseImpl with the config file {0}",
                    hbaseSettingFile));
            try {
                if (!hadoopStorage.initialize(hbaseSettingFile)) {
                    logger.error(MessageFormat.format(
                            "Error: Initializing HadoopStorageHBaseImpl with the config file {0}.",
                            hbaseSettingFile));
                    hadoopStorage = null;
                }
            } catch (IOException ex) {
                logger.info("Exception on initializing the " + hadoopStorage.getName());
                hadoopStorage = null;
            }
        }
        return hadoopStorage;
    }
}
