package com.join.lx.controller;


import com.join.lx.annotation.SystemLog;
import com.join.lx.domain.ResponseResult;
import com.join.lx.domain.entity.Article;
import com.join.lx.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/article")
public class ArticleController {


    @Autowired
    private ArticleService articleService;


    @GetMapping("/hotArticleList")
    @SystemLog(businessName = "热门文章获取")
    public ResponseResult hotArticleList(){
        return articleService.hotArticleList();
    }

    @GetMapping("/articleList")
    @SystemLog(businessName = "获取全部文章")
    public ResponseResult articleList(Integer pageNum,Integer pageSize, Long categoryId){
        return articleService.articleList(pageNum,pageSize,categoryId);
    }

    @GetMapping("/{id}")
    @SystemLog(businessName = "获取文章详情")
    public ResponseResult getArticleDetail(@PathVariable("id") Long id){
        return articleService.getArticleDetail(id);
    }

    @PutMapping("updateViewCount/{id}")
    @SystemLog(businessName = "更新文章浏览量")
    public ResponseResult updateViewCount(@PathVariable("id") Long id){
        return articleService.updateViewCount(id);
    }
}
