package com.join.lx.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.join.lx.domain.entity.User;
import org.springframework.stereotype.Component;


/**
 * 用户表(User)表数据库访问层
 *
 * @author makejava
 * @since 2022-04-26 19:32:37
 */
@Component
public interface UserMapper extends BaseMapper<User> {

}
