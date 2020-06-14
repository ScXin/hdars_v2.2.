package com.hlsii.service;

import com.alibaba.fastjson.JSONObject;
import com.hlsii.vo.HealthStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author ScXin
 * @date 4/27/2020 11:33 PM
 */
@Service
public class HealthService implements IHealthService {
    @Autowired
    private IArchiverSystemService archiverService;

    @Autowired
    private IHBaseHealthService hbaseService;

    @Override
    public HealthStatus getHBaseStatus() {
        HealthStatus status = new HealthStatus();
        int serviceCount = hbaseService.hbaseInService()? 1 : 0;
        status.setStatus(serviceCount, 1);
        return status;
    }

    @Override
    public HealthStatus getArchiverStatus() {
        return archiverService.getArchiverStatus();
    }

    @Override
    public JSONObject getHealthStatus() {
        JSONObject health = new JSONObject();
        health.put("hbase", getHBaseStatus());
        health.put("archiver", getArchiverStatus());
        return health;
    }
}

