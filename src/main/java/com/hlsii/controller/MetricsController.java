package com.hlsii.controller;

import com.alibaba.fastjson.JSONObject;
import com.hlsii.service.IHealthService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ScXin
 * @date 4/28/2020 1:20 PM
 */
@RestController
@RequestMapping(value = "/hdars")
public class MetricsController {

    @Autowired
    private IHealthService healthService;

    @ApiOperation("健康状态查询")
    @GetMapping(value = "/mgmt/healthstatus")
    public JSONObject healthstatus() {
        return healthService.getHealthStatus();
    }

}
