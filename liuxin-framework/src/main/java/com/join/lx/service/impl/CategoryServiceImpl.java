package com.join.lx.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.join.lx.constants.SystemConstants;
import com.join.lx.domain.ResponseResult;
import com.join.lx.domain.entity.Article;
import com.join.lx.domain.entity.Category;
import com.join.lx.domain.vo.AdminCategoryVo;
import com.join.lx.domain.vo.CategoryVo;
import com.join.lx.mapper.CategoryMapper;
import com.join.lx.service.ArticleService;
import com.join.lx.service.CategoryService;
import com.join.lx.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * 分类表(Category)表服务实现类
 *
 * @author makejava
 * @since 2022-04-26 08:51:40
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Autowired
    private ArticleService articleService;

    @Override
    public ResponseResult getCategoryList() {
        LambdaQueryWrapper<Article> articleWrapper = new LambdaQueryWrapper<>();

        // 查询文章表  状态为已发布的文章
        articleWrapper.eq(Article::getStatus, SystemConstants.ARTICLE_STATUS_NORMAL);
        List<Article> articles = articleService.list(articleWrapper);
        // 获取文章的分类id ， 并且去重  最好是用set集合 没有重复元素
        Set<Long> categoryIds = articles.stream()
                .map(Article::getCategoryId)
                .collect(Collectors.toSet());

        // 查询分类表
        List<Category> categories = listByIds(categoryIds);
        // 过滤掉非正常状态
        List<Category> categoryList = categories.stream()
                .filter(category -> SystemConstants.CATEGORY_STATUS_NORMAL.equals(category.getStatus()))
                .collect(Collectors.toList());

        // 封装vo
        List<CategoryVo> vs = BeanCopyUtils.copyBeanList(categoryList, CategoryVo.class);


        return ResponseResult.okResult(vs);
    }

    @Override
    public ResponseResult listAllCategory() {
        List<Category> list = list();
        List<Category> categories = list.stream()
                .filter(category -> SystemConstants.CATEGORY_STATUS_NORMAL.equals(category.getStatus()))
                .collect(Collectors.toList());
        List<AdminCategoryVo> categoryVos = BeanCopyUtils.copyBeanList(categories, AdminCategoryVo.class);
        return ResponseResult.okResult(categoryVos);
    }
}
