package com.hlsii.entity;
//
//import javax.persistence.Column;
//import javax.persistence.Entity;
//import javax.persistence.Table;
//
//@SuppressWarnings("serial")
//@Entity
//@Table(name="role")
//public class Role extends DataEntity {
//
//    private String rolename;
//    private String roleDescription;
//
//    public Role(){
//        super();
//    }
//
//    public Role(String id, String rolename, String roleDescription) {
//        super(id);
//        this.rolename = rolename;
//        this.roleDescription = roleDescription;
//    }
//
//    @Column(name="role_name",nullable = false,unique = true)
//    public String getRoleName() {
//        return rolename;
//    }
//
//    public void setRolename(String rolename) {
//        this.rolename = rolename;
//    }
//
//    @Column(name="role_description")
//    public String getRoleDescription() {
//        return roleDescription;
//    }
//
//    public void setRoleDescription(String roleDescription) {
//        this.roleDescription = roleDescription;
//    }
//    @Override
//    public boolean equals(Object obj){
//        return super.equals(obj);
//    }
//
//    @Override
//    public int hashCode(){
//        return super.hashCode();
//    }
//}


import java.io.Serializable;
import java.nio.channels.SeekableByteChannel;
import java.util.List;

public class Role implements Serializable {


    private int id;

    private String name;

    private String description;

    private List<Permission> permissionList;

    public List<Permission> getPermissionList() {
        return permissionList;
    }

    public void setPermissionList(List<Permission> permissionList) {
        this.permissionList = permissionList;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }



}