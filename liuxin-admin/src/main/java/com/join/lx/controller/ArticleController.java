package com.join.lx.controller;

import com.join.lx.annotation.SystemLog;
import com.join.lx.domain.ResponseResult;
import com.join.lx.domain.dto.AddArticleDto;
import com.join.lx.domain.dto.AddMenuDto;
import com.join.lx.domain.dto.ArticleDto;
import com.join.lx.domain.dto.UpdateArticleDto;
import com.join.lx.domain.entity.Article;
import com.join.lx.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/content/article")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @PostMapping
    @PreAuthorize("@ps.hasPermission('content:article:writer')")
    public ResponseResult add(@RequestBody AddArticleDto addArticleDto){
        return articleService.add(addArticleDto);
    }

    @GetMapping("/list")
    @PreAuthorize("@ps.hasPermission('content:article:list')")
    @SystemLog(businessName = "获取文章列表")
    public ResponseResult getList(Integer pageNum, Integer pageSize, ArticleDto articleDto){
        return articleService.getList(pageNum,pageSize,articleDto);
    }

    @GetMapping("/{id}")
    @PreAuthorize("@ps.hasPermission('content:article:list')")
    @SystemLog(businessName = "查询文章详情")
    public ResponseResult getArticle(@PathVariable("id") Long id){
        return articleService.getArticle(id);
    }

    @PutMapping
    @PreAuthorize("@ps.hasPermission('content:article:list')")
    @SystemLog(businessName = "更改文章")
    public ResponseResult updateArticle(@RequestBody UpdateArticleDto updateArticleDto){
        return articleService.updateArticle(updateArticleDto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("@ps.hasPermission('content:article:list')")
    @SystemLog(businessName = "删除文章")
    public ResponseResult deleteArticle(@PathVariable("id") Long id){
        return articleService.deleteArticle(id);
    }


}
