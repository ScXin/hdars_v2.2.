package com.hlsii.util;



import com.hlsii.commdef.Constants;

/**
 * Configuration utility
 *
 */
public class ConfigUtil {
    private ConfigUtil() {}

    public static String getConfigFilesDir() {
        return System.getenv(Constants.HDARS_CONFIG_DIR);
    }
}