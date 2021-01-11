package com.hlsii.service;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import hadarshbaseplugin.commdef.PostProcessing;
import com.hlsii.commdef.Appliance;
import com.hlsii.util.ApplianceParser;
import com.hlsii.util.HttpConnector;
import com.hlsii.util.NetworkUtil;
import com.hlsii.util.SiteConfigUtil;
import com.hlsii.vo.HealthStatus;
import com.hlsii.vo.RetrieveData;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Archiver System
 *
 */
@Service
public class ArchiverSystemService implements IArchiverSystemService {
    private static final String ID_TAG = "instance";
    private static final String JSON_TYPE = "application/json";

    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private IAARetrieveService retrieveService;

    // The root path for HADARS Management MGMT API
    private static final String MGMT_PATH = "/mgmt/";

    // Executor for the scheduled task
    ScheduledExecutorService scheduledService = Executors
            .newSingleThreadScheduledExecutor();

    // The map for configured appliances by appliance id
    private Map<String, Appliance> applianceMap = new HashMap<>();

    // The map for cluster member (ip:port) to appliance
    private Map<String, Appliance> memberMap = new HashMap<>();

    // Working appliance list
    private List<String> workingAppliances = new ArrayList<>();

    public ArchiverSystemService() {
        List<Appliance> appls = ApplianceParser.getAppliances();
        for (Appliance appl : appls) {
            applianceMap.put(appl.getIdentity(), appl);
        }
    }

    /**
     * Get all configured appliance list
     *
     * @return
     */
    @Override
    public List<Appliance> getAllAppliances() {
        return new ArrayList<>(applianceMap.values());
    }

    /**
     * Get a random working appliance
     *
     */
    @Override
    public Appliance getRandomWorkingAppliance() {
        if (workingAppliances.isEmpty()) {
            // No working appliance, return null.
            return null;
        }

        int applIdx = Math.abs(new Random().nextInt()) % workingAppliances.size();
        return applianceMap.get(workingAppliances.get(applIdx));
    }

    /**
     * Get the specified appliance by appliance id
     *
     */
    @Override
    public Appliance getAppliance(String applianceId) {
        return applianceMap.get(applianceId);
    }

    /**
     * Route the restful API request from WebUI to the specified AA MGMT host.
     *
     * If there is error at routing the request, the error status code is set in the response, otherwise the return
     * content from the routing host is set in the body of the response.
     *
     * @param req
     *            - HttpServletRequest from WebUI
     * @param resp
     *            - HttpServletResponse back to remote
     * @throws IOException
     *             - IO exception at error
     */
    @Override
    public void routeMgmtReq(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Map<String, List<String>> parms = NetworkUtil.getRequestParms(req);
        try {
            String respBody = sendMgmtReq(NetworkUtil.getRequireURI(req, MGMT_PATH), parms, req.getMethod());
            resp.setContentType(JSON_TYPE);
            resp.getWriter().write(respBody);
        } catch (IOException e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
//
//    @Override
//    public String routeMgmtReq(String urlParam) throws IOException {
//        return null;
//    }

//    @Override
//    public String routeMgmtReq(String urlParam){
//        Map<String,List<String>>params=NetworkUtil.getRequestParms(urlParam)
//
//
//
//    }

    /**
     * Send the Mgmt request to remote MGMT
     *
     * @param requestUri
     *            - request MGMT uri not includes the host name
     * @param parms
     *            - request parameters
     * @param method
     *            - HTTP method
     * @return - The response text from the response body
     * @throws IOException
     *             - IO exception at error
     */
    @Override
    public String sendMgmtReq(String requestUri, Map<String, List<String>> parms, String method)
            throws IOException {
        // Request parameters contain the target appliance id, route to that appliance directly.
        Appliance targetAppliance = null;
        if (parms.containsKey(Appliance.APPLIANCE_PARM_NAME)) {
            targetAppliance = getAppliance(parms.get(Appliance.APPLIANCE_PARM_NAME).get(0));
        }

        // No appliance is specified, or it is not working now, select a working instead.
        if (targetAppliance == null) {
            targetAppliance = getRandomWorkingAppliance();
        }

        if (targetAppliance == null) {
            throw new IOException("No appliance in working state.");
        }

        String requestUrl = targetAppliance.getMgmtURL() + "/" + requestUri;
        return sendRequestToRemote(requestUrl, parms, method);
    }

    /**
     * Send the request URL to remote host (host can be MGMT or retrieval)
     *
     * @param requestUrl
     *            - request url includes the host name, but not include parameter.
     * @param parms
     *            - request parameters
     * @param method
     *            - HTTP method
     * @return - The response text from the response body
     * @throws IOException
     *             - IO exception at error
     */
    @Override
    public String sendRequestToRemote(String requestUrl, Map<String, List<String>> parms, String method)
            throws IOException {
        return HttpConnector.sendRequestToRemote(requestUrl, parms, method);
    }

    @Override
    public List<RetrieveData> retrievePVData(String pv, Timestamp from, Timestamp to, PostProcessing pp, int duration)
            throws IOException {
        Appliance targetAppliance = getRandomWorkingAppliance();
        if (targetAppliance == null) {
            throw new IOException("No appliance in working state.");
        }
        String requestUrl = targetAppliance.getDataRetrievalURL() + "/data/getData.qw";
        Map<String, List<String>> parms = new HashMap<>();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        df.setTimeZone(TimeZone.getTimeZone("UTC"));

        String queryPV = pv;
        if (pp != PostProcessing.NONE) {
            String ppName = pp.toString();
            if (duration > 0) {
                ppName = ppName + "_" + duration;
            }
            queryPV = ppName + "(" + pv + ")";
        }
        parms.put("pv", Arrays.asList(queryPV));
        parms.put("from", Arrays.asList(df.format(from)));
        parms.put("to", Arrays.asList(df.format(to)));
        String respStr = sendRequestToRemote(requestUrl, parms, "GET");
        JSONArray dataSetObj = JSONObject.parseArray(respStr);
        List<RetrieveData> result = new ArrayList<>();
        for(int i = 0; i < dataSetObj.size(); i++) {
            JSONObject dataObj = dataSetObj.getJSONObject(i);
            RetrieveData retData = new RetrieveData(pv, dataObj.getJSONObject("meta"),
                    dataObj.getJSONArray("data"));
            result.add(retData);
        }
        return result;
    }

    @Override
    public void getMetricsReport(HttpServletRequest request, HttpServletResponse resp) throws IOException {
        JSONArray report = new JSONArray();
        resp.setContentType(JSON_TYPE);
        Map<String, List<String>> parms = NetworkUtil.getRequestParms(request);
        try {
            String respBody = sendMgmtReq(NetworkUtil.getRequireURI(request, MGMT_PATH), parms, request.getMethod());
            report = JSONObject.parseArray(respBody);
        } catch (IOException e) {
            logger.info(e.getMessage());
        }
        addNonWorkingAppliance(report);
        resp.getWriter().write(report.toJSONString());
    }

    @Override
    public String getMatchingPVs(int limit, String pv) throws IOException {
        Appliance targetAppliance = getRandomWorkingAppliance();
        if (targetAppliance == null) {
            throw new IOException("No appliance in working status, please try again later.");
        }
        String requestUrl = targetAppliance.getRetrievalURL() + "/getMatchingPVs";
        Map<String, List<String>> parms = new HashMap<>();
        parms.put("pv", Arrays.asList(pv));
        parms.put("limit", Arrays.asList("" + limit));
        return sendRequestToRemote(requestUrl, parms, "GET");
    }

    @Override
    public void archivePV(String[] pvNames, String sampleMethod, Float samplePeriod, HttpServletResponse resp)
            throws IOException {
        resp.setContentType(JSON_TYPE);
        List<Appliance> appls = new ArrayList<>();
        if (workingAppliances.isEmpty() || pvNames.length == 0) {
            resp.getWriter().write("[]");
            return;
        }
        // Get working appliance and the PV count on it
        for(String applId : workingAppliances) {
            Appliance appl = applianceMap.get(applId);
            appl.setPvCount(getPVCount(appl));
            appls.add(appl);
        }

        // Calculate the distributed PV result for each appliance
        int[] distributedPVs = distributePV(pvNames.length, appls);

        // Distribute the PVs to each appliance
        int startIdx = 0;
        JSONArray result = new JSONArray();
        for(int i = 0; i < appls.size(); i++) {
            int count = distributedPVs[i];
            if (count > 0) {
                JSONArray result1 = archivePVonAppliance(appls.get(i), Arrays.copyOfRange(pvNames, startIdx,
                        startIdx + count), sampleMethod, samplePeriod);
                if (!result1.isEmpty()) {
                    result.addAll(result1);
                }
                startIdx += count;
            }
        }
        resp.getWriter().write(result.toJSONString());
    }

    /**
     * Calculate the distributed PV count for each appliance
     *
     * @param distributePVCount - PV count for distribution
     * @param appls - working appliance list
     * @return - pv count needs to be distributed for each appliance
     */
    public static int[] distributePV(int distributePVCount, List<Appliance> appls) {
        int[] result = new int[appls.size()];
        // Calculate the average count after archiving
        int totalCount = appls.stream().mapToInt((Appliance s) -> s.getPvCount()).sum();
        totalCount += distributePVCount;
        final int mean = totalCount / appls.size();

        // Only distribute the PV to those appliance that pv count is less than the average
        List<Appliance> archivingAppls = appls.stream()
                .filter((Appliance s) -> s.getPvCount() < mean)
                .collect(Collectors.toList());

        // Re-calculate the average
        int lastMean = mean;
        if (archivingAppls.size() != appls.size()) {
            totalCount = archivingAppls.stream().mapToInt((Appliance s) -> s.getPvCount()).sum();
            totalCount += distributePVCount;
            lastMean = totalCount / archivingAppls.size();
        }

        // Calculate the PV count distributed to each appliance
        int pvLeft = distributePVCount;
        int count = 0;
        int lastDistributedIdx = -1;
        for(int i = 0; i < appls.size(); i++) {
            if (appls.get(i).getPvCount() > lastMean) {
                // The PV count on that appliance already exceeds the average, no distribution.
                count = 0;
            }
            else {
                count = Math.min(lastMean - appls.get(i).getPvCount(), pvLeft);
                lastDistributedIdx = i;
            }
            pvLeft -= count;
            result[i] = count;
        }
        if (pvLeft > 0) {
            // All left PV is distributed to the last appliance
            result[lastDistributedIdx] += pvLeft;
        }

        return result;
    }

    /**
     * Archive PVs on the specified appliance
     *
     * @param appl - appliance
     * @param pvNames - PV names
     * @param sampleMethod - Sample Method
     * @param samplePeriod - Sample Period
     */
    private JSONArray archivePVonAppliance(Appliance appl, String[] pvNames, String sampleMethod, Float samplePeriod) {
        String requestUrl = appl.getMgmtURL() + "/archivePV";
        Map<String, List<String>> parms = new HashMap<>();
        String pv = StringUtils.join(pvNames, ",");
        parms.put("pv", Arrays.asList(pv));
        parms.put("appliance", Arrays.asList(appl.getIdentity()));
        if (StringUtils.isNoneEmpty(sampleMethod)) {
            parms.put("samplingmethod", Arrays.asList(sampleMethod));
            parms.put("samplingperiod", Arrays.asList(samplePeriod.toString()));
        }
        try {
            String responseBody = HttpConnector.sendRequestToRemote(requestUrl, parms);
            return JSONObject.parseArray(responseBody);
        } catch (Exception e) {
            logger.warn("Failed to archive PV on the appliance {}: {}", appl.getIdentity(), e.getMessage());
        }
        return new JSONArray();
    }

    /**
     * Check if there is no working appliance
     *
     * @return - true if no working appliance
     */
    @Override
    public boolean isNoWorkingAppliance() {
        return workingAppliances.isEmpty();
    }

    // Get health status for Archiver
    @Override
    public HealthStatus getArchiverStatus() {
        HealthStatus health = new HealthStatus();
        health.setStatus(workingAppliances.size(), applianceMap.values().size());
        return health;
    }

    /**
     * Update the appliance status list
     *
     * @param appliances - appliance list
     */
    private synchronized void updateWorkingAppliance(List<String> appliances) {
        workingAppliances.clear();
        if (!appliances.isEmpty()) {
            workingAppliances.addAll(appliances);
        }
    }

    /**
     * Start the task to monitor the appliance status
     *
     */
    @PostConstruct
    public void startMonitorAppliance() {
        scheduledService.scheduleAtFixedRate(() -> checkApplianceStatus(), 5 * 1000L,
                SiteConfigUtil.getApplianceRefreshInterval(), TimeUnit.MILLISECONDS);
    }

    /**
     * Check the appliance status
     *
     */
    private void checkApplianceStatus() {
       // logger.debug("Checking appliance status ...");
        List<String> activeAppliances = new ArrayList<>();
        Set<String> members = retrieveService.getAvailableAA();
        if (members.isEmpty()) {
            logger.warn("No appliance is in service!");
        }
        else {
            Appliance appliance;
            for(String member : members) {
                if (memberMap.containsKey(member)) {
                    appliance = memberMap.get(member);
                    activeAppliances.add(appliance.getIdentity());
                }
                else {
                    appliance = getMatchedAppliance(member);
                    if (appliance != null) {
                        activeAppliances.add(appliance.getIdentity());
                        memberMap.put(member, appliance);
                    }
                }
            }
        }
        if (activeAppliances.isEmpty()) {
            logger.warn("All appliance are down!");
        }
        updateWorkingAppliance(activeAppliances);
    }

    /**
     * Get all appliance status from a specified appliance
     *
     * @param appliance - appliance
     * @return - appliance list
     */
    protected List<String> getApplianceStatusFromAppliance(Appliance appliance) {
        List<String> activeAppliances = new ArrayList<>();
        String requestUrl = appliance.getMgmtURL() + "/getInstanceMetrics";
        try {
            String responseBody = HttpConnector.sendRequestToRemote(requestUrl);
            JSONArray applList = JSONObject.parseArray(responseBody);
            for(Object appl : applList) {
                JSONObject applObject = (JSONObject)appl;
                if ("Working".equals(applObject.getString("status"))) {
                    String applId = applObject.getString(ID_TAG);
                    if (applianceMap.containsKey(applId)) {
                        activeAppliances.add(applId);
                        applianceMap.get(applId).setPvCount(applObject.getIntValue("pvCount"));
                    }
                }
            }
        } catch (Exception e) {
            logger.warn("Failed to refresh the appliance status: {}", e.getMessage());
        }
        return activeAppliances;
    }

    /**
     * Get a matched appliance for a cluster member
     *
     * @param memberAddr - cluster member address (ip:port)
     * @return - matched appliance, null if no matching.
     */
    private Appliance getMatchedAppliance(String memberAddr) {
        for(Appliance appl : applianceMap.values()) {
            if (appl.matchingClusterMember(memberAddr)) {
                return appl;
            }
        }
        return null;
    }

    /**
     * Get PV count on the specified appliance
     *
     * @param appl - appliance
     * @return pv count
     */
    private int getPVCount(Appliance appl) {
        int count = 0;
        String requestUrl = appl.getMgmtURL() + "/getApplianceMetricsForAppliance?appliance=" + appl.getIdentity();
        try {
            String responseBody = HttpConnector.sendRequestToRemote(requestUrl);
            JSONArray items = JSONObject.parseArray(responseBody);
            for(int i = 0; i < items.size(); i++) {
                JSONObject itemObject = items.getJSONObject(i);
                if ("Total PV count".equals(itemObject.getString("name"))) {
                    count = Integer.valueOf(itemObject.getString("value"));
                    break;
                }
            }
        } catch (Exception e) {
            logger.warn("Failed to get appliance metrics for {}: {}", appl.getIdentity(), e.getMessage());
        }
        return count;
    }

    /**
     * Add non-working appliance into metrics report
     *
     * @param report - working appliance report
     */
    private void addNonWorkingAppliance(JSONArray report) {
        Set<String> workingIdMap = new HashSet<>();
        for(Object appObj : report.toArray()) {
            workingIdMap.add(((JSONObject)appObj).getString(ID_TAG));
        }
        List<Object> nonWorkingAppls = new ArrayList<>();
        for(Appliance appl : getAllAppliances()) {
            if (!workingIdMap.contains(appl.getIdentity())) {
                JSONObject applObj = new JSONObject();
                applObj.put(ID_TAG, appl.getIdentity());
                applObj.put("status", "Non-Working");
                nonWorkingAppls.add(applObj);
            }
        }
        if (!nonWorkingAppls.isEmpty()) {
            report.addAll(nonWorkingAppls);
        }
    }

}
