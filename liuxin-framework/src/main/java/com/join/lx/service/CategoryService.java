package com.join.lx.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.join.lx.domain.ResponseResult;
import com.join.lx.domain.entity.Category;


/**
 * 分类表(Category)表服务接口
 *
 * @author makejava
 * @since 2022-04-26 08:51:40
 */
public interface CategoryService extends IService<Category> {
    ResponseResult getCategoryList();

    ResponseResult listAllCategory();
}

