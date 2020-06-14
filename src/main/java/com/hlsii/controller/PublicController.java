package com.hlsii.controller;


import com.hlsii.vo.JsonData;
import com.hlsii.vo.UserQuery;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/pub")
//@CrossOrigin
public class PublicController {

    @ApiOperation(value = "没有登陆时触发的接口")
    @GetMapping("/needLogin")
    public JsonData needLogin(){
        return JsonData.buildSuccess("温馨提示：请使用账号登陆", -1);
    }

    @ApiOperation(value = "没有权限时触发的接口")
    @GetMapping("/notPermit")
    public JsonData notPermit(){
        return JsonData.buildSuccess("温馨提示：您的账号没有权限", -2);
    }

    /**
     * 登录接口
     * @param userQuery
     * @param request
     * @param response
     * @return
     */
    @ApiOperation(value = "登录接口")
    @PostMapping("/login")
    public JsonData login(@RequestBody UserQuery userQuery, HttpServletRequest request, HttpServletResponse response){

        Subject subject = SecurityUtils.getSubject();

        Map<String, Object> info = new HashMap<>();

        try {

            UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(userQuery.getName(), userQuery.getPwd());

            subject.login(usernamePasswordToken);

            info.put("msg", "登陆成功");

            info.put("session_id", subject.getSession().getId());

            return JsonData.buildSuccess(info);

        }catch (Exception e){
            e.printStackTrace();

            return JsonData.buildSuccess("账号或密码错误");
        }
    }
}
