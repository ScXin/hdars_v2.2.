package com.hlsii.vo;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author ScXin
 * @date 4/27/2020 11:12 PM
 */
public class SubGroup extends BaseVO {
    private static final long serialVersionUID = 1L;

    // group id
    private String id;

    // group name
    private String groupName;

    // unit of the group
    private String unit;

    // Chart Title of the group
    private String title;

    private List<DisplayPVInfo> pvList = new ArrayList<>();

    public SubGroup(String groupName) {
        this(UUID.randomUUID().toString(), groupName);
    }

    public SubGroup(String id, String groupName) {
        super();
        this.id = id;
        this.groupName = groupName;
    }

    public SubGroup(String groupName, String unit, String title) {
        this(UUID.randomUUID().toString(), groupName, unit, title);
    }

    public SubGroup(String groupId, String groupName, String unit, String title) {
        this(groupId, groupName);
        this.unit = unit;
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<DisplayPVInfo> getPvList() {
        return pvList;
    }

    public void setPvList(List<DisplayPVInfo> pvList) {
        this.pvList = pvList;
    }

    public void addPV(DisplayPVInfo pv) {
        pvList.add(pv);
    }

    public void addPV(String pvName, String displayLabel) {
        pvList.add(new DisplayPVInfo(pvName, displayLabel));
    }
}
