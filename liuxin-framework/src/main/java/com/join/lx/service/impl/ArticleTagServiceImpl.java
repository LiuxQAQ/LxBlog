package com.join.lx.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.join.lx.domain.entity.ArticleTag;
import com.join.lx.domain.entity.Tag;
import com.join.lx.mapper.ArticleTagMapper;
import com.join.lx.service.ArticleTagService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 文章标签关联表(ArticleTag)表服务实现类
 *
 * @author makejava
 * @since 2022-10-09 16:57:31
 */
@Service("articleTagService")
public class ArticleTagServiceImpl extends ServiceImpl<ArticleTagMapper, ArticleTag> implements ArticleTagService {
    @Override
    public List<Long> getTags(Long id) {
        LambdaQueryWrapper<ArticleTag> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ArticleTag::getArticleId,id);
        List<ArticleTag> list = list(wrapper);
        List<Long> tags = list.stream()
                .map(ArticleTag -> ArticleTag.getTagId())
                .collect(Collectors.toList());
        return tags;
    }
}
