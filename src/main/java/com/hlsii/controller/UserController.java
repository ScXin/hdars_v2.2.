package com.hlsii.controller;

//import com.alibaba.fastjson.JSONPathException;
//
//import com.hlsii.entity.User;
//import com.hlsii.service.RoleService;
//import com.hlsii.service.UserService;
//import com.hlsii.service.WhiteIPService;
//import com.hlsii.util.SystemUtil;
//import com.hlsii.util.WebUtil;
//import com.hlsii.vo.JsonData;
//import com.hlsii.vo.ReturnCode;
//import com.hlsii.vo.RoleType;
//import com.hlsii.vo.UserQuery;
//import io.swagger.annotations.ApiOperation;
//import org.apache.commons.lang3.StringUtils;
//import org.apache.shiro.SecurityUtils;
//import org.apache.shiro.authc.UsernamePasswordToken;
//import org.apache.shiro.subject.Subject;
//import org.jdom2.JDOMException;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.*;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
////import org.springframework.web.bind.annotation.RequestBody;
//
///**
// * @author Shangcong Xin
// * @date 4/1/20
// */
//
//@RestController
//@RequestMapping("/hdars")
//public class UserController {
//
//    @Autowired
//    private UserService userService;
//    @Autowired
//    private RoleService roleService;
//
//    @Autowired
//    private WhiteIPService whiteIPService;
//
//
//    @Autowired
//    private WebUtil webUtil;
//
//
//    @ApiOperation("根据用户的id获取用户的信息")
//    @GetMapping("/user/getUser/{id}")
//    public User getUserById(@PathVariable("id") String id) {
//        User entity = null;
//
//        if (!StringUtils.isEmpty(id)) {
//            entity = userService.getUserById(id);
//        }
//        if (entity == null) {
//            entity = new User();
//        }
//        return entity;
//    }
//
//
//    @ApiOperation("根据登录名获取User的接口")
//    @GetMapping("/user/getUser")
//    public List<User> getUserByloginName(@RequestParam(value = "loginName", required = false) String loginName,
//                                         @RequestParam(value = "userName", required = false) String userName,
//                                         @RequestParam(value = "organization", required = false) String organization,
//                                         @RequestParam(value = "tel", required = false) String tel) {
//        List<User> userList = null;
//        String queryCondition = "";
//
//        if (loginName != null) {
//            queryCondition += "login_name='" + loginName + "'";
//        }
//        if (userName != null) {
//            if (queryCondition != "") {
//                queryCondition += " and user_name='" + userName + "'";
//            } else {
//                queryCondition += "user_name='" + userName + "'";
//            }
//        }
//        if (organization != null) {
//            if (queryCondition != "") {
//                queryCondition += " and organization='" + organization + "'";
//            } else {
//                queryCondition += "organization='" + organization + "'";
//            }
//        }
//        if (tel != null) {
//            if (queryCondition != "") {
//                queryCondition += " and telephone='" + tel + "'";
//            } else {
//                queryCondition += "telephone='" + tel + "'";
//            }
//        }
//
//        userList = userService.getUserByCondition(queryCondition);
//
//        return userList;
//    }
//
//
//    @GetMapping("/user/getUser/userlist")
//    public List<User> getAllUser() {
//
//        List<User> userList = userService.getUserList();
////        recordUserLogService.logOperation(OperationType.VIEW_USER_LIST, null);
//
//        return userList;
//    }
//
//    @ApiOperation("根据登录用户名修改密码")
//    @PutMapping("/user/changePwd/{oldPwd}/{newPwd}")
//    public Boolean changePwd(@PathVariable("oldPwd") String oldPwd, @PathVariable("newPwd") String newPwd) {
//        User user = webUtil.getCurrentLoginUser();
//
//
//        if (StringUtils.isNotBlank(oldPwd) && StringUtils.isNotBlank(newPwd)) {
//
////            System.out.println(SystemUtil.validatePassword(oldPwd, user.getPassword()));
//            if (SystemUtil.validatePassword(oldPwd, user.getPassword())) {
//                if (userService.modifyPwd(user.getId(), newPwd)) {
////                    recordUserLogService.logOperation(OperationType.CHANGE_PWD, user.getLoginName());
//                    return true;
//                } else {
//                    return false;
//                }
//            } else {
//                return false;
//            }
//        }
//        return false;
//    }
//
//    @PostMapping("/user/saveUser")
//    public ReturnCode saveUser(@RequestBody User user) throws JDOMException, IOException {
//
//        if (!StringUtils.isEmpty(user.getId()) || userService.getUserByLoginName(user.getLoginName()) != null) {
//            String messages = "The login name is already existed.";
//            return new ReturnCode(false, messages);
//        }
//        return userService.save(user);
//    }
//
//    @DeleteMapping("/user/deleteUser/{id}")
//    public String deleteUser(@PathVariable("id") String id) {
//        User user = userService.getUserById(id);
//
//
//        if ("admin".equals(user.getLoginName())) {
//            return "[Failed] Can't delete admin user!";
//        } else {
//            boolean isEvict = userService.evict(user.getId());
//            if (isEvict) {
//                return "[Success] The user is deleted!";
//            } else {
//                return "[Failed] The delete operation is failed!";
//            }
//        }
//    }
//
//
//    @ApiOperation("修改用户信息")
//    @PutMapping("/user/modifyUserInfo")
//    public boolean modifyUserInfo(@RequestParam(value = "loginName") String loginName,
//                                  @RequestParam(value = "userName") String userName,
//                                  @RequestParam(value = "userType") String userType,
//                                  @RequestParam(value = "organization", required = false) String organization,
//                                  @RequestParam(value = "department", required = false) String department,
//                                  @RequestParam(value = "telephone", required = false) String telephone,
//                                  @RequestParam(value = "email", required = false) String email) {
//
//        if (loginName == null) {
//            return false;
//        }
//        String roleId = roleService.getIdByRoleName(userType);
//        String queryCondition = "";
//        queryCondition += "role_id='" + roleId + "'";
//        queryCondition += ",user_name='" + userName + "'";
//        queryCondition += ",organization='" + organization + "'";
//        queryCondition += ",department='" + department + "'";
//        queryCondition += ",telephone='" + telephone + "'";
//        queryCondition += ",email='" + email + "'";
//
//        System.out.println("queryCondition==" + queryCondition);
//        return userService.modifyUserInfo(loginName, queryCondition);
//
//    }
//
//    /**
//     * 重设密码
//     *
//     * @return
//     */
//    @ApiOperation("将用户的密码重置为默认密码")
//    @PutMapping("/user/resetPwd/{userId}")
//    public boolean resetPwd(@PathVariable("userId") String userId) {
//        if (StringUtils.isNotBlank(userId)) {
//            if (userService.resetPassword(userId)) {
//                return true;
//            } else {
//                return false;
//            }
//        }
//        return false;
//    }
//
//    @ApiOperation("获得所有的用户类型")
//    @GetMapping("/user/getUserTypeList")
//    public List<RoleType> getAllUserType() {
//        return roleService.getAll();
//    }
//
//
//    /**
//     * 登录接口
//     *
//     * @param userQuery
//     * @param request
//     * @param response
//     * @return
//     */
//    @PostMapping("/user/login")
//    public JsonData login(@RequestBody UserQuery userQuery, HttpServletRequest request, HttpServletResponse response) {
//
//        Subject subject = SecurityUtils.getSubject();
//        Map<String, Object> info = new HashMap<>();
//        try {
//            UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(userQuery.getName(), userQuery.getPwd());
//
//            subject.login(usernamePasswordToken);
//
//            info.put("msg", "登录成功");
//            info.put("session_id", subject.getSession().getId());
//
//            return JsonData.buildSuccess(info);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//
//            return JsonData.buildError("账号或者密码错误");
//
//        }
//    }
//
//    @RequestMapping("/user/need_login")
//    public JsonData needLogin() {
//
//        return JsonData.buildSuccess("温馨提示：请使用对应的账号登录", -2);
//
//    }
//
//
//    @RequestMapping("/user/not_permit")
//    public JsonData notPermit() {
//
//        return JsonData.buildSuccess("温馨提示：拒绝访问，没权限", -3);
//    }
//
//    @RequestMapping("/user/logout")
//    public JsonData findMyPlayRecord() {
//
//        Subject subject = SecurityUtils.getSubject();
//
//        if (subject.getPrincipals() != null) {
//        }
//        SecurityUtils.getSubject().logout();
//
//        return JsonData.buildSuccess("logout成功");
//
//    }
//
//}

