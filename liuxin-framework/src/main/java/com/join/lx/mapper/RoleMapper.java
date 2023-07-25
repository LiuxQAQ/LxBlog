package com.join.lx.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.join.lx.domain.entity.Role;

import java.util.List;


/**
 * 角色信息表(Role)表数据库访问层
 *
 * @author makejava
 * @since 2022-10-05 20:05:29
 */
public interface RoleMapper extends BaseMapper<Role> {

    List<String> selectRolesByUserId(Long id);

}
