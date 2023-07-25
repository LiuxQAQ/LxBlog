package com.join.lx.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.join.lx.domain.entity.RoleMenu;
import com.join.lx.mapper.RoleMenuMapper;
import com.join.lx.service.RoleMenuService;
import org.springframework.stereotype.Service;

/**
 * 角色和菜单关联表(RoleMenu)表服务实现类
 *
 * @author makejava
 * @since 2022-10-15 16:36:44
 */
@Service("roleMenuService")
public class RoleMenuServiceImpl extends ServiceImpl<RoleMenuMapper, RoleMenu> implements RoleMenuService {

}
