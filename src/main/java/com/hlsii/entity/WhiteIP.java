package com.hlsii.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@SuppressWarnings("serial")
@Entity
@Table(name = "white_ip")
public class WhiteIP extends DataEntity {
    private String ipAddr;

    public WhiteIP() {
        super();
    }

    public WhiteIP(String id, String ipAddr) {
        super(id);
        this.ipAddr = ipAddr;
    }

    @Column(name = "ip_addr", nullable = false, length = 100)
    public String getIpAddr() {
        return ipAddr;
    }

    public void setIpAddr(String ipAddr) {
        this.ipAddr = ipAddr;
    }


    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public String toString() {
        return "WhiteIP{" +
                "ipAddr='" + ipAddr + '\'' +
                '}';
    }
}
