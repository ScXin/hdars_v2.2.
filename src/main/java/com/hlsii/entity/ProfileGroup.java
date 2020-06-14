package com.hlsii.entity;

//import org.apache.commons.lang3.StringUtils;
import org.springframework.util.StringUtils;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SuppressWarnings("serial")
@Entity
@Table(name = "prof_group")
public class ProfileGroup extends DataEntity {

    private QueryProfile profile;

    private String groupName;

    private boolean logarithm;

    private String pvs;

    public ProfileGroup() {
        super();
    }

    public ProfileGroup(String id, QueryProfile profile, String groupName, boolean logarithm, String pvs) {
        super(id);
        this.profile = profile;
        this.groupName = groupName;
        this.logarithm = logarithm;
        this.pvs = pvs;
    }

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    @JoinColumn(name = "prof_id")
    public QueryProfile getProfile() {
        return profile;
    }

    public void setProfile(QueryProfile profile) {
        this.profile = profile;
    }

    @Column(name = "group_name", nullable = false, length = 64)
    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    @Column(name = "logarithm")
    public boolean getLogarithm() {
        return logarithm;
    }

    public void setLogarithm(boolean logarithm) {
        this.logarithm = logarithm;
    }

    @Column(name = "pvs", nullable = false, length = 500)
    public String getPvs() {
        return pvs;
    }

    public void setPvs(String pvs) {
        this.pvs = pvs;
    }

    public List<String> getPVList() {
        if (StringUtils.isEmpty(pvs)) {
            return new ArrayList<>();
        }
        return Arrays.asList(pvs.split(","));
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public int hashCode(){
        return super.hashCode();
    }
}
