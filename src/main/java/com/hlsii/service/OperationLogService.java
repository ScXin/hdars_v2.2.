//package com.hlsii.service;
//
//import com.hlsii.dao.OperationLogDao;
//import com.hlsii.entity.OperationLog;
////import org.apache.zookeeper.Op;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.List;
//
//@Service
//@Transactional(readOnly = true)
//public class OperationLogService {
//
//    @Autowired
//    private OperationLogDao operationLogDao;
//
//    public OperationLog get(String id) {
//        return operationLogDao.get(id);
//    }
//
//    public List<OperationLog> getLogByCondition(String queryCondition, String userParam) {
//        return operationLogDao.getLogByCondition(queryCondition, userParam);
//    }
//
//    public List<OperationLog> getLogByPage(String queryCondition, String userParam, int pageNum, int pageSize) {
//        return operationLogDao.getLogByPage(queryCondition, userParam, pageNum, pageSize);
//    }
//}
