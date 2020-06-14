package com.hlsii.controller;

import com.hlsii.entity.WhiteIP;
import com.hlsii.service.WhiteIPService;
import com.hlsii.util.IPUtil;
import com.hlsii.vo.ReturnCode;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

//import javax.ws.rs.Path;
import java.util.List;

//import com.sun.tools.javac.util.List;

/**
 * @author ScXin
 * @date 4/30/2020 10:34 PM
 */

@RestController
@RequestMapping("/hdars")
public class WhiteController {
    @Autowired
    private WhiteIPService whiteIPService;
//
//    @Autowired
//    private IRecordUserLogService recordUserLogService;


    // The API has been verified
    @ApiOperation("保存一个whiteip")
    @PostMapping("/ip/saveWhiteIP/{ipAddr}")
    public ReturnCode saveIp(@PathVariable("ipAddr") String ipAddr) {
        if (!IPUtil.matchIPPattern(ipAddr)) {

            String msg = "The IP address format is incorrect!";
            ReturnCode returnCode = new ReturnCode(false, msg);

            return returnCode;

        }
        WhiteIP existedIp = whiteIPService.existedIP(ipAddr);
        if (existedIp != null) {
            String msg = "The IP is already existed!";
            ReturnCode returnCode = new ReturnCode(false, msg);
        }


        ReturnCode returnCode = whiteIPService.save(ipAddr);
        if (returnCode.isSuccess()) {
//            recordUserLogService.logOperation(OperationType.CREATE_WHITEIP, whiteIP.getIpAddr());
        }
        return returnCode;
    }


    //The API has been verified
    @ApiOperation("查询所有的whiteIP")
    @GetMapping("/ip/getAllWhiteIP")
    public List<WhiteIP> getAllWhiteIP() {
        List<WhiteIP> whiteIPsList = whiteIPService.getAll();
//        recordUserLogService.logOperation(OperationType.VIEW_IP_LIST, null);
        return whiteIPsList;
    }

    @ApiOperation("根据IP地址查询whiteIP")
    @GetMapping("/ip/getWhiteIPByAddr/{ipAddr}")
    public WhiteIP getWhiteIPByAddr(@PathVariable("ipAddr") String ipAddr) {
        WhiteIP whiteIP = whiteIPService.getWhiteIPByAddr(ipAddr);
        if (whiteIP != null) {
//            recordUserLogService.logOperation(OperationType.VIEW_IP_LIST, null);
        }
        return whiteIP;
    }

    @ApiOperation("根据IP地址删除从白名单中删除该Whiteip")
    @DeleteMapping("/ip/deleteWhiteIP/{ipAddr}")
    public boolean deletIp(@PathVariable("ipAddr") String ipAddr) {
        boolean isSuccess = whiteIPService.delete(ipAddr);
        if (isSuccess) {
//            recordUserLogService.logOperation(OperationType.DELETE_IP, ipAddr);
        }
        return isSuccess;
    }


    // 修改白名单
    @ApiOperation("修改白名单Ip地址")
    @RequestMapping("/ip/modifyIp/{id}/{newIpAddr}")
    public boolean modify(@PathVariable("id") String id,
                          @PathVariable("newIpAddr") String newIpAddr) {
        boolean isSuccess = whiteIPService.modify(id, newIpAddr);
        if (isSuccess) {
//            recordUserLogService.logOperation(OperationType.DELETE_IP, ipAddr);
        }
        return isSuccess;
    }
}
