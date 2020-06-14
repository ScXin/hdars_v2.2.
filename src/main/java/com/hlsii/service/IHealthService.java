package com.hlsii.service;

import com.alibaba.fastjson.JSONObject;
import com.hlsii.vo.HealthStatus;

/**
 * Health Service interface
 *
 */
public interface IHealthService {
    // Get health status for HBase
    HealthStatus getHBaseStatus();

    // Get health status for Archiver
    HealthStatus getArchiverStatus();

    // Get health status for HBase and Archiver
    JSONObject getHealthStatus();
}
