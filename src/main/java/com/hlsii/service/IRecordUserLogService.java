package com.hlsii.service;//package com.hlsii.service;
////
////import org.hibernate.event.spi.PostInsertEvent;
////import org.hibernate.event.spi.PostUpdateEvent;
//
//import com.hlsii.commdef.OperationType;
//
///**
// * Record user operation log service interface
// *
// */
//
//public interface IRecordUserLogService {
//    /**
//     * Log user operation
//     *
//     * @param opType - operation type
//     * @param dataObject - Data object for the operation
//     */
//    void logOperation(OperationType opType, Object dataObject);
//
//    /**
//     * Log the user operation by intercept the inserting into DB event
//     *
//     * @param event - post insert event
//     */
////    void interceptChange(PostInsertEvent event);
//
//    /**
//     * Log the user operation by intercept the update to DB event
//     *
//     * @param event - post update event
//     */
////    void interceptChange(PostUpdateEvent event);
////
//    /**
//     * Log user operation
//     *
//     * @param opType - operation type
//     * @param oldDataObject - Data object before the operation
//     * @param newDataObject - Data object after the operation
//     */
//    //void logOperation(OperationType opType, Object oldDataObject, Object newDataObject);
//}
