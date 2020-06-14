package com.hlsii.commdef;

import java.util.Date;
import java.util.UUID;

/**
 * Download Task
 *
 */
public class DownloadTask {
    // Task id
    private String id;

    // Task State
    private DownloadState state;

    // Retrieve Parms
    private RetrieveParms parms;

    // Last active time
    private Date lastActiveTime;

    // Progress (0 ~ 100)
    private int progress;

    public DownloadTask(RetrieveParms parms) {
        id = UUID.randomUUID().toString().replaceAll("-", "");
        state = DownloadState.Created;
        lastActiveTime = new Date();
        progress = 0;
        this.parms = parms;
    }

    public String getId() {
        return id;
    }

    public DownloadState getState() {
        return state;
    }

    public void setState(DownloadState state) {
        this.state = state;
        this.lastActiveTime = new Date();
    }

    public RetrieveParms getParms() {
        return parms;
    }

    public Date getLastActiveTime() {
        return lastActiveTime;
    }

    public void setLastActiveTime(Date lastActiveTime) {
        this.lastActiveTime = lastActiveTime;
    }

    public int getProgress() {
        return progress;
    }

    /**
     * Update the download progress
     *
     * @param progress - progress value (0~100)
     * @return - true: the progress is updated.
     *           false: the download is already canceled/aborted,
     *           or the progress value is not in range.
     */
    public boolean updateProgress(int progress) {
        if (state != DownloadState.Downloading || progress < 0 ||
                progress > 100 || progress < this.progress) {
            return false;
        }
        this.progress = progress;
        return true;
    }
}
