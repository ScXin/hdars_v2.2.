package com.hlsii.entity;

import com.hlsii.commdef.OperationType;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import java.util.Date;

@SuppressWarnings("serial")
@Entity
@Table(name = "operation_log")
public class OperationLog extends DataEntity {

    public static final String USER_ALIAS = "user";
    private User user;
    private String remoteIp;
    private OperationType opType;
    private Date operationTime;
    private String content;
    public OperationLog() {
        super();
    }

    public static String getUserAlias() {
        return USER_ALIAS;
    }

//    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Column(name = "remote_ip", length = 32)
    public String getRemoteIp() {
        return remoteIp;
    }

    public void setRemoteIp(String remoteIp) {
        this.remoteIp = remoteIp;
    }

    @Column(name = "op_type")
//    @Enumerated(EnumType.STRING)
    public OperationType getOpType() {
        return opType;
    }

    public void setOpType(OperationType opType) {
        this.opType = opType;
    }

    @Column(name = "op_time")
    public Date getOperationTime() {
        return operationTime;
    }

    public void setOperationTime(Date operationTime) {
        this.operationTime = operationTime;
    }

    @Column(name="content")
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
