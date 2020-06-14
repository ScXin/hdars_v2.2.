package com.hlsii.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author ScXin
 * @date 4/29/2020 10:12 PM
 */
@Controller
@RequestMapping(value = "/hdars")
public class MGMTOtherController extends MGMTBaseController {
    @RequestMapping("/mgmt/getPolicyText")
    public void getNeverConnectedPVs(HttpServletRequest request, HttpServletResponse resp) throws IOException {
        archiverSystemService.routeMgmtReq(request, resp);
    }

    @RequestMapping("/mgmt/exportConfig")
    public void exportConfig(HttpServletRequest request, HttpServletResponse resp) throws IOException {
        archiverSystemService.routeMgmtReq(request, resp);
    }

    @RequestMapping("/mgmt/exportConfigForAppliance")
    public void exportConfigForAppliance(HttpServletRequest request, HttpServletResponse resp) throws IOException {
        archiverSystemService.routeMgmtReq(request, resp);
    }

    @RequestMapping("/mgmt/getInstanceMetrics")
    public void getInstanceMetrics(HttpServletRequest request, HttpServletResponse resp) throws IOException {
        archiverSystemService.getMetricsReport(request, resp);
    }

    @RequestMapping("/mgmt/getInstanceMetricsForAppliance")
    public void getInstanceMetricsForAppliance(HttpServletRequest request, HttpServletResponse resp)
            throws IOException {
        archiverSystemService.routeMgmtReq(request, resp);
    }

    @RequestMapping("/mgmt/getApplianceMetrics")
    public void getApplianceMetrics(HttpServletRequest request, HttpServletResponse resp) throws IOException {
        archiverSystemService.getMetricsReport(request, resp);
    }

    @RequestMapping("/mgmt/getApplianceMetricsForAppliance")
    public void getApplianceMetricsForAppliance(HttpServletRequest request, HttpServletResponse resp)
            throws IOException {
        archiverSystemService.routeMgmtReq(request, resp);
    }

    @RequestMapping("/mgmt/getExternalArchiverServers")
    public void getExternalArchiverServers(HttpServletRequest request, HttpServletResponse resp) throws IOException {
        archiverSystemService.routeMgmtReq(request, resp);
    }

    @RequestMapping("/mgmt/addExternalArchiverServer")
    public void addExternalArchiverServer(HttpServletRequest request, HttpServletResponse resp) throws IOException {
        archiverSystemService.routeMgmtReq(request, resp);
    }

    @RequestMapping("/mgmt/addExternalArchiverServerArchives")
    public void addExternalArchiverServerArchives(HttpServletRequest request, HttpServletResponse resp)
            throws IOException {
        archiverSystemService.routeMgmtReq(request, resp);
    }

    @RequestMapping("/mgmt/removeExternalArchiverServer")
    public void removeExternalArchiverServer(HttpServletRequest request, HttpServletResponse resp) throws IOException {
        archiverSystemService.routeMgmtReq(request, resp);
    }

    @RequestMapping("/mgmt/test/compareWithChannelArchiver")
    public void compareWithChannelArchiver(HttpServletRequest request, HttpServletResponse resp) throws IOException {
        archiverSystemService.routeMgmtReq(request, resp);
    }

    @RequestMapping("/mgmt/getAggregatedApplianceInfo")
    public void getAggregatedApplianceInfo(HttpServletRequest request, HttpServletResponse resp) throws IOException {
        archiverSystemService.routeMgmtReq(request, resp);
    }

    @RequestMapping("/mgmt/importDataFromPlugin")
    public void importDataFromPlugin(HttpServletRequest request, HttpServletResponse resp) throws IOException {
        archiverSystemService.routeMgmtReq(request, resp);
    }

    @RequestMapping("/mgmt/getPolicyList")
    public void getPolicyList(HttpServletRequest request, HttpServletResponse resp) throws IOException {
        archiverSystemService.routeMgmtReq(request, resp);
    }

    @RequestMapping("/mgmt/getApplianceProperties")
    public void getApplianceProperties(HttpServletRequest request, HttpServletResponse resp) throws IOException {
        archiverSystemService.routeMgmtReq(request, resp);
    }

    @RequestMapping("/mgmt/webAppReady")
    public void webAppReady(HttpServletRequest request, HttpServletResponse resp) throws IOException {
        archiverSystemService.routeMgmtReq(request, resp);
    }

    @RequestMapping("/mgmt/getProcessMetrics")
    public void getProcessMetrics(HttpServletRequest request, HttpServletResponse resp) throws IOException {
        archiverSystemService.routeMgmtReq(request, resp);
    }

    @RequestMapping("/mgmt/getProcessMetricsDataForAppliance")
    public void getProcessMetricsDataForAppliance(HttpServletRequest request, HttpServletResponse resp)
            throws IOException {
        archiverSystemService.routeMgmtReq(request, resp);
    }

    @RequestMapping("/mgmt/refreshPVDataFromChannelArchivers")
    public void refreshPVDataFromChannelArchivers(HttpServletRequest request, HttpServletResponse resp)
            throws IOException {
        archiverSystemService.routeMgmtReq(request, resp);
    }

    @RequestMapping("/mgmt/getMatchingPVsForThisAppliance")
    public void getMatchingPVsForThisAppliance(HttpServletRequest request, HttpServletResponse resp)
            throws IOException {
        archiverSystemService.routeMgmtReq(request, resp);
    }

    @RequestMapping("/mgmt/getCreationReportForAppliance")
    public void getCreationReportForAppliance(HttpServletRequest request, HttpServletResponse resp) throws IOException {
        archiverSystemService.routeMgmtReq(request, resp);
    }
}
