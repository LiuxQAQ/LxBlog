package com.join.lx.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.join.lx.domain.ResponseResult;
import com.join.lx.domain.dto.AddArticleDto;
import com.join.lx.domain.dto.AddMenuDto;
import com.join.lx.domain.dto.ArticleDto;
import com.join.lx.domain.dto.UpdateArticleDto;
import com.join.lx.domain.entity.Article;

/**
 *
 */

public interface ArticleService extends IService<Article> {

    ResponseResult hotArticleList();

    ResponseResult articleList(Integer pageNum, Integer pageSize, Long categoryId);

    ResponseResult getArticleDetail(Long id);

    ResponseResult updateViewCount(Long id);
    
    ResponseResult add(AddArticleDto addArticleDto);

    ResponseResult getList(Integer pageNum, Integer pageSize, ArticleDto articleDto);

    ResponseResult getArticle(Long id);

    ResponseResult updateArticle(UpdateArticleDto updateArticleDto);

    ResponseResult deleteArticle(Long id);



}
