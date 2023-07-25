package com.join.lx.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.join.lx.constants.SystemConstants;
import com.join.lx.domain.ResponseResult;
import com.join.lx.domain.dto.AdminAddRoleDto;
import com.join.lx.domain.dto.ChangeStatusRoleDto;
import com.join.lx.domain.entity.Menu;
import com.join.lx.domain.entity.Role;
import com.join.lx.domain.entity.RoleMenu;
import com.join.lx.domain.vo.AdminRoleDetail;
import com.join.lx.domain.vo.AdminRoleListVo;
import com.join.lx.domain.vo.MenuTreeVo;
import com.join.lx.domain.vo.PageVo;
import com.join.lx.mapper.RoleMapper;
import com.join.lx.service.MenuService;
import com.join.lx.service.RoleMenuService;
import com.join.lx.service.RoleService;
import com.join.lx.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 角色信息表(Role)表服务实现类
 *
 * @author makejava
 * @since 2022-10-05 20:06:29
 */
@Service("roleService")
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    @Autowired
    private MenuService menuService;

    @Autowired
    private RoleMenuService roleMenuService;


    @Override
    public List<String> selectRoleKeyByUserId(Long id) {
        if (id == 1L){
            return Collections.singletonList(SystemConstants.SYSTEM_ROLE_KEY);
        }
        List<String> roles = getBaseMapper().selectRolesByUserId(id);
        return roles;
    }

    @Override
    public ResponseResult list(Integer pageNum, Integer pageSize, String roleName, String status) {
        // 条件封装
        LambdaQueryWrapper<Role> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.hasText(roleName),Role::getRoleName,roleName);
        queryWrapper.like(StringUtils.hasText(status),Role::getStatus,status);
        queryWrapper.orderByAsc(Role::getRoleSort);

        // 分页查询
        Page<Role> page = new Page<>();
        page.setCurrent(pageNum);
        page.setSize(pageSize);
        page(page,queryWrapper);
        List<Role> roles = page.getRecords();
        List<AdminRoleListVo> roleListVos = BeanCopyUtils.copyBeanList(roles, AdminRoleListVo.class);
        return ResponseResult.okResult(new PageVo(roleListVos,page.getTotal()));
    }

    @Override
    public ResponseResult changeStatus(ChangeStatusRoleDto roleDto) {

        LambdaUpdateWrapper<Role> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Role::getId,roleDto.getRoleId())
                .set(Role::getStatus,roleDto.getStatus());
        update(updateWrapper);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult addRole(AdminAddRoleDto adminAddRoleDto) {
        Role role = BeanCopyUtils.copyBean(adminAddRoleDto, Role.class);
        save(role);
        List<RoleMenu> roleMenus = adminAddRoleDto.getMenuIds().stream()
                .map(menuId -> new RoleMenu(role.getId(), menuId))
                .collect(Collectors.toList());
        roleMenuService.saveBatch(roleMenus);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult roleDetail(Long id) {
        Role role = getById(id);
        AdminRoleDetail roleDetail = BeanCopyUtils.copyBean(role, AdminRoleDetail.class);
        return ResponseResult.okResult(roleDetail);
    }

}
