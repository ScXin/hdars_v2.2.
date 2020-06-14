package com.hlsii.vo;

/**
 * @author ScXin
 * @date 5/22/2020 3:13 PM
 */
public class RoleType {
    private String roleId;
    private String roleName;

    public RoleType() {

    }

    public RoleType(String roleId, String roleName) {
        this.roleId = roleId;
        this.roleName = roleName;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }


}
