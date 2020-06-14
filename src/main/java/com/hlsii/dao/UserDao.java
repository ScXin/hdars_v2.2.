package com.hlsii.dao;
//
//import com.hlsii.entity.Role;
//import com.hlsii.entity.User;
//import com.hlsii.vo.ReturnCode;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.jdbc.core.RowMapper;
//import org.springframework.stereotype.Repository;
//import org.springframework.util.StringUtils;
//
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.UUID;
//
//
//@Repository
//public class UserDao {
//    @Autowired
//    private JdbcTemplate jdbcTemplate;
//
//    /*
//     get user by login name;
//     */
//
//    public UserDao() {
//
//    }
//
//    public ReturnCode saveOrUpdate(User user) {
//        if (StringUtils.isEmpty(user.getId())) {
//            user.setId(UUID.randomUUID().toString().replaceAll("-", ""));
//            boolean isSave = save(user);
//            if (isSave) {
//                return new ReturnCode(true, "Save User Success!");
//
//            } else {
//                return new ReturnCode(false, "Save user failed!");
//            }
//        } else {
//            boolean isSave = save(user);
//            if (isSave) {
//                return new ReturnCode(true, "Update User Success!");
//            } else {
//                return new ReturnCode(false, "Update User Failed!");
//            }
//        }
//    }
//
//
//    public List<User> getUserByCondition(String params) {
//        String sql = "";
//        if (params.equals("")) {
//            return getUserList();
//        } else {
//            sql = "select u.id,u.role_id,u.login_name,u.user_name," +
//                    "u.password,u.organization,u.department,u.telephone,u.email," +
//                    "r.role_name,r.role_description from (select * from user where " + params +
//                    ") as u left join role as r on u.role_id=r.id";
//        }
//        List<User> userList = new ArrayList<>();
//        try {
//            userList = jdbcTemplate.query(sql, new UserMapper());
//            return userList;
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//        return userList;
//    }
//
//    //The SQL has benn verified
//    public boolean save(User user) {
//
//        String id = user.getId();
//        String roleId = user.getUserRole().getId();
//        String loginName = user.getLoginName();
//        String userName = user.getUserName();
//        String pwd = user.getPassword();
//        String organization = user.getOrganization();
//        String department = user.getDepartment();
//        String telephone = user.getTelephone();
//        String emial = user.getEmail();
//        String sql = "INSERT INTO user (id, role_id, login_name, user_name, password,organization, department, telephone, email)" +
//                "VALUES('" + id + "','" + roleId + "','" + loginName + "','" + userName + "','" + pwd + "','" + organization + "','" +
//                department + "','" + telephone + "','" + emial + "')";
//
//        try {
//            jdbcTemplate.update(sql);
//            return true;
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//        return false;
//    }
//
//    //The SQL has been verified
//    public User getUserById(String id) {
//
//        String sql = "select u.id,u.role_id,u.login_name,u.user_name,u.password,u.organization,u.department,u.telephone,u.email,r.role_name,r.role_description from (select * from user where id='" + id + "' and del_flag is null) as u left join role as r on u.role_id=r.id";
//        User user = null;
//        try {
//            user = jdbcTemplate.queryForObject(sql, new UserMapper());
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//        return user;
//
//    }
//
//    /**
//     * 根据登录名获取User信息
//     *
//     * @param loginName
//     * @return
//     */
//
//    //The SQL has been verified
//    public User getUserByLoginName(String loginName) {
//
//        String sql = "select u.id,u.role_id,u.login_name,u.user_name,u.password,u.organization,u.department,u.telephone,u.email,r.role_name,r.role_description from (select * from user where login_name='" + loginName + "' and del_flag is null) as u left join role as r on u.role_id=r.id";
//        User user = null;
//        try {
//            user = jdbcTemplate.queryForObject(sql, new UserMapper());
//            return user;
//        } catch (Exception ex) {
//
//        }
//        return user;
//    }
//
//
//
//    /**
//     * 得到与用户列表
//     *
//     * @return
//     */
//    //The SQL has been verified
//    public List<User> getUserList() {
//        String sql = "select u.id,u.role_id,u.login_name,u.user_name,u.password,u.organization,u.department,u.telephone,u.email,r.role_name,r.role_description from (select * from user) as u left join role as r on u.role_id=r.id";
//        List<User> userList = new ArrayList<>();
//        try {
//            userList = jdbcTemplate.query(sql, new UserMapper());
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//        return userList;
//
//    }
//
//    /**
//     * 修改用户密码
//     *
//     * @param id
//     * @param pwd
//     * @return
//     */
//    public boolean updatePassword(String id, String pwd) {
//        String sql = "update user set password='" + pwd + "'where id='" + id + "'";
//        try {
//            jdbcTemplate.execute(sql);
//            return true;
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//        return false;
//    }
//
//    public boolean modifyPwd(String id, String newPwd) {
//        String sql = "update user set password='" + newPwd + "'where id='" + id + "'";
//        try {
//            jdbcTemplate.execute(sql);
//            return true;
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//        return false;
//    }
//
//
//    public boolean evict(User user) {
//        String sql = "delete from user where id='" + user.getId() + "'";
//        try {
//            jdbcTemplate.execute(sql);
//            return true;
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//        return false;
//    }
//
//
//    /**
//     * 修改用户的信息
//     *
//     * @param queryCondition
//     * @return
//     */
//    //The SQL has been verified
//    public boolean modifyUserInfo(String loginName, String queryCondition) {
//        String sql = "update user set " + queryCondition + " where login_name='" + loginName + "'";
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
//    /**
//     * 根据用户id删除用户
//     *
//     * @param id
//     * @return
//     */
//    public boolean delete(String id) {
//
//        String sql = "delete from user where id='" + id + "'";
//
//        try {
//            jdbcTemplate.execute(sql);
//            return true;
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//        return false;
//    }
//}
//
//
//class UserMapper implements RowMapper<User> {
//    @Override
//    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
//        User user = new User();
//        user.setId(rs.getString("id"));
//        user.setLoginName(rs.getString("login_Name"));
//        user.setUserName(rs.getString("user_Name"));
//        user.setPassword(rs.getString("password"));
//        user.setOrganization(rs.getString("organization"));
//        user.setDepartment(rs.getString("department"));
//        user.setTelephone(rs.getString("telephone"));
//        user.setEmail(rs.getString("email"));
//        Role role = new Role();
//        role.setId(rs.getString("role_id"));
//        role.setRolename(rs.getString("role_name"));
//        role.setRoleDescription(rs.getString("role_description"));
//        user.setUserRole(role);
//        return user;
//    }

//}

//import com.hlsii.entity.User;
//import com.hlsii.entity.User;
import com.commonuser.entity.User;
import com.hlsii.entity.UserRole;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
public class UserDao {


    @Autowired
    JdbcTemplate jdbcTemplate;


//    用户信息查询方法
    /**
     * 通过有用户名查询用户所有信息
     * @param username
     * @return
     */
    public User findByUsername(String username){
        String sql = "select * from user where username = '" + username + "'";
        try {
            return jdbcTemplate.queryForObject(sql, new userRowMapper());
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 通过用户id查询用户所有信息
     * @param id
     * @return
     */
    public User findById(int id){
        String sql = "select * from user where id = " + id;
        return jdbcTemplate.queryForObject(sql, new userRowMapper());

    }

    /**
     * 通过账号密码查询用户所有信息
     * @param username
     * @param pwd
     * @return
     */
    public User findByUsernameAndPwd(String username, String pwd){
        String sql = "select * from user where username = '" + username + "' and password = '" + pwd + "'";
        return jdbcTemplate.queryForObject(sql, new userRowMapper());
    }

    /**
     * 查询所有用户信息
     * @return
     */
    public List<User> findAllUser(){
        String sql = "select * from user";
        return jdbcTemplate.query(sql,new userRowMapper());
    }

//    用户信息管理方法
    /**
     * 插入新的用户信息:username作为salt
     * @param username
     * @param password
     * @param createTime
     * @return
     */
    public boolean insertUser(String username, String password, String createTime){
        String sql = "insert into user(username, password, salt, create_time) values(?,?,?,?)";
        try{
            String pwd = encrypt(password, username);
            jdbcTemplate.update(sql, username, pwd,username,createTime);
            return true;
        }catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 通过username更新存在的用户信息
     * @param username
     * @param password
     * @param createTime
     * @return
     */
    public boolean updateUser(String username, String password, String createTime){
        String sql = "update user set password = ?,salt = ?, create_time = ? where username = ?";
        try{
            String pwd = encrypt(password, username);
            int flag = jdbcTemplate.update(sql, pwd, username, createTime, username);
            if (flag > 0){
                return true;
            }else {
                return false;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 通过id删除用户信息
     * @param id
     * @return
     */
    public boolean deleteUser(int id){
        String sql = "delete from user where id = ?";
        try {
            jdbcTemplate.update(sql, id);
            return true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }




    /**
     * 插入用户角色关联内容
     * @param user_id
     * @param role_id
     * @return
     */
    public boolean addUR(int user_id, int role_id){
        String sql = "insert into user_role(user_id, role_id) values(?,?)";
        try {
            jdbcTemplate.update(sql, user_id, role_id);
            return true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }


    /**
     * 通过userid和roleid修改用户角色关联表
     * @param user_id
     * @param role_id_old
     * @param role_id_new
     * @return
     */
    public boolean updateUR(int user_id, int role_id_old, int role_id_new){
        String sql = "update user_role set role_id = ? where user_id = ? and role_id = ?";
        try {
            jdbcTemplate.update(sql, role_id_new, user_id, role_id_old);
            return true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }


    /**
     * 通过userid和roleid删除用户角色关联表
     * @param user_id
     * @param role_id
     * @return
     */
    public boolean deleteUR(int user_id, int role_id){
        String sql = "delete from user_role where user_id = ? and role_id = ?";
        try {
            int flag = jdbcTemplate.update(sql, user_id, role_id);
            if(flag > 0){
                return true;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }


// 加密方法

    /**
     * 加密函数:采用MD5，加盐，迭代2次
     * @return
     */
    public String encrypt(String pwd,String salt){
        Object s = ByteSource.Util.bytes(salt);
        SimpleHash simpleHash = new SimpleHash("MD5", pwd, s, 2);
        return simpleHash.toString();
    }

//   用来封装的类
    /**
     * 将数据库查询结果封装成实体类user对象的方法
     */
    class userRowMapper implements RowMapper<User>{
        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            User user = new User();
            user.setId(rs.getInt("id"));
            user.setUsername(rs.getString("username"));
            user.setPassword(rs.getString("password"));
            user.setSalt(rs.getString("salt"));
            user.setCreateTime(rs.getDate("create_time"));
            return user;
        }
    }

    /**
     * 将数据库查询结果封装成实体类userRole对象的方法
     */
    class urRowMapper implements RowMapper<UserRole>{
        @Override
        public UserRole mapRow(ResultSet rs, int rowNum) throws SQLException {
            UserRole userRole = new UserRole();
            userRole.setId(rs.getInt("id"));
            userRole.setUserId(rs.getInt("user_id"));
            userRole.setRoleId(rs.getInt("role_id"));
            userRole.setRemark(rs.getString("remarks"));
            return userRole;
        }
    }

}



