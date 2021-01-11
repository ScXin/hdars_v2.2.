package com.hlsii.service;

/**
 * @author ScXin
 * @date 5/3/2020 12:08 PM
 */

import com.hlsii.commdef.DownloadTask;
import com.hlsii.commdef.RetrieveParms;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


/**
 * PV data download service
 *
 */
@Service
public class DownloadService implements IDownloadService {
   private Logger logger = LoggerFactory.getLogger(getClass());
    private Map<String, DownloadTask> taskMap = new HashMap<>();

    // Download task audit interval (5 minutes)
    private static final long AUDIT_INTERVAL = 5 * 60 * 1000L;

    // Executor for the scheduled task
    ScheduledExecutorService scheduledService = Executors
            .newSingleThreadScheduledExecutor();
    @Override
    public DownloadTask createTask(RetrieveParms parms) {
        DownloadTask task = new DownloadTask(parms);
        taskMap.put(task.getId(), task);
        return task;
    }

    @Override
    public DownloadTask getTask(String id) {
        return taskMap.get(id);
    }

    @Override
    public void cleanTask(String id) {
        taskMap.remove(id);
    }

    /**
     * Start the task to audit all download task
     *
     */
    @PostConstruct
    public void startMonitorAppliance() {
        scheduledService.scheduleAtFixedRate(() -> auditTasks(), AUDIT_INTERVAL, AUDIT_INTERVAL,
                TimeUnit.MILLISECONDS);
    }

    private void auditTasks() {
     //   logger.info("Audit the download tasks.");
        Date now = new Date();
        for(String taskid : new ArrayList<String>(taskMap.keySet())) {
            DownloadTask task = taskMap.get(taskid);
            if (now.getTime() - task.getLastActiveTime().getTime() > AUDIT_INTERVAL) {
                logger.info("Clean download task {}.", taskid);
                taskMap.remove(taskid);
            }
        }
    }
}
