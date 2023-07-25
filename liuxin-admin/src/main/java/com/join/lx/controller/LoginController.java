package com.join.lx.controller;


import com.join.lx.annotation.SystemLog;
import com.join.lx.domain.ResponseResult;
import com.join.lx.domain.entity.LoginUser;
import com.join.lx.domain.entity.Menu;
import com.join.lx.domain.vo.RoutersVo;
import com.join.lx.domain.vo.AdminUserInfoVo;
import com.join.lx.domain.entity.User;
import com.join.lx.domain.vo.UserInfoVo;
import com.join.lx.service.LoginService;
import com.join.lx.service.MenuService;
import com.join.lx.service.RoleService;
import com.join.lx.utils.BeanCopyUtils;
import com.join.lx.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class LoginController {

    @Autowired
    private LoginService loginService;

    @Autowired
    private MenuService menuService;

    @Autowired
    private RoleService roleService;

    @PostMapping("/user/login")
    @SystemLog(businessName = "登陆")
    public ResponseResult login(@RequestBody User user){
        return loginService.login(user);
    }

    @PostMapping("/user/logout")
    @SystemLog(businessName = "退出登录")
    public ResponseResult logout(){
        return loginService.logout();
    }


    @GetMapping("/getInfo")
    public ResponseResult<AdminUserInfoVo> getInfo(){
        // 获取当前登录的用户
        LoginUser loginUser = SecurityUtils.getLoginUser();
        // 根据用户id查询权限信息
        List<String> perms = menuService.selectPermsByUserId(loginUser.getUser().getId());
        // 根据用户id查询角色信息
        List<String> roles = roleService.selectRoleKeyByUserId(loginUser.getUser().getId());

        User user = loginUser.getUser();
        UserInfoVo userInfoVo = BeanCopyUtils.copyBean(user, UserInfoVo.class);
        AdminUserInfoVo adminUserInfoVo = new AdminUserInfoVo(perms,roles,userInfoVo);
        return ResponseResult.okResult(adminUserInfoVo);

    }

    @GetMapping("/getRouters")
    public ResponseResult<RoutersVo> getRouters(){
        // 查询menu 结果是tree的形式
        Long id = SecurityUtils.getUserId();
        List<Menu> routers = menuService.selectRouterMenuTreeByUserId(id);
        return ResponseResult.okResult(new RoutersVo(routers));
    }

}
