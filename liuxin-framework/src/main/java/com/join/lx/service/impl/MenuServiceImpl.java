package com.join.lx.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.join.lx.constants.SystemConstants;
import com.join.lx.domain.ResponseResult;
import com.join.lx.domain.dto.AddMenuDto;
import com.join.lx.domain.dto.AdminMenuDetailDto;
import com.join.lx.domain.entity.Menu;
import com.join.lx.domain.vo.AdminMenuDetailVo;
import com.join.lx.domain.vo.AdminMenuListVo;
import com.join.lx.domain.vo.MenuTreeVo;
import com.join.lx.enums.AppHttpCodeEnum;
import com.join.lx.exception.SystemException;
import com.join.lx.mapper.MenuMapper;
import com.join.lx.service.MenuService;
import com.join.lx.utils.BeanCopyUtils;
import com.join.lx.utils.SecurityUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 菜单权限表(Menu)表服务实现类
 *
 * @author makejava
 * @since 2022-10-05 19:58:20
 */
@Service("menuService")
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {

    @Override
    public List<String> selectPermsByUserId(Long id) {
        if (SecurityUtils.isAdmin()){
            LambdaQueryWrapper<Menu> wrapper = new LambdaQueryWrapper<>();
            wrapper.in(Menu::getMenuType, SystemConstants.SYSTEM_MENUS,SystemConstants.SYSTEM_BUTTON);
            wrapper.eq(Menu::getStatus,SystemConstants.MENU_STATUS);
            List<Menu> list = list(wrapper);
            List<String> perms = list.stream()
                    .map(Menu::getPerms)
                    .collect(Collectors.toList());
            return perms;
        }

        List<String> baseMapper = getBaseMapper().selectPermsByUserId(id);
        return baseMapper;
    }

    @Override
    public ResponseResult list(String status, String menuName) {
        LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.hasText(menuName),Menu::getMenuName,menuName);
        queryWrapper.like(StringUtils.hasText(status),Menu::getStatus,status);
        List<Menu> menus = list(queryWrapper);
        List<AdminMenuListVo> adminMenuListVos = BeanCopyUtils.copyBeanList(menus, AdminMenuListVo.class);
        return ResponseResult.okResult(adminMenuListVos);
    }

    @Override
    public List<Menu> selectRouterMenuTreeByUserId(Long userId) {
        MenuMapper menuMapper = getBaseMapper();
        List<Menu> menus = null;
        //判断是否是管理员
        if(SecurityUtils.isAdmin()){
            //如果是 获取所有符合要求的Menu
            menus = menuMapper.selectAllRouterMenu();
        }else{
            //否则  获取当前用户所具有的Menu
            menus = menuMapper.selectRouterMenuTreeByUserId(userId);
        }
        //构建tree
        //先找出第一层的菜单  然后去找他们的子菜单设置到children属性中
        List<Menu> menuTree = builderMenuTree(menus,0L);
        return menuTree;
    }


    private List<Menu> builderMenuTree(List<Menu> menus, Long parentId) {
        List<Menu> menuTree = menus.stream()
                .filter(menu -> menu.getParentId().equals(parentId))
                .map(menu -> menu.setChildren(getChildren(menu, menus)))
                .collect(Collectors.toList());
        return menuTree;
    }

    /**
     * @author lx
     * @date 2022/10/6 16:03
     * @param  menu menus
     * @description 获取存入参数的子Menu集合
     */
    private List<Menu> getChildren(Menu menu, List<Menu> menus) {
        List<Menu> childrenList = menus.stream()
                .filter(m -> m.getParentId().equals(menu.getId()))
                .map(m->m.setChildren(getChildren(m,menus)))
                .collect(Collectors.toList());
        return childrenList;
    }
    @Override
    public ResponseResult addMenu(AddMenuDto addMenuDto) {
        Menu menu = BeanCopyUtils.copyBean(addMenuDto, Menu.class);
        save(menu);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult getMenu(Long id) {
        Menu menu = getById(id);
        AdminMenuDetailVo menuDetailVo = BeanCopyUtils.copyBean(menu, AdminMenuDetailVo.class);
        return ResponseResult.okResult(menuDetailVo);
    }

    @Override
    public ResponseResult updateMenu(AdminMenuDetailDto adminMenuDetailDto) {

        Menu menu = BeanCopyUtils.copyBean(adminMenuDetailDto, Menu.class);
        if (menu.getParentId().equals(menu.getId()))
            throw new RuntimeException("修改菜单'"+menu.getMenuName()+"'失败,上级菜单不能是自己");
        LambdaUpdateWrapper<Menu> menuLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        menuLambdaUpdateWrapper.eq(Menu::getId,menu.getId());
        update(menu,menuLambdaUpdateWrapper);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult deleteMenu(Long id) {
        List<Menu> list = list();
        List<Long> parentIds = list.stream()
                .map(menu -> menu.getParentId())
                .collect(Collectors.toList());
        if (parentIds.contains(id)){
            throw new SystemException(AppHttpCodeEnum.DELETE_MENU_ERROR);
        }

        LambdaUpdateWrapper<Menu> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Menu::getId,id)
                .set(Menu::getDelFlag,SystemConstants.MENU_DELETE);
        update(updateWrapper);
        return ResponseResult.okResult();
    }

    @Override
    // TODO   类型转换
    public ResponseResult selectTree() {
        List<Menu> menus = list();
        List<Menu> menuTree = builderMenuTree(menus, 0L);
        List<MenuTreeVo> menuTreeVoList = menuTree.stream()
                .map(menu -> new MenuTreeVo(menu.getId(), menu.getMenuName(), menu.getParentId(), BeanCopyUtils.copyBeanList(menu.getChildren(),MenuTreeVo.class)))
                .collect(Collectors.toList());
//        List<MenuTreeVo> menuTreeVos = BeanCopyUtils.copyBeanList(menuTree, MenuTreeVo.class);
        return ResponseResult.okResult(menuTreeVoList);
    }



}
