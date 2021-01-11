package com.hlsii.config;

//import com.hlsii.entity.Permission;
//import com.hlsii.entity.User;
//import com.hlsii.entity.Role;

import com.commonuser.entity.Permission;
import com.commonuser.entity.Role;
import com.commonuser.entity.User;
import com.hlsii.service.UserService;
import com.hlsii.service.UserServiceImpl;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.authz.SimpleRole;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * 自定义realm
 */
public class CustomRealm extends AuthorizingRealm {

    @Autowired
    private UserService userService;

    /**
     * 进行权限校验的时候回调用
     *
     * @param principals
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
//        System.out.println("授权 doGetAuthorizationInfo");

        User newUser = (User) principals.getPrimaryPrincipal();
        //        System.out.println("授权方法doGetAuthorizationInfo");

        User user = userService.findAllUserInfoByUsername(newUser.getUsername());
        if (user == null) {
            return null;
        }
//        List<Role> roleList = user.getRoleList();
        Role role = user.getRole();
        List<String> stringRoleList = new ArrayList<>();
        List<String> stringPermissionList = new ArrayList<>();
        stringRoleList.add(role.getName());
        List<Permission> permissionList = role.getPermissionList();
//        for (Permission permission : permissionList) {
//                if (permission != null) {
//                    stringPermissionList.add(permission.getName());
//                }
//            }
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        simpleAuthorizationInfo.addRoles(stringRoleList);
        simpleAuthorizationInfo.addStringPermissions(stringPermissionList);

        return simpleAuthorizationInfo;
    }


    /**
     * 用户登录的时候会调用
     *
     * @param token
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {

        System.out.println("认证 doGetAuthenticationInfo");

        //从token获取用户信息，token代表用户输入
        String userName = (String) token.getPrincipal();


//获取salt
        User user = userService.findAllUserInfoByUsername(userName);

//取密码
        String pwd = user.getPassword();

        ByteSource salt = ByteSource.Util.bytes(userName);

        if(pwd == null || "".equals(pwd)){
            return null;
        }

        return new SimpleAuthenticationInfo(user, pwd, salt, this.getClass().getName());
    }
}
