package com.hlsii.service;


import com.hlsii.commdef.DownloadTask;
import com.hlsii.commdef.RetrieveParms;

/**
 * Download task service interface
 *
 */
public interface IDownloadService {
    /**
     * Create a download task based on the retrieve parms
     *
     * @param parms - retrieve parameters
     * @return - Download Task
     */
    DownloadTask createTask(RetrieveParms parms);

    /**
     * Get the download task by task id
     *
     * @param id - task id
     * @return - Download Task, null if the task is not existed or is clean.
     */
    DownloadTask getTask(String id);

    /**
     * Clean the task
     *
     * @param id - task id
     */
    void cleanTask(String id);
}
