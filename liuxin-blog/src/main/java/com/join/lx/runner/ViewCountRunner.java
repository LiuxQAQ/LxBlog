package com.join.lx.runner;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.join.lx.constants.SystemConstants;
import com.join.lx.domain.entity.Article;
import com.join.lx.mapper.ArticleMapper;
import com.join.lx.utils.JwtUtil;
import com.join.lx.utils.RedisCache;
import com.join.lx.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

// 服务开启自动运行方法
@Component
public class ViewCountRunner implements CommandLineRunner {
    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private RedisCache redisCache;
    @Override
    public void run(String... args) throws Exception {
        // 查询博客信息 id 和 viewCount
        List<Article> articles = articleMapper.selectList(null);
        Map<String, Integer> articleMap = articles.stream()
                .collect(Collectors.toMap(article -> article.getId().toString(), article1 -> article1.getViewCount().intValue()));
        // 存储到redis中
        redisCache.setCacheMap(SystemConstants.ARTICLE_VIEW_COUNT,articleMap);

    }
}
