//package com.hlsii.dao;
//
//import com.hlsii.entity.ProfileGroup;
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
//import java.util.List;
//
//@Repository
//public class ProfileGroupDao {
//
//    @Autowired
//    private JdbcTemplate jdbcTemplate;
//
//    public List<ProfileGroup> getProfileGroups(QueryProfile profile) {
//
//        return getProfileGroups(profile.getId());
//    }
//
//    public List<ProfileGroup> getProfileGroups(String profileId) {
////        String sql = "select * from prof_group where prof_id='" + profileId + "'";
//
//        String sql = "select pqe.id,pqe.group_name,pqe.prof_id,pqe.logarithm,pqe.pvs,pqe.user_id,pqe.update_time,pqe.prof_name," +
//                "pqe.role_id,pqe.login_name,pqe.user_name,pqe.password,pqe.organization,pqe.department,pqe.telephone,pqe.email," +
//                "r.role_name,r.role_description from (select pq.id,pq.group_name,pq.prof_id,pq.logarithm,pq.pvs,pq.user_id," +
//                "pq.update_time,pq.prof_name,u.role_id,u.login_name,u.user_name,u.password,u.organization,u.department,u.telephone,u.email from " +
//                "(select p.id,p.group_name,p.prof_id,p.logarithm,p.pvs,q.user_id,q.update_time,q.prof_name from " +
//                "(select * from prof_group where prof_id='" + profileId + "' and del_flag is null) as p left join query_prof as q on p.prof_id=q.id) as pq " +
//                "left join user as u on  pq.user_id=u.id) as pqe left join role as r on pqe.role_id=r.id";
//
//
//        List<ProfileGroup> profileGroupList = null;
//
//        try {
//            profileGroupList = jdbcTemplate.query(sql, new ProfileGroupRowMapper());
//
//        } catch (Exception ex) {
//        }
//        System.out.println(profileGroupList==null);
//        return profileGroupList;
//    }
//
//    /**
//     * CREATE TABLE `prof_group` (
//     * `id` varchar(64) NOT NULL,
//     * `group_name` varchar(64) NOT NULL,
//     * `prof_id` varchar(64) DEFAULT NULL,
//     * `logarithm` tinyint(1) DEFAULT 0,
//     * `pvs` longtext NOT NULL,
//     * `del_flag` varchar(1) DEFAULT NULL,
//     * PRIMARY KEY (`id`),
//     * FO
//     *
//     * @param profileGroup
//     * @return
//     */
//    public boolean saveOrUpdate(ProfileGroup profileGroup) {
//
//        String sql = "insert into prof_group values('"
//                + profileGroup.getId() + "', '"
//                + profileGroup.getGroupName() + "', '"
//                + profileGroup.getProfile().getId() + "', '"
//                + profileGroup.getLogarithm() + "','"
//                + profileGroup.getPvs() + "','"
//                + profileGroup.getDelFlag() + "')";
//        int row = jdbcTemplate.update(sql);
//        if (row != 0) {
//            return true;
//        }
//        return false;
//    }
//
//    public boolean delete(String groupId) {
//
//        String sql = "update prof_group set del_flag=1 where id='" + groupId + "'";
//        int row = jdbcTemplate.update(sql);
//        if (row != 0) {
//            return true;
//        }
//        return false;
//    }
//
//    public boolean delete(ProfileGroup profileGroup) {
//        String groupId = profileGroup.getId();
//        String sql = "update prof_group set del_flag=1 where id='" + groupId + "'";
//        int row = jdbcTemplate.update(sql);
//        if (row != 0) {
//            return true;
//        }
//        return false;
//    }
//
//    public boolean delete(QueryProfile profile){
//        String id=profile.getId();
//        String sql="update query_prof set del_flag=1 where id='"+id+"'";
//        int row=jdbcTemplate.update(sql);
//        if(row!=0){
//            return true;
//        }
//        return false;
//    }
//    static   class ProfileGroupRowMapper implements RowMapper<ProfileGroup> {
//
//        @Override
//        public ProfileGroup mapRow(ResultSet rs, int rowNum) throws SQLException {
//            ProfileGroup profileGroup = new ProfileGroup();
//            QueryProfile queryProfile = new QueryProfile();
//            queryProfile.setId(rs.getString("prof_id"));
//            queryProfile.setProfName(rs.getString("prof_name"));
//            queryProfile.setUpdateTime(rs.getTime("update_time"));
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
//            profileGroup.setProfile(queryProfile);
//            profileGroup.setGroupName(rs.getString("group_name"));
//            profileGroup.setLogarithm(rs.getBoolean("logarithm"));
//            profileGroup.setPvs(rs.getString("pvs"));
//            return profileGroup;
//        }
//
//    }
//}
