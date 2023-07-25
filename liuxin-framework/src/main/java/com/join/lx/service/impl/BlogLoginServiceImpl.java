package com.join.lx.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.join.lx.constants.SystemConstants;
import com.join.lx.domain.ResponseResult;
import com.join.lx.domain.entity.LoginUser;
import com.join.lx.domain.entity.User;
import com.join.lx.domain.vo.BlogUserLoginVo;
import com.join.lx.domain.vo.UserInfoVo;
import com.join.lx.enums.AppHttpCodeEnum;
import com.join.lx.exception.SystemException;
import com.join.lx.mapper.UserMapper;
import com.join.lx.service.BlogLoginService;
import com.join.lx.utils.BeanCopyUtils;
import com.join.lx.utils.JwtUtil;
import com.join.lx.utils.RedisCache;
import com.join.lx.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Objects;

/**
 * 用户表(User)表服务实现类
 *
 * @author makejava
 * @since 2022-04-26 20:21:50
 */
@Service
public class BlogLoginServiceImpl extends ServiceImpl<UserMapper, User> implements BlogLoginService {

    //  1
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
        redisCache.setCacheObject(SystemConstants.BLOG_LOGIN_ID +userId,loginUser);

        UserInfoVo userInfoVo = BeanCopyUtils.copyBean(loginUser.getUser(), UserInfoVo.class);
        BlogUserLoginVo blogUserLoginVo = new BlogUserLoginVo(jwt, userInfoVo);
        return ResponseResult.okResult(blogUserLoginVo);
    }

    @Override
    public ResponseResult logout() {
        // 获取token 解析获取userId
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        // 获取userId
        Long userId = SecurityUtils.getUserId();
        // 删除redis中的用户信息
        redisCache.deleteObject(SystemConstants.BLOG_LOGIN_ID+userId);

        return ResponseResult.okResult();
    }


}
