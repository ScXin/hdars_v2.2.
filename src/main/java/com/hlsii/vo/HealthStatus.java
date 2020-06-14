package com.hlsii.vo;


/**
 * Health status for HBase, Archiver.
 *
 */
public class HealthStatus extends BaseVO {
    private static final long serialVersionUID = 1L;

    public enum Status {
        GREEN,     // All are in working
        YELLOW,    // At least one of all in working
        RED        // None in working
    }

    private Status status;
    private int totalCount;
    private int workingCount;

    public HealthStatus() {
        super();
    }

    public HealthStatus(Status status, int totalCount, int workingCount) {
        super();
        this.status = status;
        this.totalCount = totalCount;
        this.workingCount = workingCount;
    }

    public Status getStatus() {
        return status;
    }

    public String getStatusText() {
        return status.toString().toLowerCase();
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getWorkingCount() {
        return workingCount;
    }

    public void setWorkingCount(int workingCount) {
        this.workingCount = workingCount;
    }

    public void setStatus(int workingCount, int totalCount) {
        this.totalCount = totalCount;
        this.workingCount = workingCount;
        if (workingCount == 0) {
            status = Status.RED;
        }
        else if (workingCount == totalCount) {
            status = Status.GREEN;
        }
        else {
            status = Status.YELLOW;
        }
    }
}
