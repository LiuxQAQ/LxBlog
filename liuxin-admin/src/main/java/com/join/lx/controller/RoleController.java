package com.join.lx.controller;


import com.join.lx.annotation.SystemLog;
import com.join.lx.domain.ResponseResult;
import com.join.lx.domain.dto.AdminAddRoleDto;
import com.join.lx.domain.dto.ChangeStatusRoleDto;
import com.join.lx.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/system/role")
public class RoleController {
    @Autowired
    private RoleService roleService;

    @GetMapping("/list")
    @PreAuthorize("@ps.hasPermission('system:role:query')")
    @SystemLog(businessName = "查询角色列表")
    public ResponseResult list(Integer pageNum,Integer pageSize,String roleName,String status){
        return roleService.list(pageNum,pageSize,roleName,status);
    }

    @PutMapping("/changeStatus")
    @PreAuthorize("@ps.hasPermission('system:role:edit')")
    @SystemLog(businessName = "角色状态修改")
    public ResponseResult changeStatus(@RequestBody ChangeStatusRoleDto roleDto){
        return roleService.changeStatus(roleDto);
    }

    @PostMapping
    @PreAuthorize("@ps.hasPermission('system:role:add')")
    @SystemLog(businessName = "新增角色")
    public ResponseResult addRole(@RequestBody AdminAddRoleDto adminAddRoleDto){
        return roleService.addRole(adminAddRoleDto);
    }

    @GetMapping("/{id}")
    @PreAuthorize("@ps.hasPermission('system:role:query')")
    @SystemLog(businessName = "角色详情")
    public ResponseResult roleDetail(@PathVariable("id") Long id){
        return roleService.roleDetail(id);
    }

    // TODO  获取树  字段不一致问题
    @GetMapping("/roleMenuTreeselect/{id}")
    @PreAuthorize("@ps.hasPermission('system:role:query')")
    @SystemLog(businessName = "获取树")
    public ResponseResult roleMenuTreeselect(@PathVariable("id") Long id){
        return null;
    }

    @PutMapping
    public ResponseResult updateRole(@RequestBody AdminAddRoleDto adminAddRoleDto){
        return null;
    }
}
