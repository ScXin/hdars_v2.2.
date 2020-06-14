package com.hlsii.service;

import hadarshbaseplugin.commdef.PostProcessing;
import com.hlsii.commdef.Appliance;
import com.hlsii.vo.HealthStatus;
import com.hlsii.vo.RetrieveData;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

/**
 * Archiver System Interface
 * <p>
 * Provide API to access Archiver System using AA REST API
 */
public interface IArchiverSystemService {
    /**
     * Get the appliance by Id
     *
     * @param applianceId - appliance id
     * @return - appliance
     */
    Appliance getAppliance(String applianceId);

    /**
     * Get a random working appliance
     *
     * @return - a random working appliance, if no working appliance return null.
     */
    Appliance getRandomWorkingAppliance();

    /**
     * Route the restful API request from WebUI to the specified AA MGMT host.
     * <p>
     * If there is error at routing the request, the error status code is set in the response, otherwise the return
     * content from the routing host is set in the body of the response.
     *
     * @param req  - HttpServletRequest from WebUI
     * @param resp - HttpServletResponse back to remote
     * @throws IOException - IO exception at error
     */
    void routeMgmtReq(HttpServletRequest req, HttpServletResponse resp) throws IOException;


//    String routeMgmtReq(String urlParam) throws IOException;

    /**
     * Send the Mgmt request to remote MGMT
     *
     * @param requestUri - request MGMT uri not includes the host name
     * @param parms      - request parameters
     * @param method     - HTTP method
     * @return - The response text from the response body
     * @throws IOException - IO exception at error
     */
    String sendMgmtReq(String requestUri, Map<String, List<String>> parms, String method) throws IOException;

    /**
     * Send the request URL to remote host (host can be MGMT or retrieval)
     *
     * @param requestUrl - request url includes the host name, but not include parameter.
     * @param parms      - request parameters
     * @param method     - HTTP method
     * @return - The response text from the response body
     * @throws IOException - IO exception at error
     */
    String sendRequestToRemote(String requestUrl, Map<String, List<String>> parms, String method) throws IOException;

    /**
     * Retrieve PV historical data
     *
     * @param pv       - PV name
     * @param from     - from time
     * @param to       - to time
     * @param pp       - post processing
     * @param duration - sample duration
     * @return - data in array
     * @throws IOException - IO exception at error
     */
    List<RetrieveData> retrievePVData(String pv, Timestamp from, Timestamp to, PostProcessing pp, int duration)
            throws IOException;

    /**
     * Get Matching PVs from Data Retrieval
     *
     * @param limit - the maximum return PV number
     * @param pv    - PV name pattern
     * @return - Matching PV list
     * @throws IOException - IO exception at error
     */
    String getMatchingPVs(int limit, String pv) throws IOException;

    /**
     * Check if there is no working appliance
     *
     * @return - true if no working appliance
     */
    boolean isNoWorkingAppliance();

    /**
     * Get all configured appliance list
     *
     * @return
     */
    List<Appliance> getAllAppliances();

    /**
     * Archive the PVs, the archiving appliance is selected by Hadars.
     *
     * @param pvNames      - pv list
     * @param sampleMethod - Sample Method
     * @param samplePeriod - Sample Period
     * @param resp         - HttpServletResponse
     * @throws IOException
     */
    void archivePV(String[] pvNames, String sampleMethod, Float samplePeriod, HttpServletResponse resp) throws IOException;

    /**
     * Get metrics
     *
     * @param request
     * @param resp
     * @throws IOException
     */
    void getMetricsReport(HttpServletRequest request, HttpServletResponse resp) throws IOException;

    // Get health status for Archiver
    HealthStatus getArchiverStatus();
}
