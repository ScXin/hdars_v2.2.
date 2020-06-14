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


package hadarshbaseplugin.commdef;

import org.epics.archiverappliance.config.ArchDBRTypes;

/**
 * To stores the PV information.
 */
public class PvInfo {
    /**
     * name of the PV
     */
    private String pvName;

    /**
     * The PV ID value of this PV.
     */
    private byte[] pvId = null;

    /**
     * Payload type of the PV samples
     */
    private ArchDBRTypes pvType = null;

    /**
     * The time in second to record when the PV is deleted.
     * 
     */
    private int deletedTime = 0;
    
    /**
     * the meta information of the PV.
     */
    private String metaOfPV; 



    /**
     * 
     * @return name of the PV
     */
    public String getPvName() {
        return pvName;
    }

    /**
     * set name of the PV
     * 
     * @param pvName
     *            - name of the PV
     */
    public void setPvName(String pvName) {
        this.pvName = pvName;
    }

    /**
     * 
     * @return The PV ID value of this PV.
     */
    public byte[] getPvId() {
        return pvId;
    }

    /**
     * Set the PV ID value of this PV.
     * 
     * @param pvId
     *            - PV ID value
     */
    public void setPvId(byte[] pvId) {
        this.pvId = pvId;
    }

    /**
     * 
     * @return Payload type of the PV samples
     */
    public ArchDBRTypes getPvType() {
        return pvType;
    }

    /**
     * Set the payload type of the PV samples
     * 
     * @param pvType
     *            - payload type of the PV samples
     */
    public void setPvType(ArchDBRTypes pvType) {
        this.pvType = pvType;
    }

    /**
     * get the time in second to record when the PV is deleted.
     * 
     * @return If the value is bigger than 0, means this PV should be deleted.
     */
    public int getDeletedTime() {
        return deletedTime;
    }

    /**
     * Set the time in second to record when the PV is deleted.
     * 
     * @param deletedTime
     */
    public void setDeletedTime(int deletedTime) {
        this.deletedTime = deletedTime;
    }
    
    /**
     * get the PV meta information.
     * @return the PV meta information.
     */
    public String getMetaOfPV() {
        return metaOfPV;
    }

    /**
     * set the PV meta information.
     * @param metaOfPV - the PV meta information.
     */
    public void setMetaOfPV(String metaOfPV) {
        this.metaOfPV = metaOfPV;
    }
}
