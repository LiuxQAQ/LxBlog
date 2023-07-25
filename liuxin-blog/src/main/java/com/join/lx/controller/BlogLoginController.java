package com.join.lx.controller;



import com.join.lx.annotation.SystemLog;
import com.join.lx.domain.ResponseResult;
import com.join.lx.domain.entity.User;
import com.join.lx.enums.AppHttpCodeEnum;
import com.join.lx.exception.SystemException;
import com.join.lx.service.BlogLoginService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BlogLoginController {

    @Autowired
    private BlogLoginService blogLoginService;

    @PostMapping("/login")
    @SystemLog(businessName = "登陆")
    public ResponseResult login(@RequestBody User user){
        return blogLoginService.login(user);
    }

    @PostMapping("/logout")
    @SystemLog(businessName = "退出登录")
    public ResponseResult logout(){
        return blogLoginService.logout();
    }
}
