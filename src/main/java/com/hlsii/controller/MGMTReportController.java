package com.hlsii.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author ScXin
 * @date 4/29/2020 10:13 PM
 */
@Controller
@RequestMapping(value = "/hdars")
public class MGMTReportController extends MGMTBaseController {
    @RequestMapping("/mgmt/getNeverConnectedPVs")
    public void getNeverConnectedPVs(HttpServletRequest request, HttpServletResponse resp) throws IOException {
        archiverSystemService.routeMgmtReq(request, resp);
    }

    @RequestMapping("/mgmt/getNeverConnectedPVsForThisAppliance")
    public void getNeverConnectedPVsForThisAppliance(HttpServletRequest request, HttpServletResponse resp)
            throws IOException {
        archiverSystemService.routeMgmtReq(request, resp);
    }

    @RequestMapping("/mgmt/getMetaGets")
    public void getMetaGets(HttpServletRequest request, HttpServletResponse resp) throws IOException {
        archiverSystemService.routeMgmtReq(request, resp);
    }

    @RequestMapping("/mgmt/getCurrentlyDisconnectedPVs")
    public void getCurrentlyDisconnectedPVs(HttpServletRequest request, HttpServletResponse resp) throws IOException {
        archiverSystemService.routeMgmtReq(request, resp);
    }

    @RequestMapping("/mgmt/getEventRateReport")
    public void getEventRateReport(HttpServletRequest request, HttpServletResponse resp) throws IOException {
        archiverSystemService.routeMgmtReq(request, resp);
    }

    @RequestMapping("/mgmt/getStorageRateReport")
    public void getStorageRateReport(HttpServletRequest request, HttpServletResponse resp) throws IOException {
        archiverSystemService.routeMgmtReq(request, resp);
    }

    @RequestMapping("/mgmt/getRecentlyAddedPVs")
    public void getRecentlyAddedPVs(HttpServletRequest request, HttpServletResponse resp) throws IOException {
        archiverSystemService.routeMgmtReq(request, resp);
    }

    @RequestMapping("/mgmt/getRecentlyAddedPVsForThisInstance")
    public void getRecentlyAddedPVsForThisInstance(HttpServletRequest request, HttpServletResponse resp)
            throws IOException {
        archiverSystemService.routeMgmtReq(request, resp);
    }

    @RequestMapping("/mgmt/getRecentlyModifiedPVs")
    public void getRecentlyModifiedPVs(HttpServletRequest request, HttpServletResponse resp) throws IOException {
        archiverSystemService.routeMgmtReq(request, resp);
    }

    @RequestMapping("/mgmt/getRecentlyModifiedPVsForThisInstance")
    public void getRecentlyModifiedPVsForThisInstance(HttpServletRequest request, HttpServletResponse resp)
            throws IOException {
        archiverSystemService.routeMgmtReq(request, resp);
    }

    @RequestMapping("/mgmt/getStorageMetrics")
    public void getStorageMetrics(HttpServletRequest request, HttpServletResponse resp) throws IOException {
        archiverSystemService.getMetricsReport(request, resp);
    }

    @RequestMapping("/mgmt/getStorageMetricsForAppliance")
    public void getStorageMetricsForAppliance(HttpServletRequest request, HttpServletResponse resp) throws IOException {
        archiverSystemService.routeMgmtReq(request, resp);
    }

    @RequestMapping("/mgmt/getPVsByStorageConsumed")
    public void getPVsByStorageConsumed(HttpServletRequest request, HttpServletResponse resp) throws IOException {
        archiverSystemService.routeMgmtReq(request, resp);
    }

    @RequestMapping("/mgmt/getLostConnectionsReport")
    public void getLostConnectionsReport(HttpServletRequest request, HttpServletResponse resp) throws IOException {
        archiverSystemService.routeMgmtReq(request, resp);
    }

    @RequestMapping("/mgmt/getSilentPVsReport")
    public void getSilentPVsReport(HttpServletRequest request, HttpServletResponse resp) throws IOException {
        archiverSystemService.routeMgmtReq(request, resp);
    }

    @RequestMapping("/mgmt/getPVsForThisAppliance")
    public void getPVsForThisAppliance(HttpServletRequest request, HttpServletResponse resp) throws IOException {
        archiverSystemService.routeMgmtReq(request, resp);
    }

    @RequestMapping("/mgmt/getPVsByDroppedEventsTimestamp")
    public void getPVsByDroppedEventsTimestamp(HttpServletRequest request, HttpServletResponse resp)
            throws IOException {
        archiverSystemService.routeMgmtReq(request, resp);
    }

    @RequestMapping("/mgmt/getPVsByDroppedEventsBuffer")
    public void getPVsByDroppedEventsBuffer(HttpServletRequest request, HttpServletResponse resp) throws IOException {
        archiverSystemService.routeMgmtReq(request, resp);
    }

    @RequestMapping("/mgmt/getPVsByDroppedEventsTypeChange")
    public void getPVsByDroppedEventsTypeChange(HttpServletRequest request, HttpServletResponse resp)
            throws IOException {
        archiverSystemService.routeMgmtReq(request, resp);
    }

    @RequestMapping("/mgmt/getPausedPVsReport")
    public void getPausedPVsReport(HttpServletRequest request, HttpServletResponse resp) throws IOException {
        archiverSystemService.routeMgmtReq(request, resp);
    }

    @RequestMapping("/mgmt/getPausedPVsForThisAppliance")
    public void getPausedPVsForThisAppliance(HttpServletRequest request, HttpServletResponse resp) throws IOException {
        archiverSystemService.routeMgmtReq(request, resp);
    }

    @RequestMapping("/mgmt/getArchivedWaveforms")
    public void getArchivedWaveforms(HttpServletRequest request, HttpServletResponse resp) throws IOException {
        archiverSystemService.routeMgmtReq(request, resp);
    }

    @RequestMapping("/mgmt/getTimeSpanReport")
    public void getTimeSpanReport(HttpServletRequest request, HttpServletResponse resp) throws IOException {
        archiverSystemService.routeMgmtReq(request, resp);
    }
}
