package com.hlsii.commdef;


import com.hlsii.util.IPUtil;
import org.apache.commons.lang3.StringUtils;

import java.net.InetAddress;
import java.net.UnknownHostException;


/**
 * Appliance definition
 *
 */
public class Appliance {
    // The appliance parameter name used in MGMT REST API
    public static final String APPLIANCE_PARM_NAME = "appliance";

    // The name of the appliance
    private String identity;
    private String clusterInetport;
    private String clusterHost;
    private String clusterIP;
    private String clusterPort;
    // MGMT URL for the appliance
    private String mgmtURL;
    // Engine URL for the appliance
    private String engineURL;
    // ETL URL for the appliance
    private String etlURL;
    // Retrieval URL for the appliance
    private String retrievalURL;
    // Data Retrieval URL for the appliance
    private String dataRetrievalURL;
    // Whether the appliance is working
    private boolean working;
    // archive PV count on this appliance
    private int pvCount;

    public Appliance() {}

    /**
     * Constructor with all fields
     *
     * @param identity
     * @param clusterInetport
     * @param mgmtURL
     * @param engineURL
     * @param etlURL
     * @param retrievalURL
     * @param dataRetrievalURL
     */
    public Appliance(String identity, String clusterInetport, String mgmtURL, String engineURL, String etlURL,
                     String retrievalURL, String dataRetrievalURL) {
        super();
        this.identity = identity;
        setClusterInfo(clusterInetport);
        this.mgmtURL = mgmtURL;
        this.engineURL = engineURL;
        this.etlURL = etlURL;
        this.retrievalURL = retrievalURL;
        this.dataRetrievalURL = dataRetrievalURL;
        this.working = false;
    }

    /**
     * Check if the cluster member matching this appliance
     *
     * @param clustMember - cluster member
     * @return - true when the member is exactly the appliance
     */
    public boolean matchingClusterMember(String clustMember) {
        if (StringUtils.isNotEmpty(clusterIP)) {
            return clustMember.equals(clusterIP + ":" + clusterPort);
        }
        return false;
    }

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public String getClusterInetport() {
        return clusterInetport;
    }

    public String getClusterHost() {
        return clusterHost;
    }

    public String getClusterIP() {
        return clusterIP;
    }

    public String getClusterPort() {
        return clusterPort;
    }

    public void setClusterInetport(String clusterInetport) {
        setClusterInfo(clusterInetport);
    }

    public String getMgmtURL() {
        return mgmtURL;
    }

    public void setMgmtURL(String mgmtURL) {
        this.mgmtURL = mgmtURL;
    }

    public String getEngineURL() {
        return engineURL;
    }

    public void setEngineURL(String engineURL) {
        this.engineURL = engineURL;
    }

    public String getEtlURL() {
        return etlURL;
    }

    public void setEtlURL(String etlURL) {
        this.etlURL = etlURL;
    }

    public String getRetrievalURL() {
        return retrievalURL;
    }

    public void setRetrievalURL(String retrievalURL) {
        this.retrievalURL = retrievalURL;
    }

    public String getDataRetrievalURL() {
        return dataRetrievalURL;
    }

    public void setDataRetrievalURL(String dataRetrievalURL) {
        this.dataRetrievalURL = dataRetrievalURL;
    }

    public boolean isWorking() {
        return working;
    }

    public void setWorking(boolean working) {
        this.working = working;
    }

    public int getPvCount() {
        return pvCount;
    }

    public void setPvCount(int pvCount) {
        this.pvCount = pvCount;
    }

    private void setClusterInfo(String clusterInetport) {
        this.clusterInetport = clusterInetport;
        int idx = clusterInetport.lastIndexOf(':');
        this.clusterPort = clusterInetport.substring(idx + 1);
        String host = clusterInetport.substring(0, idx);
        idx = host.indexOf(':');
        if (idx >= 0) {
            host = host.substring(idx + 1);
        }
        if (IPUtil.isIPAddresss(host)) {
            clusterIP = host;
            clusterHost = "";
        }
        else {
            clusterHost = host;
            try {
                clusterIP = InetAddress.getByName(host).getHostAddress();
            } catch (UnknownHostException e) {
                clusterIP = "";
            }
        }
    }
}