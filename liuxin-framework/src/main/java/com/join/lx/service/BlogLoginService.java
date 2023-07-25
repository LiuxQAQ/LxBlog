package com.join.lx.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.join.lx.domain.ResponseResult;
import com.join.lx.domain.entity.User;


/**
 * 用户表(User)表服务接口
 *
 * @author makejava
 * @since 2022-04-26 20:21:47
 */

public interface BlogLoginService extends IService<User> {

    ResponseResult login(User user);

    ResponseResult logout();
}

