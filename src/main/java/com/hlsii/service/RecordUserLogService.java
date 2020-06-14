package com.hlsii.service;//package com.hlsii.service;
//
//import com.hlsii.commdef.OperationType;
//import com.hlsii.dao.OperationLogDao;
//import com.hlsii.entity.OperationLog;
//import com.hlsii.entity.User;
//import com.hlsii.util.WebUtil;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.Date;
//import java.util.UUID;
//
///**
// * @author ScXin
// * @date 4/26/2020 8:11 PM
// */
//@Service
//public class RecordUserLogService implements IRecordUserLogService {
//    private Logger logger = LoggerFactory.getLogger(getClass());
//
//    private static final int MAX_CONTENT_LEN = 1000;
//
////    // User entity name
////    private static final String USER_ENTITY_NAME = "com.cosylab.hadars.entity.User";
////
////    // The entity needs to be record for operation log
////    private Set<String> entitiesNeedRecord = new HashSet<>();
////
////    // The field no need to be record in operation log
////    private Set<String> fieldsNoNeedRecord = new HashSet<>();
//
//    @Autowired
//    private OperationLogDao operationLogDao;
//
//    /**
//     * Log user operation
//     *
//     * @param opType     - operation type
//     * @param dataObject - Data object for the operation
//     */
//    public void logOperation(OperationType opType, Object dataObject) {
//        User currentUser = WebUtil.getCurrentLoginUser();
//        String ip = WebUtil.getLoginUserIP();
//        String operationContent = "";
//        if (dataObject != null) {
//            if (opType == OperationType.ARCHIEVE_PV) {
//                String[] pvs = dataObject.toString().split(",");
//                operationContent += pvs.length + " PV";
//                if (pvs.length > 1) {
//                    operationContent += "s";
//                }
//            }
//            operationContent += " [" + dataObject.toString() + "]";
//        }
//        OperationLog opLog = new OperationLog();
//        String id = UUID.randomUUID().toString().replaceAll("-", "");
//        opLog.setId(id);
//        opLog.setUser(currentUser);
//        opLog.setOpType(opType);
//        opLog.setOperationTime(new Date());
//        opLog.setRemoteIp(ip);
//        if (operationContent.length() > MAX_CONTENT_LEN) {
//            // Avoid to long in the field
//            operationContent = operationContent.substring(0, MAX_CONTENT_LEN - 3) + "...";
//        }
//        opLog.setContent(operationContent);
//
//        // Generate the log to log file
//        outputLog(opLog);
//
//        // Save the log to DB
//        operationLogDao.save(opLog);
//
//    }
//
////    /**
////     * Log the user operation by intercept the inserting into DB event
////     *
////     * @param event - post insert event
////     */
////    @Override
////    public void interceptChange(PostInsertEvent event) {
////        if (entitiesNeedRecord.contains(event.getEntity().getClass().getName())) {
////            interceptChange(event.getSession(), event.getPersister(), event.getEntity(), event.getState(), null);
////        }
////    }
//
////    /**
////     * Log the user operation by intercept the update to DB event
////     *
////     * @param event - post update event
////     */
////    @Override
////    public void interceptChange(PostUpdateEvent event) {
////        if (entitiesNeedRecord.contains(event.getEntity().getClass().getName())) {
////            interceptChange(event.getSession(), event.getPersister(), event.getEntity(), event.getState(),
////                    event.getOldState());
////        }
////    }
//
//    /**
//     * Save the operation log to DB
//     *
//     * @param opLog - operation log
//    //     */
////    private void saveLog(OperationLog opLog) {
////        // Generate the log to log file
////        outputLog(opLog);
////        operationLogService.save(opLog);
////    }
//
//    /**
//     * Intercept the entity's change
//     *
//     * @param session   - session for the event
//     * @param persister - entity persister
//     * @param entity    - entity after change
//     * @param state     - each field's new value
//     * @param oldState  - each field's old value
//     */
////    private void interceptChange(EventSource session, EntityPersister persister, Object entity, Object[] state, Object[] oldState) {
////        OperationLog opLog = generateOperationLog(persister, entity, state, oldState);
//
//		/*
//		session.save(opLog);
//		session.flush();
//		*/
//
//    // Create a new thread to save the operation log to DB
////        new Thread(() -> saveLog(opLog)).start();
////    }
//
//    /**
//     * Generate the operation log for entity's change
//     *
//     * @param persister - entity persister
//     * @param entity    - entity after change
//     * @param state     - each field's new value
//     * @param oldState  - each field's old value
//     * @return - operation log
//     */
////    private OperationLog generateOperationLog(EntityPersister persister, Object entity, Object[] state, Object[] oldState) {
////        List<String> changedFieldNames = new ArrayList<>();
////        List<String> changedFieldTitles = new ArrayList<>();
////        List<String> changedFieldVals = new ArrayList<>();
////        OperationType opType = null;
////        for (int i = 0; i < state.length; i++) {
////            String changedContent = null;
////            if (oldState == null) {
////                // Create a new entity
////                changedContent = getFieldVal(state[i]);
////                if (opType == null) {
////                    opType = getOptype(entity, false);
////                }
////            } else {
////                // Update the entity
////                String oldValue = getFieldVal(oldState[i]);
////                String newValue = getFieldVal(state[i]);
////                if (opType == null) {
////                    opType = getOptype(entity, true);
////                }
////                if (oldValue.equals(newValue)) {
////                    // Unchanged field not recorded
////                    continue;
////                }
////                changedContent = oldValue + " => " + newValue;
////            }
////            if (StringUtils.isNotEmpty(changedContent)) {
////                String changedFieldName = getChangedFieldName(persister, i);
////                changedFieldNames.add(changedFieldName);
////                changedFieldTitles.add(getFieldTitle(changedFieldName));
////                changedFieldVals.add(changedContent);
////            }
////        }
////
////        User currentUser = WebUtil.getCurrentLoginUser();
////        String ip = WebUtil.getLoginUserIP();
////        OperationLog opLog = new OperationLog();
////        opLog.setUser(currentUser);
////        opLog.setOperationTime(new Date());
////        opLog.setOpType(opType);
////        opLog.setRemoteIp(ip);
////        opLog.setContent(generateDetailOperationContent(currentUser, entity, oldState != null, changedFieldNames,
////                changedFieldTitles, changedFieldVals));
////        return opLog;
////    }
//
//    /**
//     * Get operation type for save an entity
//     *
//     * @param entity    - data entity
//     * @param isChanged - is changed the entity
//     * @return - operation type
//     */
////    private OperationType getOptype(Object entity, boolean isChanged) {
////        if (isChanged) {
////            return entity instanceof User ? OperationType.MODIFY_USER : OperationType.MODIFY_WHITEIP;
////        }
////        return entity instanceof User ? OperationType.CREATE_USER : OperationType.CREATE_WHITEIP;
////    }
//
//    /**
//     * Generate the detail content of the operation log
//     *
//     * @param currentUser        - Current login user
//     * @param entity             - entity after change
//     * @param isChangeRecord     - is the operation for change a record
//     * @param changedFieldNames  - changed field name list
//     * @param changedFieldTitles - changed field title list
//     * @param changedFieldVals   - changed field value description list
//     * @return - operation detail
//     */
////    private String generateDetailOperationContent(User currentUser, Object entity, boolean isChangeRecord,
////                                                  List<String> changedFieldNames, List<String> changedFieldTitles, List<String> changedFieldVals) {
////        StringBuilder operationContent = new StringBuilder();
////        if (changedFieldNames.size() == 1) {
////            if ("delFlag".equals(changedFieldNames.get(0))) {
////                if (isUserEntity(entity.getClass().getName())) {
////                    User user = (User) entity;
////                    operationContent.append("Delete User [" + user.getLoginName() + "]");
////                } else {
////                    WhiteIP whiteIP = (WhiteIP) entity;
////                    operationContent.append("Delete White IP [" + whiteIP.getIpAddr() + "]");
////                }
////            } else if ("password".equals(changedFieldNames.get(0))) {
////                User user = (User) entity;
////                if (currentUser.equals(user)) {
////                    operationContent.append("Change Password");
////                } else {
////                    operationContent.append("Reset Password for [" + user.getLoginName() + "]");
////                }
////            }
////        }
////        if (operationContent.length() == 0) {
////            return consolidateChangedItems(entity, isChangeRecord, changedFieldNames, changedFieldTitles,
////                    changedFieldVals);
////        }
////        return operationContent.toString();
////    }
//
//    /**
//     * Consolidate each field's change to the change detail
//     *
//     * @param entity             - entity after change
//     * @param isChangeRecord     - is the operation for change a record
//     * @param changedFieldNames  - changed field name list
//     * @param changedFieldTitles - changed field title list
//     * @param changedFieldVals   - changed field value description list
//     * @param isChangeRecord
//     * @param changedFieldNames
//     * @param changedFieldTitles
//     * @param changedFieldVals
//     * @return - change detail
//     */
////    private String consolidateChangedItems(Object entity, boolean isChangeRecord, List<String> changedFieldNames,
////                                           List<String> changedFieldTitles, List<String> changedFieldVals) {
////        StringBuilder operationContent = new StringBuilder();
////        for (int i = 0; i < changedFieldNames.size(); i++) {
////            if (fieldsNoNeedRecord.contains(changedFieldNames.get(i))) {
////                continue;
////            }
////            if (operationContent.length() > 0) {
////                operationContent.append(", ");
////            }
////            operationContent.append(changedFieldTitles.get(i) + ": ");
////            operationContent.append(changedFieldVals.get(i));
////        }
////        operationContent.insert(0, "[");
////        operationContent.append("]");
////        if (isUserEntity(entity.getClass().getName())) {
////            User user = (User) entity;
////            if (isChangeRecord) {
////                operationContent.insert(0, "Modify " + user.getLoginName() + "'s Info ");
////            } else {
////                operationContent.insert(0, "Create User " + user.getLoginName() + " ");
////            }
////        } else {
////            if (isChangeRecord) {
////                operationContent.insert(0, "Modify White IP ");
////            } else {
////                operationContent.insert(0, "Add New White IP ");
////            }
////        }
////        return operationContent.toString();
////    }
//
//    /**
//     * Is the user entity?
//     *
//     * @param entityName - entity name
//     * @return - true for user entity
//     */
////    private boolean isUserEntity(String entityName) {
////        return USER_ENTITY_NAME.equals(entityName);
////    }
//
//    /**
//     * Get the field value in string
//     *
//     * @param fieldVal - field value object
//     * @return - field value in string
//     */
////    private String getFieldVal(Object fieldVal) {
////        String val;
////        if (fieldVal instanceof Role) {
////            val = ((Role) fieldVal).getRoleName();
////        } else {
////            val = fieldVal == null ? "" : fieldVal.toString();
////        }
////        return val;
////    }
//
//    /**
//     * Get the field name
//     *
//     * @param persister - entity persister
//     * @param fieldIdx  - field index
//     * @return - field name
//     */
////    private String getChangedFieldName(EntityPersister persister, int fieldIdx) {
////        return persister.getPropertyNames()[fieldIdx];
////    }
//
//    /**
//     * Get the field name title in the log
//     *
//     * @param fieldName - field name
//     * @return - field name title
//     */
////    private String getFieldTitle(String fieldName) {
////        if ("userRole".equals(fieldName)) {
////            return "Role";
////        } else if ("ipAddr".equals(fieldName)) {
////            return "IP";
////        }
////
////        // Change the first letter to upper case
////        return fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
////    }
//
//    /**
//     * Output the operation log to appender
//     *
//     * @param opLog - log record
//     */
//    private void outputLog(OperationLog opLog) {
//        // Generate the log to log file
//        logger.info("{}({}) {} {}", opLog.getUser().getLoginName(), opLog.getRemoteIp(),
//                opLog.getOpType(), opLog.getContent());
//    }
////
////    public Set<String> getEntitiesNeedRecord() {
////        return entitiesNeedRecord;
////    }
////
////    public void setEntitiesNeedRecord(Set<String> entitiesNeedRecord) {
////        this.entitiesNeedRecord = entitiesNeedRecord;
////    }
////
////    public Set<String> getFieldsNoNeedRecord() {
////        return fieldsNoNeedRecord;
////    }
////
////    public void setFieldsNoNeedRecord(Set<String> fieldsNoNeedRecord) {
////        this.fieldsNoNeedRecord = fieldsNoNeedRecord;
////    }
//}
