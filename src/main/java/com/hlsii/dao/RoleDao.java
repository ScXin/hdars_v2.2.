package com.hlsii.dao;

//import com.hlsii.entity.Role;
import com.commonuser.entity.Role;
//import com.hlsii.entity.User;
//import com.hlsii.vo.RoleType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
//import org.springframework.stereotype.Repository;

//import javax.jws.soap.SOAPBinding;
//import java.sql.JDBCType;
import java.sql.ResultSet;
import java.sql.SQLException;
//import java.util.ArrayList;
import java.util.List;
//
//@Repository
//public class RoleDao {
//
//
//    @Autowired
//    private JdbcTemplate jdbcTemplate;
//
//    /**
//     * 通过role名称拿到role Id
//     *
//     * @param roleName
//     * @return
//     */
//    public String getIdByRoleName(String roleName) {
//        String sql = "select id from role where role_name='" + roleName + "'";
//        String roleId = "";
//        try {
//            roleId = jdbcTemplate.queryForObject(sql, String.class);
//            return roleId;
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//        return roleId;
//    }
//
//
//    /**
//     * 获取所有的用户类型列表
//     *
//     * @return
//     */
//    public List<RoleType> getAll() {
//        String sql = "select distinct id,role_name from role";
//        List<RoleType> roleList = new ArrayList<>();
//        try {
//            roleList = jdbcTemplate.query(sql, new RoleTypeMapper());
//            return roleList;
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//        return roleList;
//    }
//
//    class RoleTypeMapper implements RowMapper<RoleType> {
//
//        @Override
//        public RoleType mapRow(ResultSet rs, int rowNum) throws SQLException {
//            RoleType roleType = new RoleType();
//            roleType.setRoleId(rs.getString("id"));
//            roleType.setRoleName(rs.getString("role_name"));
//            return roleType;
//        }
//    }
//}

@Component
public class RoleDao {
    @Autowired
    PermissionDao permissionDao;

    @Autowired
    JdbcTemplate jdbcTemplate;

    /**
     * 通过userid查询对应的角色信息和权限信息
     * @param userId
     * @return
     */
    public Role findRoleListByUserId(int userId){
        String sql = "select ur.role_id as id, r.name as name, r.description as description" +
                " from user_role ur" +
                " left join role r on ur.role_id = r.id" +
                " where ur.user_id =" + userId;
        return jdbcTemplate.queryForObject(sql, new roleRowMapper1());
    }


    /**
     * 通过userid查询对应的角色信息
     * @param userId
     * @return
     */
    public List<Role> findSimpleRoleListByUserId(int userId){
        String sql = "select ur.role_id as id, r.name as name, r.description as description" +
                " from user_role ur" +
                " left join role r on ur.role_id = r.id" +
                " where ur.user_id =" + userId;
        return jdbcTemplate.query(sql, new roleRowMapper2());
    }


    /**
     * 获取所有角色信息
     * @return
     */
    public List<Role> findAllRole(){
        String sql = "select * from role";
        return jdbcTemplate.query(sql, new roleRowMapper2());
    }


    public Role findRoleByRolename(String rolename){
        String sql = "select * from role where role.name = '" + rolename + "'";
        return jdbcTemplate.queryForObject(sql, new roleRowMapper2());
    }

    /**
     * 将数据库查询结果封装成实体类的role对象(包含权限信息)
     */
//    class roleRowMapper1 implements RowMapper<Role>{
//        @Override
//        public Role mapRow(ResultSet rs, int rowNum) throws SQLException {
//            Role role = new Role();
//            role.setId(rs.getInt("id"));
//            role.setName(rs.getString("name"));
//            role.setDescription(rs.getString("description"));
//            role.setPermissionList(permissionDao.findPermissionListByRoleId(rs.getInt("id")));
//            return role;
//        }
//    }
   static  class roleRowMapper1 implements RowMapper<Role>{
        @Override
        public Role mapRow(ResultSet rs, int rowNum) throws SQLException {
            Role role = new Role();
            role.setId(rs.getInt("id"));
            role.setName(rs.getString("name"));
            role.setDescription(rs.getString("description"));
//            role.setPermissionList(permissionDao.findPermissionListByRoleId(rs.getInt("id")));
            return role;
        }
    }

    /**
     * 将数据库查询结果封装成实体类的role对象（只含有角色信息）
     */
    class roleRowMapper2 implements RowMapper<Role>{
        @Override
        public Role mapRow(ResultSet rs, int rowNum) throws SQLException {
            Role role = new Role();
            role.setId(rs.getInt("id"));
            role.setName(rs.getString("name"));
            role.setDescription(rs.getString("description"));
            return role;
        }
    }
}

