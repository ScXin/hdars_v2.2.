//package com.hlsii.dao;
//
//import com.hlsii.entity.QueryProfile;
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
//import java.util.Date;
//import java.util.List;
//
////import java.sql.Date;
///**
// * query_prof table
// */
////CREATE TAB
////        LE `query_prof` (
////        `id` varchar(64) NOT NULL,
////        `profName` varchar(255) DEFAULT NULL,
////        `user_id` varchar(64) NOT NULL,
////        `update_time` timestamp not null default current_timestamp,
////        `del_flag` varchar(1) DEFAULT NULL,
////        PRIMARY KEY (`id`),
////        FOREIGN KEY(`user_id`) REFERENCES `user` (`id`),
////        INDEX `update_time` (`update_time`)
////        );
//
///**
// * user table
// */
////CREATE TABLE `user` (
////        `id` VARCHAR(64) NOT NULL,
////        `role_id` VARCHAR(64) NOT NULL,
////        `login_name` VARCHAR(20) NOT NULL,
////        `user_name` VARCHAR(64) NOT NULL,
////        `password` VARCHAR(32) NOT NULL,
////        `organization` VARCHAR(100),
////        `department` VARCHAR(50),
////        `telephone` VARCHAR(20),
////        `email` VARCHAR(100),
////        `del_flag` CHAR(1) NULL DEFAULT NULL,
////        PRIMARY KEY (`id`),
////        FOREIGN KEY(`role_id`) references role(`id`),
////        INDEX `login_name` (`login_name`),
////        INDEX `del_flag` (`del_flag`)
////        );
//
//
////    @SuppressWarnings("serial")
//@Repository
//public class QueryProfileDao {
//
//    @Autowired
//    private JdbcTemplate jdbcTemplate;
//
//    public List<QueryProfile> getUserProfiles(User user) {
//        return getUserProfiles(user.getId());
//    }
//
//
//    //The SQL has been tested
//    public List<QueryProfile> getUserProfiles(String userId) {
//        String sql = "select qe.id,qe.prof_Name,qe.user_id,qe.update_time,qe.role_id," +
//                "qe.login_name,qe.user_name,qe.password,qe.organization,qe.department," +
//                "qe.telephone,qe.email,r.role_name,r.role_description from (select q.id,q.prof_name,q.user_id,q.update_time,u.role_id,u.login_name,u.user_name,u.password," +
//                "u.organization,u.department,u.telephone,u.email from (select * from query_prof where user_id='" + userId + "' and del_flag is null) as q left join " +
//                "user as u on q.user_id=u.id) as qe left join role as r on qe.role_id=r.id";
//
//        List<QueryProfile> queryProfileList = null;
//        try {
//            queryProfileList = jdbcTemplate.query(sql, new QueryProfileMapper());
//        } catch (Exception ex) {
//
//        }
//        return queryProfileList;
//    }
//
//    //The SQL has been tested
//    public boolean saveOrUpdate(QueryProfile profile) {
//
//        String profile_id = profile.getId();
//        String user_id = profile.getUser().getId();
//        String profName = profile.getProfName();
//        Date date = profile.getUpdateTime();
////        String profile_id = "2";
////        String user_id = "666666666666666666666666666666";
////        String profName1 = "test";
////        Date date = new Date();
////        Timestamp ts=new Timestamp(new Date().getTime());
//        SimpleDateFormat sp = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
//        String updateTime = sp.format(date);
////        Timestamp updateTime2 = Timestamp.valueOf(updateTime);
////        System.out.println(ts);
////        String th=ts.toString();
////        System.out.println(ts);
////        String updateTime3=TO_TIMESTAMP(updateTime);
////        String st = "2020-12-12 02:23:25.0009";
//        String sql = "insert into query_prof(id,profName,user_id,update_time)values" +
//                "('" + profile_id + "','" + profName + "','" + user_id + "','" + updateTime + "" + "')";
//        try {
//            jdbcTemplate.update(sql);
//            return true;
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//        return false;
//    }
//
//
//    //The SQl has benn tested
//    public QueryProfile getProfileById(String profId) {
//        String sql = "select qe.id,qe.prof_name,qe.user_id,qe.update_time,qe.role_id," +
//                "qe.login_name,qe.user_name,qe.password,qe.organization,qe.department," +
//                "qe.telephone,qe.email,r.role_name,r.role_description from (select q.id,q.prof_name,q.user_id,q.update_time,u.role_id,u.login_name,u.user_name,u.password," +
//                "u.organization,u.department,u.telephone,u.email from (select * from query_prof where id='" + profId + "' and del_flag is null) as q left join " +
//                "user as u on q.user_id=u.id) as qe left join role as r on qe.role_id=r.id";
//        QueryProfile queryProfile = null;
//        try {
//            queryProfile = jdbcTemplate.queryForObject(sql, new QueryProfileMapper());
//            return queryProfile;
//        } catch (Exception ex) {
//
//        }
//        return queryProfile;
//    }
//
//    class QueryProfileMapper implements RowMapper<QueryProfile> {
//
//        @Override
//        public QueryProfile mapRow(ResultSet rs, int rowNum) throws SQLException {
//
//            QueryProfile queryProfile = new QueryProfile();
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
//            queryProfile.setUser(user);
//            queryProfile.setId(rs.getString("id"));
//            queryProfile.setProfName(rs.getString("prof_name"));
//            queryProfile.setUpdateTime(rs.getTimestamp("update_time"));
//
//            return queryProfile;
//        }
//
//    }
//}
//
