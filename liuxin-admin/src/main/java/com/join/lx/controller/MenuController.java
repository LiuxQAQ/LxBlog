package com.join.lx.controller;

import com.join.lx.annotation.SystemLog;
import com.join.lx.domain.ResponseResult;
import com.join.lx.domain.dto.AddMenuDto;
import com.join.lx.domain.dto.AdminMenuDetailDto;
import com.join.lx.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/system/menu")
public class MenuController {

    @Autowired
    private MenuService menuService;

    @GetMapping("/list")
    @PreAuthorize("@ps.hasPermission('system:menu:query')")
    @SystemLog(businessName = "查询菜单列表")
    public ResponseResult list(String status,String menuName){
        return menuService.list(status,menuName);
    }

    @PostMapping
    @PreAuthorize("@ps.hasPermission('system:menu:add')")
    @SystemLog(businessName = "新增菜单")
    public ResponseResult addMenu(@RequestBody AddMenuDto addMenuDto){
        return menuService.addMenu(addMenuDto);
    }

    @GetMapping("/{id}")
    @PreAuthorize("@ps.hasPermission('system:menu:query')")
    @SystemLog(businessName = "菜单查询")
    public ResponseResult getMenu(@PathVariable("id") Long id){
        return menuService.getMenu(id);
    }

    @PutMapping
    @PreAuthorize("@ps.hasPermission('system:menu:edit')")
    @SystemLog(businessName = "菜单修改")
    public ResponseResult updateMenu(@RequestBody AdminMenuDetailDto adminMenuDetailDto){
        return menuService.updateMenu(adminMenuDetailDto);
    }

    @DeleteMapping("/{menuId}")
    @PreAuthorize("@ps.hasPermission('system:menu:remove')")
    @SystemLog(businessName = "菜单删除")
    public ResponseResult deleteMenu(@PathVariable("menuId") Long id){
        return menuService.deleteMenu(id);
    }

    @GetMapping("/treeselect")
    @PreAuthorize("@ps.hasPermission('system:role:add')")
    @SystemLog(businessName = "查询菜单树")
    public ResponseResult selectTree(){
        return menuService.selectTree();
    }
}
