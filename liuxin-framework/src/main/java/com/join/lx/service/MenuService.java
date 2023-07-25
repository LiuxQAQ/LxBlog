package com.join.lx.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.join.lx.domain.ResponseResult;
import com.join.lx.domain.dto.AddMenuDto;
import com.join.lx.domain.dto.AdminMenuDetailDto;
import com.join.lx.domain.entity.Menu;

import java.util.List;


/**
 * 菜单权限表(Menu)表服务接口
 *
 * @author makejava
 * @since 2022-10-05 19:57:24
 */
public interface MenuService extends IService<Menu> {

    List<String> selectPermsByUserId(Long id);

    List<Menu> selectRouterMenuTreeByUserId(Long id);

    ResponseResult list(String status, String menuName);

    ResponseResult addMenu(AddMenuDto addMenuDto);

    ResponseResult getMenu(Long id);

    ResponseResult updateMenu(AdminMenuDetailDto adminMenuDetailDto);

    ResponseResult deleteMenu(Long id);

    ResponseResult selectTree();

}
