package com.hlsii.vo;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ScXin
 * @date 4/27/2020 11:10 PM
 */
public class MidGroup extends BaseVO {
    private static final long serialVersionUID = 1L;

    private String groupName;
    private List<SubGroup> subGroups = new ArrayList<>();

    public MidGroup(String groupName) {
        super();
        this.groupName = groupName;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public List<SubGroup> getSubGroups() {
        return subGroups;
    }

    public void setSubGroups(List<SubGroup> subGroups) {
        this.subGroups = subGroups;
    }

    public void addSubGroup(SubGroup subGroup) {
        this.subGroups.add(subGroup);
    }
}
