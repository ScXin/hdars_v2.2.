package com.hlsii.service;
//
//import com.hlsii.dao.UserDao;
//import com.hlsii.entity.User;
//import com.hlsii.util.SystemUtil;
//import com.hlsii.vo.ReturnCode;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.util.StringUtils;
//
//import java.util.List;
////import org.apache.commons.lang3.StringUtils;
//
//@Service
//@Transactional(readOnly = true)
//public class UserService {
//
//    @Autowired
//    private UserDao userDao;
//
//
//    @Transactional(readOnly = false)
//    public ReturnCode save(User user) {
//        if (StringUtils.isEmpty(user.getId())) {
//            user.setPassword(SystemUtil.defaultPassword());
//        } else {
//            User existingUser = userDao.getUserById(user.getId());
//            user.setDepartment(existingUser.getDelFlag());
//            user.setPassword(existingUser.getPassword());
//            boolean isEvict = userDao.evict(existingUser);
//            if (!isEvict) {
//                return new ReturnCode(false, "Failed to delete user with the same id");
//            }
//        }
//        return userDao.saveOrUpdate(user);
//    }
//
//    @Transactional(readOnly = false)
//    public boolean evict(String id) {
//        return userDao.delete(id);
//    }
//
//    /**
//     * get User by Id
//     *
//     * @param id
//     * @return
//     */
//
//    public User getUserById(String id) {
//        return userDao.getUserById(id);
//    }
//
//    /**
//     * get User informaion by login_name
//     *
//     * @param loginName
//     * @return
//     */
//    public User getUserByLoginName(String loginName) {
//        return userDao.getUserByLoginName(loginName);
//    }
//
//
//    public User getUserByUserName(String userName) {
//
//
//        return userDao.getUserByUserName(userName);
//    }
//
//
//    public List<User> getUserByCondition(String params) {
//
//        return userDao.getUserByCondition(params);
//
//
//    }
//
//
//    @Transactional(readOnly = false)
//    public boolean modifyUserInfo(String loginName, String queryCondition) {
//        return userDao.modifyUserInfo(loginName, queryCondition);
//    }
//
//    /**
//     * Reset user password to default password
//     *
//     * @param id - user id
//     */
//    @Transactional(readOnly = false)
//    public boolean resetPassword(String id) {
//        return userDao.updatePassword(id, SystemUtil.defaultPassword());
//    }
//
//
//    @Transactional(readOnly = false)
//    public boolean modifyPwd(String id, String newPwd) {
//        return userDao.modifyPwd(id, newPwd);
//    }
//
//    public List<User> getUserList() {
//        return userDao.getUserList();
//    }
//}

//import com.hlsii.entity.User;

import com.commonuser.entity.User;

public interface UserService {

    /**
     * 根据用户名获取全部用户信息包括角色、权限
     * @param username
     * @return
     */
    User findAllUserInfoByUsername(String username);


    /**
     * 根据id获取用户基本信息
     * @param userId
     * @return
     */
    User findSimpleUserInfoById(int userId);


    /**
     * 根据用户名获取用户基本信息
     * @param username
     * @return
     */
    User findSimpleUserInfoByUsername(String username);

}
