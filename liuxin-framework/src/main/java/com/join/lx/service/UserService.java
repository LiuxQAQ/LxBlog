package com.join.lx.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.join.lx.domain.ResponseResult;
import com.join.lx.domain.entity.User;


/**
 * 用户表(User)表服务接口
 *
 * @author makejava
 * @since 2022-04-27 22:38:39
 */
public interface UserService extends IService<User> {

    ResponseResult userInfo() ;

    ResponseResult updateUserInfo(User user);

    ResponseResult register(User user);
}

