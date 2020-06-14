//package com.hlsii.controller;
//
//import com.hlsii.entity.OperationLog;
//import com.hlsii.service.OperationLogService;
//import io.swagger.annotations.ApiOperation;
//import org.apache.commons.lang3.StringUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
///**
// * @author ScXin
// * @date 4/30/2020 11:57 PM
// */
//
//@RestController
//@RequestMapping("/hdars")
//public class UserLogController {
//
//    @Autowired
//    private OperationLogService operationLogService;
//
//
//    //The API has been verified
//    @ApiOperation("根据日志ID查询日志")
//    @GetMapping("/userLog/getLog/{id}")
//    public OperationLog get(@PathVariable("id") String id) {
//        OperationLog operationLog = null;
//        if (StringUtils.isNotEmpty(id)) {
//            System.out.println("id==" + id);
//            operationLog = operationLogService.get(id);
//        }
//        if (operationLog == null) {
//            operationLog = new OperationLog();
//        }
//        return operationLog;
//    }
//
//    // The API has been verified
//    @ApiOperation("根据参数查询日志")
//    @GetMapping("/userLog/viewLog")
//    public List<OperationLog> getLogByCondition(@RequestParam(value = "loginName", required = false) String loginName,
//                                                @RequestParam(value = "userName", required = false) String userName,
//                                                @RequestParam(value = "loginIP", required = false) String loginIP,
//                                                @RequestParam(value = "operationType", required = false) String operationType,
//                                                @RequestParam(value = "startTime", required = false) String startTime,
//                                                @RequestParam(value = "endTime", required = false) String endTime) {
//
//        String queryCondition = "";
//        String userParam = "";
//        if (loginName != null) {
//            userParam += "login_name='" + loginName + "'";
//        }
//        if (userName != null) {
//            if (!userParam.equals("")) {
//                userParam += " and user_name='" + userName + "'";
//            } else {
//                userParam += "user_name='" + userName + "'";
//            }
//        }
//        if (loginIP != null) {
//            if (!queryCondition.equals("")) {
//                queryCondition += " and remote_ip='" + loginIP + "'";
//            } else {
//                queryCondition += "remote_ip='" + loginIP + "'";
//            }
//        }
//        if (operationType != null) {
//            if (!queryCondition.equals("")) {
//                queryCondition += " and op_type='" + operationType + "'";
//            } else {
//                queryCondition += "op_type='" + operationType + "'";
//            }
//        }
//        if (startTime != null) {
//            if (!queryCondition.equals("")) {
//                queryCondition += " and op_time>='" + startTime + "'";
//            } else {
//                queryCondition += "op_time>='" + startTime + "'";
//            }
//        }
//
//        if (endTime != null) {
//            if (!queryCondition.equals("")) {
//                queryCondition += " and op_time<='" + endTime + "'";
//            } else {
//                queryCondition += "op_time<='" + endTime + "'";
//            }
//        }
//
//        return operationLogService.getLogByCondition(queryCondition, userParam);
//    }
//
//
//    @ApiOperation("按页获取日志")
//    @GetMapping("/userLog/page")
//    public List<OperationLog> getOperlogByPage(@RequestParam(value = "pageNum") int pageNum,
//                                               @RequestParam(value = "pageSize") int pageSize,
//                                               @RequestParam(value = "loginName", required = false) String loginName,
//                                               @RequestParam(value = "userName", required = false) String userName,
//                                               @RequestParam(value = "loginIP", required = false) String loginIP,
//                                               @RequestParam(value = "operationType", required = false) String operationType,
//                                               @RequestParam(value = "startTime", required = false) String startTime,
//                                               @RequestParam(value = "endTime", required = false) String endTime) {
//
//        String queryCondition = "";
//        String userParam = "";
//        if (loginName != null) {
//            userParam += "login_name='" + loginName + "'";
//        }
//        if (userName != null) {
//            if (!userParam.equals("")) {
//                userParam += " and user_name='" + userName + "'";
//            } else {
//                userParam += "user_name='" + userName + "'";
//            }
//        }
//        if (loginIP != null) {
//            if (!queryCondition.equals("")) {
//                queryCondition += " and remote_ip='" + loginIP + "'";
//            } else {
//                queryCondition += "remote_ip='" + loginIP + "'";
//            }
//        }
//        if (operationType != null) {
//            if (!queryCondition.equals("")) {
//                queryCondition += " and op_type='" + operationType + "'";
//            } else {
//                queryCondition += "op_type='" + operationType + "'";
//            }
//        }
//        if (startTime != null) {
//            if (!queryCondition.equals("")) {
//                queryCondition += " and op_time>='" + startTime + "'";
//            } else {
//                queryCondition += "op_time>='" + startTime + "'";
//            }
//        }
//
//        if (endTime != null) {
//            if (!queryCondition.equals("")) {
//                queryCondition += " and op_time<='" + endTime + "'";
//            } else {
//                queryCondition += "op_time<='" + endTime + "'";
//            }
//        }
//       return  operationLogService.getLogByPage(queryCondition, userParam, pageNum, pageSize);
//
//    }
//
//}
