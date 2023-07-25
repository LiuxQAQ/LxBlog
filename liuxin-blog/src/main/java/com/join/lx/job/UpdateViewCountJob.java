package com.join.lx.job;


import com.join.lx.constants.SystemConstants;
import com.join.lx.domain.entity.Article;
import com.join.lx.service.ArticleService;
import com.join.lx.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class UpdateViewCountJob {

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private ArticleService articleService;
    /**
     *   从redis中获取数据 更新到 数据库中
     *   每 十分钟执行一次
     */
    @Scheduled(cron = "0 0/10 * * * ?")
    public void updateViewCount(){
        // 获取redis中的浏览量
        Map<String, Integer> viewCountMap = redisCache.getCacheMap(SystemConstants.ARTICLE_VIEW_COUNT);
        // 双列集合不能使用stream流  先转换为单列集合
        List<Article> collect = viewCountMap.entrySet()
                .stream()
                .map(stringIntegerEntry -> new Article(Long.valueOf(stringIntegerEntry.getKey()), Long.valueOf(stringIntegerEntry.getValue())))
                .collect(Collectors.toList());
        // 更新到数据库中
         articleService.updateBatchById(collect);
    }
}
