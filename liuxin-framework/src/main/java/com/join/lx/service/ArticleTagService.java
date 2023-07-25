package com.join.lx.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.join.lx.domain.entity.ArticleTag;

import java.util.List;


/**
 * 文章标签关联表(ArticleTag)表服务接口
 *
 * @author makejava
 * @since 2022-10-09 16:57:31
 */
public interface ArticleTagService extends IService<ArticleTag> {
    List<Long> getTags(Long id);
}
