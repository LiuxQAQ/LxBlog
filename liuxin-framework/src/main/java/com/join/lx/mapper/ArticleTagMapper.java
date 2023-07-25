package com.join.lx.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.join.lx.domain.entity.ArticleTag;
import org.springframework.stereotype.Repository;


/**
 * 文章标签关联表(ArticleTag)表数据库访问层
 *
 * @author makejava
 * @since 2022-10-09 16:57:31
 */
@Repository
public interface ArticleTagMapper extends BaseMapper<ArticleTag> {

}
