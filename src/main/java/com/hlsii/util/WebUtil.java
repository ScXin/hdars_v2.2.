//package com.hlsii.util;
//
//import com.hlsii.entity.User;
//import com.hlsii.service.UserService;
//import org.apache.shiro.SecurityUtils;
//import org.apache.shiro.session.Session;
//import org.apache.shiro.subject.Subject;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.stereotype.Service;
//import org.springframework.web.bind.annotation.PathVariable;
//
///**
// * Web utility
// */
//
//@Service
//public class WebUtil{
//    public static final String USER_INFO_KEY = "USER_INFO";
//    public static final String REMOTE_IP_KEY = "REMOTE_IP";
//
//
//    @Autowired
//    private UserService userService;
//
//    private WebUtil() {
//    }
//
//    /**
//     * Get current session login user
//     *
//     * @return - The login user, null means no user login.
//     */
//    public User getCurrentLoginUser() {
////        Session session = getSession();
////        if (session != null) {
////            return (User)session.getAttribute(USER_INFO_KEY);
////        }
////        return new User();
//
////        UserService userService = new UserService();
//        User user = userService.getUserByLoginName("admin");
//        return user;
//    }
//
//    /**
//     * Get current session login user IP
//     *
//     * @return - The login user IP, null means no user login.
//     */
//    public static String getLoginUserIP() {
//        Session session = getSession();
//        if (session != null) {
//            return (String) session.getAttribute(REMOTE_IP_KEY);
//        }
//        return "";
//    }
//
//    /**
//     * Get current session ID
//     *
//     * @return - current session ID
//     */
//    public static String getSessionId() {
//        String id = "";
//        Subject subject = SecurityUtils.getSubject();
//        if (subject != null && subject.getSession() != null) {
//            return (String) subject.getSession().getId();
//        }
//        return id;
//    }
//
//    /**
//     * Get current session
//     *
//     * @return - current session
//     */
//    private static Session getSession() {
//        Subject subject = SecurityUtils.getSubject();
//        if (subject != null) {
//            return subject.getSession();
//        }
//        return null;
//    }
//}
