package com.join.lx.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.join.lx.domain.ResponseResult;
import com.join.lx.domain.dto.AdminAddRoleDto;
import com.join.lx.domain.dto.ChangeStatusRoleDto;
import com.join.lx.domain.entity.Role;

import java.util.List;


/**
 * 角色信息表(Role)表服务接口
 *
 * @author makejava
 * @since 2022-10-05 20:05:29
 */


public interface RoleService extends IService<Role> {

    List<String> selectRoleKeyByUserId(Long id);

    ResponseResult list(Integer pageNum, Integer pageSize, String roleName, String status);

    ResponseResult changeStatus(ChangeStatusRoleDto roleDto);


    ResponseResult addRole(AdminAddRoleDto adminAddRoleDto);

    ResponseResult roleDetail(Long id);

}
