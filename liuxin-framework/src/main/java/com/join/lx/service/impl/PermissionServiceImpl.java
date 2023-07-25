package com.join.lx.service.impl;

import com.join.lx.utils.SecurityUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("ps")
public class PermissionServiceImpl {
/**
 * @author lx
 * @date 2022/10/12 17:38
 * @param permission
 * @description 判断当前用户是否具有permission
 */
    public boolean hasPermission(String permission){
        //如果是超级管理员  直接返回 true
        if (SecurityUtils.isAdmin()){
            return true;
        }
        // 否者  获取当前登录用户所具有的权限列表  如何判断是否存在permission
        List<String> permissions = SecurityUtils.getLoginUser().getPermissions();
        return permissions.contains(permission);
    }
}
