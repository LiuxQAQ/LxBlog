package com.join.lx.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.join.lx.domain.ResponseResult;
import com.join.lx.domain.entity.User;
import com.join.lx.domain.vo.UserInfoVo;
import com.join.lx.enums.AppHttpCodeEnum;
import com.join.lx.exception.SystemException;
import com.join.lx.mapper.UserMapper;
import com.join.lx.service.UserService;
import com.join.lx.utils.BeanCopyUtils;
import com.join.lx.utils.SecurityUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * 用户表(User)表服务实现类
 *
 * @author makejava
 * @since 2022-04-27 22:38:40
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Override
    public ResponseResult userInfo() {
        // 获取当前登录的用户id
        Long userId = SecurityUtils.getUserId();
        // 查询用户信息
        User user = getById(userId);
        // 封装成UserInfoVo
        UserInfoVo userInfoVo = BeanCopyUtils.copyBean(user, UserInfoVo.class);
        return ResponseResult.okResult(userInfoVo);
    }

    @Override
    public ResponseResult updateUserInfo(User user) {
//        updateById(user);
        LambdaUpdateWrapper<User> userWrapper = new LambdaUpdateWrapper<>();
        userWrapper.eq(User::getId,SecurityUtils.getUserId())
                .set(User::getAvatar,user.getAvatar())
                .set(User::getNickName,user.getNickName())
                .set(User::getSex,user.getSex());
        update(userWrapper);
        return ResponseResult.okResult();
    }


    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public ResponseResult register(User user) {
        // 对数据进行非空判断
        if (!StringUtils.hasText(user.getUserName())) throw new SystemException(AppHttpCodeEnum.USERNAME_NOT_NULL);
        if (!StringUtils.hasText(user.getNickName())) throw new SystemException(AppHttpCodeEnum.NICKNAME_NOT_NULL);
        if (!StringUtils.hasText(user.getPassword())) throw new SystemException(AppHttpCodeEnum.PASSWORD_NOT_NULL);
        if (!StringUtils.hasText(user.getEmail())) throw new SystemException(AppHttpCodeEnum.EMAIL_NOT_NULL);
        // 不为空 对数据进行是否重复的判断
        if (userNameExist(user.getUserName())) throw new SystemException(AppHttpCodeEnum.USERNAME_EXIST);
        if (nickNameExist(user.getNickName())) throw new SystemException(AppHttpCodeEnum.NICKNAME_EXIST);
        if (emailExist(user.getEmail())) throw new SystemException(AppHttpCodeEnum.EMAIL_EXIST);

        // 对密码 进行加密处理
        String encodePassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodePassword);
        // 存入数据库中
        save(user);
        return ResponseResult.okResult();
    }



    private boolean emailExist(String email) {
        LambdaUpdateWrapper<User> userLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        userLambdaUpdateWrapper.eq(User::getEmail,email);
        int count = count(userLambdaUpdateWrapper);
        return count > 0;
    }

    private boolean nickNameExist(String nickName) {
        LambdaUpdateWrapper<User> userLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        userLambdaUpdateWrapper.eq(User::getNickName,nickName);
        int count = count(userLambdaUpdateWrapper);
        return count > 0;
    }


    private boolean userNameExist(String userName) {
        LambdaUpdateWrapper<User> userLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        userLambdaUpdateWrapper.eq(User::getUserName,userName);
        int count = count(userLambdaUpdateWrapper);
        return count>0;
    }
}
