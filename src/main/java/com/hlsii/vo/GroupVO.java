package com.hlsii.vo;

import com.hlsii.entity.ProfileGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ScXin
 * @date 4/28/2020 11:24 AM
 */

public class GroupVO extends BaseEntityVO {
    private static final long serialVersionUID = 1L;

    private String groupName;
    private boolean logarithm;
    private List<String> pvs = new ArrayList<>();

    public GroupVO() {
        super();
    }

    public GroupVO(ProfileGroup group) {
        super(group.getId());
        groupName = group.getGroupName();
        logarithm = group.getLogarithm();
        pvs.addAll(group.getPVList());
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public boolean getLogarithm() {
        return logarithm;
    }

    public void setLogarithm(boolean logarithm) {
        this.logarithm = logarithm;
    }

    public List<String> getPvs() {
        return pvs;
    }

    public void setPvs(List<String> pvs) {
        this.pvs = pvs;
    }

    public static List<GroupVO> fromGroups(List<ProfileGroup> groups) {
        List<GroupVO> groupVOs = new ArrayList<>();
        for(ProfileGroup group : groups) {
            groupVOs.add(new GroupVO(group));
        }
        return groupVOs;
    }
}

