package com.join.lx.domain.vo;


import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArticleDetailVo {


    private Long id;

    /**
     * 标题
     */
    private String title;

    /**
     * 文章内容
     */
    private String content;

    /**
     * 文章摘要
     */
    private String summary;

    /**
     * 所属分类id
     */
    private Long categoryId;


    private String categoryName;

    /**
     * 缩略图
     */
    private String thumbnail;



    /**
     * 访问量
     */
    private Long viewCount;



    /**
     *
     */
    private Long createBy;

    /**
     *
     */
    private Date createTime;




}
