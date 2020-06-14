package com.hlsii.vo;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ScXin
 * @date 4/27/2020 11:10 PM
 */
public class TopGroup extends BaseVO {
    private static final long serialVersionUID = 1L;

    private String groupName;
    private List<MidGroup> midGroups = new ArrayList<>();

    public TopGroup(String groupName) {
        super();
        this.groupName = groupName;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public List<MidGroup> getMidGroups() {
        return midGroups;
    }

    public void setMidGroups(List<MidGroup> midGroups) {
        this.midGroups = midGroups;
    }

    public void addMidGroup(MidGroup midGroup) {
        this.midGroups.add(midGroup);
    }
}
