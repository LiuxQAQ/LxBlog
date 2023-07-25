package com.join.lx.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.join.lx.constants.SystemConstants;
import com.join.lx.domain.ResponseResult;
import com.join.lx.domain.dto.AddArticleDto;
import com.join.lx.domain.dto.AddMenuDto;
import com.join.lx.domain.dto.ArticleDto;
import com.join.lx.domain.dto.UpdateArticleDto;
import com.join.lx.domain.entity.Article;
import com.join.lx.domain.entity.ArticleTag;
import com.join.lx.domain.entity.Category;
import com.join.lx.domain.entity.Menu;
import com.join.lx.domain.vo.*;
import com.join.lx.mapper.ArticleMapper;
import com.join.lx.service.ArticleService;
import com.join.lx.service.ArticleTagService;
import com.join.lx.service.CategoryService;
import com.join.lx.service.MenuService;
import com.join.lx.utils.BeanCopyUtils;
import com.join.lx.utils.RedisCache;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.xml.ws.Action;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 *
 */
@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article>
    implements ArticleService{

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private ArticleTagService articleTagService;

    @Autowired
    private MenuService menuService;


    @Override
    public ResponseResult hotArticleList() {
        // 查询热门文章  封装成ResponseResult返回
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        // 必须是正式文章
        queryWrapper.eq(Article::getStatus, SystemConstants.ARTICLE_STATUS_NORMAL);
        // 按照浏览量进行排序
        queryWrapper.orderByDesc(Article::getViewCount);
        //  最多只查询10条
        Page<Article> page = new Page(SystemConstants.ARTICLE_PAGE_CURRENT, SystemConstants.ARTICLE_PAGE_SIZE);
        page(page,queryWrapper);
        List<Article> articles = page.getRecords();
//        // Bean拷贝   BeanUtils.copyProperties()
//        List<HotArticleVo> hotArticleVos = new ArrayList<>();
//        for (Article article : articles) {
//            HotArticleVo vo = new HotArticleVo();
//            BeanUtils.copyProperties(article,vo);
//            hotArticleVos.add(vo);
//        }

        //  直接用工具类
        List<HotArticleVo> vs = BeanCopyUtils.copyBeanList(articles, HotArticleVo.class);
        return ResponseResult.okResult(vs);
    }



    @Override
    public ResponseResult articleList(Integer pageNum, Integer pageSize, Long categoryId) {
        // 查询条件
        LambdaQueryWrapper<Article> articleWrapper = new LambdaQueryWrapper<>();
        // 如果有categoryId  查询时就要求有 categoryId
        articleWrapper.eq(Objects.nonNull(categoryId)&&categoryId>0,Article::getCategoryId,categoryId);
        // 状态是正式发布的文章
        articleWrapper.eq(Article::getStatus,SystemConstants.ARTICLE_STATUS_NORMAL);
        // 对isTop进行排序
        articleWrapper.orderByDesc(Article::getIsTop);
        // 分页查询
        Page<Article> page = new Page<>(pageNum,pageSize);
        page(page,articleWrapper);
        List<Article> articles = page.getRecords();
        //  categoryId  去查询 categoryName
        articles.stream()
                .map(article -> article.setCategoryName(categoryService.getById(article.getCategoryId()).getName()))
                // 从redis中 获取浏览量
                .map(article -> article.setViewCount(Long.valueOf(redisCache.getCacheMapValue(SystemConstants.ARTICLE_VIEW_COUNT, article.getId().toString()).toString())))
                .collect(Collectors.toList());
        // 封装查询结果
        List<ArticleListVo> articleListVos = BeanCopyUtils.copyBeanList(articles, ArticleListVo.class);
        PageVo pageVo = new PageVo(articleListVos, page.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult getArticleDetail(Long id) {
        // 根据id 查询文章
        Article article = getById(id);
        // 从redis当中获取 文章的浏览量
        Integer viewCount = redisCache.getCacheMapValue(SystemConstants.ARTICLE_VIEW_COUNT, id.toString());
        // 转换成vo
        ArticleDetailVo articleDetailVo = BeanCopyUtils.copyBean(article, ArticleDetailVo.class);
        // 根据分类id  查询分类名称
        Long categoryId = articleDetailVo.getCategoryId();
        Category category = categoryService.getById(categoryId);
        if (category!=null){
            articleDetailVo.setCategoryName(category.getName());
            articleDetailVo.setViewCount(viewCount.longValue());
        }
        return ResponseResult.okResult(articleDetailVo);
    }

    @Override
    public ResponseResult updateViewCount(Long id) {
        // 更新redis中对应 id的浏览量
        redisCache.incrementCatchMapValue(SystemConstants.ARTICLE_VIEW_COUNT,id.toString(),SystemConstants.UPDATE_VIEW_COUNT);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult add(AddArticleDto addArticleDto) {
        // 添加博客
        Article article = BeanCopyUtils.copyBean(addArticleDto, Article.class);
        save(article);
        List<ArticleTag> articleTags = addArticleDto.getTags().stream()
                .map(tagId -> new ArticleTag(article.getId(), tagId))
                .collect(Collectors.toList());
        // 添加  博客和标签的关联
        articleTagService.saveBatch(articleTags);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult getList(Integer pageNum, Integer pageSize, ArticleDto articleDto) {
        // 查询条件
        LambdaQueryWrapper<Article> articleWrapper = new LambdaQueryWrapper<>();
        // 如果有title  模糊查询时就要求有 title
        articleWrapper.like(StringUtils.hasText(articleDto.getTitle()),Article::getTitle,articleDto.getTitle());
        // 如果有summary  模糊查询时就要求有 summary
        articleWrapper.like(StringUtils.hasText(articleDto.getSummary()),Article::getSummary,articleDto.getSummary());
        // 状态是正式发布的文章
        articleWrapper.eq(Article::getStatus,SystemConstants.ARTICLE_STATUS_NORMAL);
        // 对isTop进行排序
        articleWrapper.orderByDesc(Article::getIsTop);
        // 分页查询
        Page<Article> page = new Page<>(pageNum,pageSize);
        page(page,articleWrapper);
        List<Article> articles = page.getRecords();
        //  categoryId  去查询 categoryName
        articles.stream()
                // 从redis中 获取浏览量
                .map(article -> article.setViewCount(Long.valueOf(redisCache.getCacheMapValue(SystemConstants.ARTICLE_VIEW_COUNT, article.getId().toString()).toString())))
                .collect(Collectors.toList());
        // 封装查询结果
        List<AdminArticleListVo> adminArticleListVos = BeanCopyUtils.copyBeanList(articles, AdminArticleListVo.class);
        PageVo pageVo = new PageVo(adminArticleListVos, page.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult getArticle(Long id) {
        Article article = getById(id);
        // 从redis当中获取 文章的浏览量
        Integer viewCount = redisCache.getCacheMapValue(SystemConstants.ARTICLE_VIEW_COUNT, id.toString());
        List<Long> tags = articleTagService.getTags(id);
        AdminArticleDetailVo adminArticleDetailVo = BeanCopyUtils.copyBean(article, AdminArticleDetailVo.class);
        adminArticleDetailVo.setViewCount(viewCount.longValue()).setTags(tags);
        return ResponseResult.okResult(adminArticleDetailVo);
    }

    @Override
    public ResponseResult updateArticle(UpdateArticleDto updateArticleDto) {
        Article article = BeanCopyUtils.copyBean(updateArticleDto, Article.class);
        LambdaUpdateWrapper<Article> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Article::getId,article.getId());
        update(article,updateWrapper);
        // 更改  博客和标签的关联
        List<ArticleTag> articleTags = updateArticleDto.getTags().stream()
                .map(tagId -> new ArticleTag(article.getId(), tagId))
                .collect(Collectors.toList());
        articleTagService.removeById(article.getId());
        articleTagService.saveBatch(articleTags);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult deleteArticle(Long id) {
        LambdaUpdateWrapper<Article> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Article::getId,id)
                .set(Article::getDelFlag,SystemConstants.ARTICLE_STATUS_NOT_NORMAL);
        update(updateWrapper);
        return ResponseResult.okResult();
    }
}




