package com.hlsii.security;//package com.hlsii.security;
//
//import com.hlsii.entity.User;
//import org.apache.commons.lang3.StringUtils;
//import org.apache.shiro.SecurityUtils;
//import org.apache.shiro.authc.*;
//import org.apache.shiro.authz.AuthorizationException;
//import org.apache.shiro.authz.AuthorizationInfo;
//import org.apache.shiro.authz.SimpleAuthorizationInfo;
//import org.apache.shiro.realm.AuthorizingRealm;
//import org.apache.shiro.session.Session;
//import org.apache.shiro.subject.PrincipalCollection;
//import org.apache.shiro.subject.Subject;
//import org.springframework.beans.factory.annotation.Autowired;
//import com.hlsii.service.UserService;
//import com.hlsii.util.WebUtil;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class UserRealm extends AuthorizingRealm {
//    @Autowired
//    private UserService userService;
//
//    @Override
//    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
//        String username = (String) super.getAvailablePrincipal(principalCollection);
//        if (StringUtils.isNotEmpty(username)) {
//            User user = userService.getUserByLoginName(username);
//            if (user == null) {
//                throw new AuthorizationException();
//            }
//            // Set roles and permission for user
//            SimpleAuthorizationInfo simpleAuthorInfo = new SimpleAuthorizationInfo();
//            List<String> roleList = new ArrayList<>();
//            roleList.add(user.getUserRole().getRoleName());
//            List<String> permissionList = new ArrayList<>();
//            simpleAuthorInfo.addRoles(roleList);
//            simpleAuthorInfo.addStringPermissions(permissionList);
//            return simpleAuthorInfo;
//        }
//        return null;
//    }
//
//    @Override
//    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken)
//            throws AuthenticationException {
//
//        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
//        User user = userService.getUserByLoginName(token.getUsername());
//        if (null != user) {
//            setSession(WebUtil.USER_INFO_KEY, user);
//            return new SimpleAuthenticationInfo(user.getLoginName(), user.getPassword(),
//                 user.getUserName());
//        }
//        return null;
//    }
//
//    /**
//     * Add some info to ShiroSession, so it can be used in Controller.
//     * Use HttpSession.getAttribute(key) to get the data.
//     */
//    public static void setSession(String key, Object value) {
//        Subject currentUser = SecurityUtils.getSubject();
//        if (null != currentUser) {
//            Session session = currentUser.getSession();
//            if (null != session) {
//                session.setAttribute(key, value);
//            }
//        }
//    }
//}
