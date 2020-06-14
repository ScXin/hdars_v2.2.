package com.hlsii.vo;

/**
 * @author ScXin
 * @date 4/27/2020 11:42 PM
 */

/**
 * PV defined in beam current config
 *
 */
public class BeamCurrentPV extends BaseVO {
    private static final long serialVersionUID = 1L;

    // The PV title displayed in the Operation Status lable
    private String pvTitle;

    // The PV name
    private String pvName;

    // The unit of the value
    private String unit;

    public BeamCurrentPV() {
        super();
    }

    /**
     * Axis title(format: pvTitle + (unit)
     * @return
     */
    public String getAxisTitle() {
        return pvTitle + "(" + unit + ")";
    }

    public String getPvTitle() {
        return pvTitle;
    }

    public String getPvName() {
        return pvName;
    }

    public String getUnit() {
        return unit;
    }

    public void setPvTitle(String pvTitle) {
        this.pvTitle = pvTitle;
    }

    public void setPvName(String pvName) {
        this.pvName = pvName;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}

