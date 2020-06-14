package com.hlsii.commdef;


import hadarshbaseplugin.commdef.PostProcessing;
import org.apache.log4j.Logger;

import java.sql.Timestamp;
import java.text.MessageFormat;
import java.util.List;

/**
 * PV data retrieve parameters
 *
 */

public class RetrieveParms {
    private static Logger logger = Logger.getLogger(RetrieveParms.class.getName());

    // PV name list (currently only support one PV in the list for the data retrieving.
    private List<String> pvs;
    // post processing supports none, firstSample, lastSample, mean, min, max.
    private PostProcessing postProcessIdentity;
    // Sample duration, 0 means the duration is determined by retrieval service.
    private int intervalSeconds = 0;
    // from time
    private Timestamp from;
    // to time
    private Timestamp to;
    // whether to fetch latest meta data from AA engine
    private boolean fetchLatestMetadata;
    // the data format
    private PVDataFormat pvDataFormat;

    public RetrieveParms(List<String> pvs, PostProcessing postProcessIdentity, int intervalSeconds, Timestamp from, Timestamp to,
                         boolean fetchLatestMetadata, PVDataFormat pvDataFormat) {
        super();
        this.pvs = pvs;
        this.postProcessIdentity = postProcessIdentity;
        this.intervalSeconds = intervalSeconds;
        this.from = from;
        this.to = to;
        this.fetchLatestMetadata = fetchLatestMetadata;
        this.pvDataFormat = pvDataFormat;
    }

    public List<String> getPvs() {
        return pvs;
    }
    public void setPvs(List<String> pvs) {
        this.pvs = pvs;
    }
    public PostProcessing getPostProcessIdentity() {
        return postProcessIdentity;
    }
    public void setPostProcessIdentity(PostProcessing postProcessIdentity) {
        this.postProcessIdentity = postProcessIdentity;
    }
    public int getIntervalSeconds() {
        return intervalSeconds;
    }
    public void setIntervalSeconds(int intervalSeconds) {
        this.intervalSeconds = intervalSeconds;
    }
    public Timestamp getFrom() {
        return from;
    }
    public void setFrom(Timestamp from) {
        this.from = from;
    }
    public Timestamp getTo() {
        return to;
    }
    public void setTo(Timestamp to) {
        this.to = to;
    }
    public boolean getFetchLatestMetadata() {
        return fetchLatestMetadata;
    }

    public void setFetchLatestMetadata(boolean fetchLatestMetadata) {
        this.fetchLatestMetadata = fetchLatestMetadata;
    }
    public PVDataFormat getPvDataFormat() {
        return pvDataFormat;
    }

    public RetrieveParms clone() {
        return new RetrieveParms(this.getPvs(),
                this.getPostProcessIdentity(),
                this.getIntervalSeconds(),
                this.getFrom(),
                this.getTo(),
                this.getFetchLatestMetadata(),
                this.getPvDataFormat());
    }

    /**
     * Check whether all parameters are valid.
     *
     * @param parm the {@link RetrieveParms}
     * @return true if all parameters are valid, false is any parameter is not valid.
     */
    public static boolean check(RetrieveParms parm) {
        if (parm == null) {
            logger.error("RetrieveParms is not valid: It is null!");
            return false;
        }

        if (parm.getPvs() == null) {
            logger.error("RetrieveParms is not valid: RetrieveParms.getPvs() is null!");
            return false;
        }

        if (parm.getPvs().isEmpty()) {
            logger.error("RetrieveParms is not valid: RetrieveParms.getPvs() is empty!");
            return false;
        }

        if (parm.getPostProcessIdentity() != PostProcessing.NONE && parm.getIntervalSeconds() == 0) {
            logger.error(MessageFormat.format("RetrieveParms is not valid: RetrieveParms.getPostProcessIdentity()" +
                    " is {0}, but parm.getIntervalSeconds() is 0!", parm.getPostProcessIdentity()));
            return false;
        }

        if (parm.getFrom() == null) {
            logger.error("RetrieveParms is not valid: RetrieveParms.getFrom() is null!");
            return false;
        }

        if (parm.getTo() == null) {
            logger.error("RetrieveParms is not valid: RetrieveParms.getTo() is null!");
            return false;
        }

        if (parm.getFrom().after(parm.getTo())) {
            logger.error(MessageFormat.format("RetrieveParms is not valid: RetrieveParms.getFrom() ({0}) is " +
                    "after RetrieveParms.getTo() (){1}!", parm.getFrom().toString(), parm.getTo().toString()));
            return false;
        }

        return true;
    }

}