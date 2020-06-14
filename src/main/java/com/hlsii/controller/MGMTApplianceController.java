package com.hlsii.controller;

import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author ScXin
 * @date 4/29/2020 10:11 PM
 */
@Controller
@RequestMapping(value = "/hdars")
public class MGMTApplianceController extends MGMTBaseController {
    /**
     * Get Achiever Appliance version info
     *
     * @param request
     * @param resp
     * @throws IOException
     */
    @GetMapping("/mgmt/getVersions")
    public void getVersions(HttpServletRequest request, HttpServletResponse resp) throws IOException {
        archiverSystemService.routeMgmtReq(request, resp);
    }

    @ApiOperation("得到Appliance的信息")
    @GetMapping("/mgmt/getApplianceInfo")
    public void getApplianceInfo(HttpServletRequest request, HttpServletResponse resp) throws IOException {
        archiverSystemService.routeMgmtReq(request, resp);
    }

    @ApiOperation("得到Appliance集群")
    @GetMapping("/mgmt/getAppliancesInCluster")
    public void getAppliancesInCluster(HttpServletRequest request, HttpServletResponse resp) throws IOException {
        archiverSystemService.routeMgmtReq(request, resp);
    }


    @GetMapping("/mgmt/getNamedFlag")
    public void getNamedFlag(HttpServletRequest request, HttpServletResponse resp) throws IOException {
        archiverSystemService.routeMgmtReq(request, resp);
    }

    @RequestMapping("/mgmt/setNamedFlag")
    public void setNamedFlag(HttpServletRequest request, HttpServletResponse resp) throws IOException {
        archiverSystemService.routeMgmtReq(request, resp);
    }
}