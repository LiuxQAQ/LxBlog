package com.join.lx.service.impl;

import com.join.lx.constants.SystemConstants;
import com.join.lx.domain.ResponseResult;
import com.join.lx.domain.entity.LoginUser;
import com.join.lx.domain.entity.User;
import com.join.lx.domain.vo.BlogUserLoginVo;
import com.join.lx.domain.vo.UserInfoVo;
import com.join.lx.enums.AppHttpCodeEnum;
import com.join.lx.exception.SystemException;
import com.join.lx.service.LoginService;
import com.join.lx.utils.BeanCopyUtils;
import com.join.lx.utils.JwtUtil;
import com.join.lx.utils.RedisCache;
import com.join.lx.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
public class LoginServiceImpl implements LoginService {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private RedisCache redisCache;

    @Override
    public ResponseResult login(User user) {
        if (!StringUtils.hasText(user.getUserName()))
            // 提示 必须传用户名
            throw new SystemException(AppHttpCodeEnum.REQUIRE_USERNAME);

        // 3
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getUserName(), user.getPassword());
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        // 6
        // 判断是否认证通过
        if (Objects.isNull(authenticate))
            throw new RuntimeException("用户名或密码错误");
        // 获取userid 生成token
        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
        String userId = loginUser.getUser().getId().toString();
        String jwt = JwtUtil.createJWT(userId);
        // 把用户信息存入redis中
        redisCache.setCacheObject(SystemConstants.ADMIN_LOGIN_ID+userId,loginUser);

        //把token封装 返回
        Map<String,String> map = new HashMap<>();
        map.put("token",jwt);
        return ResponseResult.okResult(map);
    }

    @Override
    public ResponseResult logout() {

        // 获取userId
        Long userId = SecurityUtils.getUserId();
        redisCache.deleteObject(SystemConstants.ADMIN_LOGIN_ID+userId);
        return ResponseResult.okResult();
    }
}
