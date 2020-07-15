package com.hlsii.service;


import com.commonuser.entity.Role;
import com.commonuser.entity.User;
import com.hlsii.dao.RoleDao;
import com.hlsii.dao.UserDao;
//import com.hlsii.entity.Role;
//import com.hlsii.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private UserDao userDao;


    /**
     * 通过用户名获取有用户、角色和权限所有信息
     * @param username
     * @return
     */
    @Override
    public User findAllUserInfoByUsername(String username) {
        User user = userDao.findByUsername(username);
        if (user == null) {
            return null;
        }
        Role role = roleDao.findRoleListByUserId(user.getId());
        user.setRole(role);
        return user;
    }

    /**
     * 通过用户id仅获取用户信息
     * @param userId
     * @return
     */
    @Override
    public User findSimpleUserInfoById(int userId) {
        return userDao.findById(userId);
    }

    /**
     * 通过用户名仅获取用户信息
     * @param username
     * @return
     */
    @Override
    public User findSimpleUserInfoByUsername(String username) {
        return userDao.findByUsername(username);
    }
}
