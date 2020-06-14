package com.hlsii.service;//package com.hlsii.service;
//
//import com.hlsii.entity.User;
//import com.hlsii.security.UserRealm;
//import com.hlsii.util.SystemUtil;
//import com.hlsii.util.WebUtil;
//import org.apache.shiro.SecurityUtils;
//import org.apache.shiro.authc.AuthenticationException;
//import org.apache.shiro.authc.UsernamePasswordToken;
//import org.apache.shiro.subject.Subject;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import java.util.UUID;
//
///**
// * Authentication service
// *
// */
//@Component
//public class AuthService {
//    private static final String GUEST = "guest";
//
//    @Autowired
//    private UserService userService;
//
//    /**
//     * Login for subject use login name and password, for white IP just login as guest.
//     *
//     * @param userName - user login name (can't be guest)
//     * @param password - password (plain text)
//     * @param ip - remote ip
//     * @param whiteip -whether remote is white IP
//     *
//     * @throws AuthenticationException - login failed will throw the exception
//     */
//    public boolean login(String userName, String password, String ip, boolean whiteip) throws AuthenticationException {
//        String name = userName;
//        String pwd = SystemUtil.encryptPassword(password);
//        if (whiteip) {
//            // For white IP, login as guest.
//            User guest = userService.getUserByLoginName(GUEST);
//            if (guest != null) {
//                name = GUEST;
//                pwd = guest.getPassword();
//            }
//        }
//        else {
//            if (GUEST.equals(name)) {
//                // Not allow to use guest as name to login, if so use a random
//                // password so that the login will be failed.
//                pwd = UUID.randomUUID().toString();
//            }
//        }
//        UsernamePasswordToken token = new UsernamePasswordToken(name, pwd);
//        Subject subject = SecurityUtils.getSubject();
//        subject.login(token);
//        // When login successfully, logout and re-login so that user role can be reloaded.
//        subject.logout();
//        subject.login(token);
//        UserRealm.setSession(WebUtil.REMOTE_IP_KEY, ip);
//        return true;
//    }
//
//    /**
//     * Logout the subject
//     *
//     */
//    public void logout() {
//        SecurityUtils.getSubject().logout();
//    }
//}
