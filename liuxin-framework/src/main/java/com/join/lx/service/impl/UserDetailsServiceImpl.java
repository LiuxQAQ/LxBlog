package com.join.lx.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.join.lx.constants.SystemConstants;
import com.join.lx.domain.entity.LoginUser;
import com.join.lx.domain.entity.User;
import com.join.lx.mapper.MenuMapper;
import com.join.lx.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.sql.ClientInfoStatus;
import java.util.List;
import java.util.Objects;


//  4
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private MenuMapper menuMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 根据用户名查询用户信息
        LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userLambdaQueryWrapper.eq(User::getUserName,username);
        User user = userMapper.selectOne(userLambdaQueryWrapper);
        // 判断是否查到用户  没有查到 抛出异常
        if (Objects.isNull(user))
             throw new RuntimeException("用户不存在");
        // 返回用户信息
        List<String> permissions = null;
        // TODO 查询权限信息封装
        if (user.getType().equals(SystemConstants.ADMIN)){
            permissions = menuMapper.selectPermsByUserId(user.getId());
        }
        // TODO 如果是后台用户才需要查询权限封装
        return new LoginUser(user,permissions);
    }
}
