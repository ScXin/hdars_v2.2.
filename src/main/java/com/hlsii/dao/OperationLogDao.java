//package com.hlsii.dao;
//
//import com.hlsii.commdef.OperationType;
//import com.hlsii.entity.OperationLog;
//import com.hlsii.entity.Role;
//import com.hlsii.entity.User;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.jdbc.core.RowMapper;
//import org.springframework.stereotype.Repository;
//
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.text.SimpleDateFormat;
//import java.util.List;
//
////import org.joda.time.DateTime;
//@Repository
//public class OperationLogDao {
//
//    @Autowired
//    private JdbcTemplate jdbcTemplate;
//
//
//    //
//    public void save(OperationLog operationLog) {
//        String id = operationLog.getId();
//        String userId = operationLog.getUser().getId();
//        String remote_id = operationLog.getRemoteIp();
//        String oper_type = operationLog.getOpType().getOperation();
//        String content = operationLog.getContent();
//        SimpleDateFormat sp = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
//        String oper_date = sp.format(operationLog.getOperationTime());
//        String sql = "insert into operation_log(id,user_id,remote_ip,op_type,op_time,content)values('" + id + "','" + userId + "','" + remote_id + "','" + oper_type + "','" + oper_date + "','" + content + "')";
//        try {
//            jdbcTemplate.update(sql);
//
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//    }
//
//
//    //The SQL has been verified
//    public OperationLog get(String id) {
////        String sql = "select op.id,op.op_time,op.op_type,op.remote_ip,op.content," +
////                    "op.role_id,op.login_name,op.user_name,op.password,op.organization,op.department,op.telephone,op.email," +
////                    "ro.role_name,ro.role_description from (select oper.id,oper.op_time,oper.op_type,oper.remote_ip,oper.content," +
////                    "u.id as user_id,u.role_id,u.login_name,u.user_name,u.password,u.organization," +
////                    "u.department,u.telephone,u.email from (select * from operation_log where id='" + id + "' and del_flag is null) " +
////                    "as oper left join user  as u on oper.user_id=u.id) as op left join role as ro on op.role_id=ro.id";
////The SQL has been verified
//        String sql = "select op.id,op.op_time,op.op_type,op.remote_ip,op.content," +
//                "op.role_id,op.user_id,op.login_name,op.user_name,op.password,op.organization,op.department,op.telephone,op.email," +
//                "ro.role_name,ro.role_description from (select oper.id,oper.op_time,oper.op_type,oper.remote_ip,oper.content," +
//                "u.id as user_id,u.role_id,u.login_name,u.user_name,u.password,u.organization," +
//                "u.department,u.telephone,u.email from (select * from operation_log where id='" + id + "' and del_flag is null) " +
//                "as oper left join user  as u on oper.user_id=u.id) as op left join role as ro on op.role_id=ro.id";
//        OperationLog operationLog = null;
//        try {
//            operationLog = jdbcTemplate.queryForObject(sql, new OperationLogMapper());
//            return operationLog;
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//        return operationLog;
//    }
//
//
//    //The SQL has been verified
//    public List<OperationLog> getLogByCondition(String queryCondition, String userParam) {
//
//
//        if (!userParam.equals("")) {
//            String userIdSQL = "select id from user where " + userParam;
//            String userId = jdbcTemplate.queryForObject(userIdSQL, String.class);
//            if (!queryCondition.equals("")) {
//                queryCondition += "and user_id='" + userId + "'";
//            } else {
//                queryCondition += "user_id='" + userId + "'";
//            }
//        }
//
//        List<OperationLog> operationLogs = null;
//        String sql = "";
//        if (queryCondition != "") {
//            String sq = "select * from operation_log where ";
//            String sq2 = "as op left join user as u on op.user_id=u.id) as o left join role as r on o.role_id=r.id";
////            String sql2=sq+queryCondition ;
////            System.out.println(sql2);
//            queryCondition = " " + queryCondition;
//            sql = "select o.id,o.op_time,o.op_type,o.remote_ip,o.content,o.user_id,o.role_id,o.login_name,o.user_name," +
//                    "o.password,o.organization,o.department,o.telephone,o.email,r.role_name,r.role_description from " +
//                    "(select op.id,op.op_time,op.op_type,op.remote_ip,op.content," +
//                    "op.user_id,u.role_id,u.login_name,u.user_name,u.password,u.organization,u.department,u.telephone,u.email" +
//                    " from (" + sq + queryCondition + ")" + " " + sq2;
//        } else {
//            sql = "select o.id,o.op_time,o.op_type,o.remote_ip,o.content,o.user_id,o.role_id,o.login_name,o.user_name," +
//                    "o.password,o.organization,o.department,o.telephone,o.email,r.role_name,r.role_description from " +
//                    "(select op.id,op.op_time,op.op_type,op.remote_ip,op.content,op.user_id,u.role_id," +
//                    "u.login_name,u.user_name,u.password,u.organization,u.department,u.telephone,u.email " +
//                    "from operation_log as op left join user as u on op.user_id=u.id) as o left join role as r on o.role_id=r.id";
//        }
//
//
////        String sql = "select o.id,o.op_time,o.op_type,o.remote_ip,o.oper_content,o.role_id,o.login_name,o.user_name," +
////                "o.password,o.organization,o.department,o.teleohone,o.email,r.role_name,r.role.description from " +
////                "(select op.id,op.op_time,op.op_type,op.remote_ ip,op.content," +
////                "u.role_id,u.login_name,u.user_name,u.password,u.organization,u.department,u.telephone,u.email" +
////                " from (select * from operation_log where '" + queryCondition + "') as op left join user as u on " +
////                "op.user_id=u.id) as o left join role as r on o.role_id=r.id";
//        try {
//            operationLogs = jdbcTemplate.query(sql, new OperationLogMapper());
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//        return operationLogs;
//    }
//
//    public List<OperationLog> getLogByPage(String queryCondition, String userParam, int pageNum, int pageSize) {
//        if (!userParam.equals("")) {
//            String userIdSQL = "select id from user where " + userParam;
//            String userId = jdbcTemplate.queryForObject(userIdSQL, String.class);
//            if (!queryCondition.equals("")) {
//                queryCondition += "and user_id='" + userId + "'";
//            } else {
//                queryCondition += "user_id='" + userId + "'";
//            }
//        }
//        List<OperationLog> operationLogs = null;
//        String sql = "";
//        if (queryCondition != "") {
//            String sq = "select * from operation_log where ";
//            String sq2 = "as op left join user as u on op.user_id=u.id) as o left join role as r on o.role_id=r.id";
////            String sql2=sq+queryCondition ;
////            System.out.println(sql2);
//            queryCondition = " " + queryCondition;
//            sql = "select o.id,o.op_time,o.op_type,o.remote_ip,o.content,o.user_id,o.role_id,o.login_name,o.user_name," +
//                    "o.password,o.organization,o.department,o.telephone,o.email,r.role_name,r.role_description from " +
//                    "(select op.id,op.op_time,op.op_type,op.remote_ip,op.content," +
//                    "op.user_id,u.role_id,u.login_name,u.user_name,u.password,u.organization,u.department,u.telephone,u.email" +
//                    " from (" + sq + queryCondition + ")" + " " + sq2 + " limit ?,?";
//        } else {
//            sql = "select o.id,o.op_time,o.op_type,o.remote_ip,o.content,o.user_id,o.role_id,o.login_name,o.user_name," +
//                    "o.password,o.organization,o.department,o.telephone,o.email,r.role_name,r.role_description from " +
//                    "(select op.id,op.op_time,op.op_type,op.remote_ip,op.content,op.user_id,u.role_id," +
//                    "u.login_name,u.user_name,u.password,u.organization,u.department,u.telephone,u.email " +
//                    "from operation_log as op left join user as u on op.user_id=u.id) as o left join role as r on o.role_id=r.id limit ?,?";
//        }
//        try {
//            operationLogs = jdbcTemplate.query(sql, new OperationLogMapper(), (pageNum - 1) * pageSize, pageSize);
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//        return operationLogs;
//    }
//
//    class OperationLogMapper implements RowMapper<OperationLog> {
//        @Override
//        public OperationLog mapRow(ResultSet rs, int rowNum) throws SQLException {
//
//            OperationLog operationLog = new OperationLog();
//            operationLog.setId(rs.getString("id"));
//            operationLog.setOperationTime(rs.getTimestamp("op_time"));
//            String operationType = rs.getString("op_type");
//
//            operationLog.setOpType(OperationType.stateOf(operationType));
//            operationLog.setRemoteIp(rs.getString("remote_ip"));
//            operationLog.setContent(rs.getString("content"));
//            User user = new User();
//            user.setId(rs.getString("user_id"));
//            user.setLoginName(rs.getString("login_name"));
//            user.setUserName(rs.getString("user_name"));
//            user.setPassword(rs.getString("password"));
//            user.setOrganization(rs.getString("organization"));
//            user.setDepartment(rs.getString("department"));
//            user.setTelephone(rs.getString("telephone"));
//            user.setEmail(rs.getString("email"));
//            Role role = new Role();
//            role.setId(rs.getString("role_id"));
//            role.setRolename(rs.getString("role_name"));
//            role.setRoleDescription(rs.getString("role_description"));
//            user.setUserRole(role);
//            operationLog.setUser(user);
//            return operationLog;
//        }
//    }
//
//}
