package com.hlsii.controller;

import com.hlsii.util.SiteConfigUtil;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;

//import com.hlsii.service.IRecordUserLogService;

/**
 * @author ScXin
 * @date 4/26/2020 12:31 PM
 */
@RestController
@RequestMapping(value = "/hdars")
public class MGMTPVController extends MGMTBaseController {

//    @Autowired
//    private IRecordUserLogService recordUserLogService;


    @ApiOperation("得到所有的存档的PV信息")
    @GetMapping("/mgmt/getAllPVs")
    public void getAllPVs(HttpServletRequest request, HttpServletResponse resp) throws IOException {
//        archiverSystemService.routeMgmtReq();
        archiverSystemService.routeMgmtReq(request, resp);
    }

    @ApiOperation("得到PV的全名")
    @RequestMapping("/mgmt/getAllExpandedPVNames")
    public void getAllExpandedPVNames(HttpServletRequest request, HttpServletResponse resp) throws IOException {
        archiverSystemService.routeMgmtReq(request, resp);
    }

    //前端的pv参数以逗号隔开传入到控制器
    @ApiOperation("获取PV的状态信息")
    @RequestMapping("/mgmt/getPVStatus")
    public void getPVStatus(HttpServletRequest request, HttpServletResponse resp) throws IOException {
        archiverSystemService.routeMgmtReq(request, resp);
    }

    @ApiOperation("获取PV的类型信息")
    @RequestMapping("/mgmt/getPVTypeInfo")
    public void getPVTypeInfo(HttpServletRequest request, HttpServletResponse resp) throws IOException {
        archiverSystemService.routeMgmtReq(request, resp);
    }

    @ApiOperation("存档PV的接口")
    @RequestMapping("/mgmt/archivePV")
    public void archivePV(HttpServletRequest request, HttpServletResponse resp) throws IOException {
        if (SiteConfigUtil.getArchivingPVDistributedByHadars()) {
            // The archiving PVs are distributed by HADARS
            String pvStr = request.getParameter("pv");
            String samplingMethod = request.getParameter("samplingmethod");
            Float samplingPeriod = null;
            if (StringUtils.isNotEmpty(samplingMethod)) {
                samplingPeriod = Float.valueOf(request.getParameter("samplingperiod"));
            }
            archiverSystemService.archivePV(pvStr.split(","), samplingMethod, samplingPeriod, resp);
        } else {
            // The archiving PVs are distributed by archiver system
            archiverSystemService.routeMgmtReq(request, resp);
        }
//        recordUserLogService.logOperation(OperationType.ARCHIEVE_PV, request.getParameter("pv"));
    }

    @ApiOperation("停止存档PV的接口")
    @RequestMapping("/mgmt/pauseArchivingPV")
    public void pauseArchivingPV(HttpServletRequest request, HttpServletResponse resp) throws IOException {
        archiverSystemService.routeMgmtReq(request, resp);
//        recordUserLogService.logOperation(OperationType.PAUSE_PV, request.getParameter("pv"));
    }

    @ApiOperation("恢复存档PV的接口")
    @RequestMapping("/mgmt/resumeArchivingPV")
    public void resumeArchivingPV(HttpServletRequest request, HttpServletResponse resp) throws IOException {
        archiverSystemService.routeMgmtReq(request, resp);
//        recordUserLogService.logOperation(OperationType.RESUME_PV, request.getParameter("pv"));
    }

    @ApiOperation("获取PV的存档位置信息")
    @RequestMapping("/mgmt/getStoresForPV")
    public void getStoresForPV(HttpServletRequest request, HttpServletResponse resp) throws IOException {
        archiverSystemService.routeMgmtReq(request, resp);
    }

    @RequestMapping("/mgmt/consolidateDataForPV")
    public void consolidateDataForPV(HttpServletRequest request, HttpServletResponse resp) throws IOException {
        archiverSystemService.routeMgmtReq(request, resp);
//        recordUserLogService.logOperation(OperationType.CONSOLIDATE_PV_DATA, request.getParameter("pv"));
    }

    @ApiOperation("删除PV的接口")
    @RequestMapping("/mgmt/deletePV")
    public void deletePV(HttpServletRequest request, HttpServletResponse resp) throws IOException {
        archiverSystemService.routeMgmtReq(request, resp);
//        recordUserLogService.logOperation(OperationType.REMOVE_PV, request.getParameter("pv"));
    }

    @RequestMapping("/mgmt/abortArchivingPV")
    public void abortArchivingPV(HttpServletRequest request, HttpServletResponse resp) throws IOException {
        archiverSystemService.routeMgmtReq(request, resp);
    }

    @RequestMapping("/mgmt/abortArchivingPVForThisAppliance")
    public void abortArchivingPVForThisAppliance(HttpServletRequest request, HttpServletResponse resp)
            throws IOException {
        archiverSystemService.routeMgmtReq(request, resp);
    }

    @ApiOperation("改变存档参数的接口")
    @RequestMapping("/mgmt/changeArchivalParameters")
    public void changeArchivalParameters(HttpServletRequest request, HttpServletResponse resp) throws IOException {
        archiverSystemService.routeMgmtReq(request, resp);
    }

    // The API has been verified
    @ApiOperation("获取PV details的接口")
    @RequestMapping("/mgmt/getPVDetails")
    public void getPVDetails(HttpServletRequest request, HttpServletResponse resp) throws IOException {
        archiverSystemService.routeMgmtReq(request, resp);
//        recordUserLogService.logOperation(OperationType.QUERY_PV, request.getParameter("pv"));

    }

    @ApiOperation("重命名PV的接口")
    @RequestMapping("/mgmt/renamePV")
    public void renamePV(HttpServletRequest request, HttpServletResponse resp) throws IOException {
        archiverSystemService.routeMgmtReq(request, resp);
    }

    @RequestMapping("/mgmt/reshardPV")
    public void reshardPV(HttpServletRequest request, HttpServletResponse resp) throws IOException {
        archiverSystemService.routeMgmtReq(request, resp);
    }

    @RequestMapping("/mgmt/addAlias")
    public void addAlias(HttpServletRequest request, HttpServletResponse resp) throws IOException {
        archiverSystemService.routeMgmtReq(request, resp);
    }

    @RequestMapping("/mgmt/removeAlias")
    public void removeAlias(HttpServletRequest request, HttpServletResponse resp) throws IOException {
        archiverSystemService.routeMgmtReq(request, resp);
    }

    @RequestMapping("/mgmt/skipAliasCheck")
    public void skipAliasCheck(HttpServletRequest request, HttpServletResponse resp) throws IOException {
        archiverSystemService.routeMgmtReq(request, resp);
    }

    @RequestMapping("/mgmt/changeTypeForPV")
    public void changeTypeForPV(HttpServletRequest request, HttpServletResponse resp) throws IOException {
        archiverSystemService.routeMgmtReq(request, resp);
    }

    @RequestMapping("/mgmt/modifyMetaFields")
    public void modifyMetaFields(HttpServletRequest request, HttpServletResponse resp) throws IOException {
        archiverSystemService.routeMgmtReq(request, resp);
    }
}
