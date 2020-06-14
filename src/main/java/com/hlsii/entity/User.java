package com.hlsii.entity;

import java.io.Serializable;
import java.time.DateTimeException;
import java.util.Date;
import java.util.List;

//
//import javax.persistence.*;
//
///**
// * CREATE TABLE `user` (
// * `id` VARCHAR(64) NOT NULL,
// * `role_id` VARCHAR(64) NOT NULL,
// * `login_name` VARCHAR(20) NOT NULL,
// * `user_name` VARCHAR(64) NOT NULL,
// * `password` VARCHAR(32) NOT NULL,
// * `organization` VARCHAR(100),
// * `department` VARCHAR(50),
// * `telephone` VARCHAR(20),
// * `email` VARCHAR(100),
// * `del_flag` CHAR(1) NULL DEFAULT NULL,
// * PRIMARY KEY (`id`),
// * FOREIGN KEY(`role_id`) references role(`id`),
// * INDEX `login_name` (`login_name`),
// * INDEX `del_flag` (`del_flag`)
// * );
// */
//@Entity
//@Table(name = "user")
//public class User extends DataEntity {
//
//    private String loginName;
//    private String userName;
//    private String password;
//
//    private String organization;
//    private String department;
//    private String telephone;
//    private String email;
//    private Role userRole;
//
//
//    public User(){
//    super();
//    }
//    public User(String loginName, String userName, String password, String organization, String department, String telephone, String email, Role userRole) {
//        super();
//        this.loginName = loginName;
//        this.userName = userName;
//        this.password = password;
//        this.organization = organization;
//        this.department = department;
//        this.telephone = telephone;
//        this.email = email;
//        this.userRole = userRole;
//    }
//
//    @Column(name = "login_name", nullable = false, length = 20)
//    public String getLoginName() {
//        return loginName;
//    }
//
//    public void setLoginName(String loginName) {
//        this.loginName = loginName;
//    }
//
//    @Column(name = "user_name", nullable = false, length = 64)
//    public String getUserName() {
//        return userName;
//    }
//
//    public void setUserName(String userName) {
//        this.userName = userName;
//    }
//
//    @Column(name = "password", nullable = false, length = 32)
//    public String getPassword() {
//        return password;
//    }
//
//    public void setPassword(String password) {
//        this.password = password;
//    }
//
//    @Column(name = "organization", length = 100)
//    public String getOrganization() {
//        return organization;
//    }
//
//    public void setOrganization(String organization) {
//        this.organization = organization;
//    }
//
//    @Column(name = "department", length = 50)
//    public String getDepartment() {
//        return department;
//    }
//
//    public void setDepartment(String department) {
//        this.department = department;
//    }
//
//    @Column(name = "telephone", length = 20)
//    public String getTelephone() {
//        return telephone;
//    }
//
//    public void setTelephone(String telephone) {
//        this.telephone = telephone;
//    }
//
//    @Column(name="email",length=100)
//    public String getEmail() {
//        return email;
//    }
//
//    public void setEmail(String email) {
//        this.email = email;
//    }
//
//    @ManyToOne(cascade = CascadeType.REFRESH,fetch = FetchType.EAGER)
//    @JoinColumn(name = "role_id")
//    public Role getUserRole() {
//        return userRole;
//    }
//
//    public void setUserRole(Role userRole) {
//        this.userRole = userRole;
//    }
//    @Override
//    public boolean equals(Object obj) {
//        return super.equals(obj);
//    }
//
//    @Override
//    public int hashCode() {
//        return super.hashCode();
//    }
//
//    @Override
//    public String toString() {
//        return "User{" +
//                "loginName='" + loginName + '\'' +
//                ", userName='" + userName + '\'' +
//                ", password='" + password + '\'' +
//                ", organization='" + organization + '\'' +
//                ", department='" + department + '\'' +
//                ", telephone='" + telephone + '\'' +
//                ", email='" + email + '\'' +
//                ", userRole=" + userRole +
//                '}';
//    }
//
//}
//
public class User implements Serializable {


    private int id;

    private String username;

    private String password;

    private Date createTime;

    private String salt;


    /**
     * 角色集合
     */
    private List<Role> roleList;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Role> getRoleList() {
        return roleList;
    }

    public void setRoleList(List<Role> roleList) {
        this.roleList = roleList;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

}