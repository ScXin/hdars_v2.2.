package com.hlsii.entity;

//import com.sun.javafx.beans.IDProperty;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;

//import javax.print.attribute.standard.DateTimeAtCompleted;
//import javax.xml.crypto.Data;

@Entity
public abstract class DataEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    public static final String DEFAULT_ALIAS = "0";
    private String id;
    private String delFlag;

    public DataEntity() {
        super();
    }

    public DataEntity(String id) {
        super();
        this.id = id;
    }

    @Id
    @Column(name = "id", nullable = false, unique = true, length = 64)
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Column(name = "del_flag", length = 1)
    public String getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if(this==obj){
            return true;
        }
        if(!getClass().equals(obj.getClass())){
            return false;
        }
        DataEntity that=(DataEntity) obj;
        return null==this.getId()?false:this.getId().equals(that.id);
    }
    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }

}
