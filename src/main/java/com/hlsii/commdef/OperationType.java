package com.hlsii.commdef;

public enum OperationType {
    LOGIN("Login"),
    LOGOUT("Logout"),
    CREATE_USER("Create New User"),
    MODIFY_USER("Change User Info"),
    VIEW_USER_LIST("View User List"),
    RESET_PWD("Reset User Password"),
    CHANGE_PWD("Change Password"),
    DELETE_USER("Delete User"),
    CREATE_WHITEIP("Create White IP"),
    MODIFY_WHITEIP("Change White IP"),
    DELETE_IP("Delete White IP"),
    VIEW_IP_LIST("View White IP List"),
    SAVE_PROFILE("Save Query Profile"),
    DELETE_PROFILE("Delete Query Profile"),
    QUERY_OPERATION_STATUS("Query Operation Status"),
    FREE_QUERY("Free PV Group Query"),
    QUERY_GROUP_DATA("Query Group PV Data"),
    ARCHIEVE_PV("Archieve PV"),
    PAUSE_PV("Pause PV"),
    RESUME_PV("Resume PV"),
    REMOVE_PV("Remove PV"),
    QUERY_PV("Query PV Detail"),
    QUERY_PV_DATA("Query PV Historical Data"),
    CHANGE_PV_PARMS("Change PV Archival Parameters"),
    CONSOLIDATE_PV_DATA("Consolidate PV Data"),
    STATISTICS_DATA("Query PV Statistics Data"),
    DOWNLOAD_RAW_DATA("Download PV Raw Data");

    private String operation;

    OperationType(final String operation) {
        this.operation = operation;
    }

    /**
     * Returns description instead of enum name
     */
    public static OperationType stateOf(String state) {
        for (OperationType stateEnum : values()) {
            if (stateEnum.getOperation().equals(state)) {
                return stateEnum;
            }
        }
        return null;
    }


    public String getOperation() {
        return this.operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }
}
