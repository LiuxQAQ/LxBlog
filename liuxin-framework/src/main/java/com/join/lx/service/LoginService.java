package com.join.lx.service;


import com.join.lx.domain.ResponseResult;
import com.join.lx.domain.entity.User;

public interface LoginService {
    ResponseResult login(User user);

    ResponseResult logout();
}
