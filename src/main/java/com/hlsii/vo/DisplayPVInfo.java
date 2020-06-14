package com.hlsii.vo;

/**
 * @author ScXin
 * @date 4/27/2020 11:13 PM
 */
public class DisplayPVInfo  extends BaseVO {
    private static final long serialVersionUID = 1L;

    // Real PV name
    private String pvName;

    // Display label of the PV
    private String displayLable;

    public DisplayPVInfo(String pvName, String displayLable) {
        super();
        this.pvName = pvName;
        this.displayLable = displayLable;
    }

    public String getPvName() {
        return pvName;
    }

    public void setPvName(String pvName) {
        this.pvName = pvName;
    }

    public String getDisplayLable() {
        return displayLable;
    }

    public void setDisplayLable(String displayLable) {
        this.displayLable = displayLable;
    }
}

