package com.hlsii.entity;

//import org.apache.hadoop.hdfs.server.federation.store.records.Query;

import javax.persistence.*;
import java.util.Date;

@SuppressWarnings("serial")
@Entity
@Table(name = "query_prof")
public class QueryProfile extends DataEntity {
    private User user;
    private String profName;
    private Date updateTime;

    public QueryProfile() {
        super();
        this.updateTime = new Date();
    }

    public QueryProfile(String id, User user, String profName) {
        super(id);
        this.user = user;
        this.profName = profName;
        this.updateTime = new Date();
    }

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Column(name = "prof_name", nullable = false, length = 64)
    public String getProfName() {
        return profName;
    }

    public void setProfName(String profName) {
        this.profName = profName;
    }

    @Column(name = "update_time", nullable = false)
    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }


    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
