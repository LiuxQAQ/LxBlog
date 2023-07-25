package com.join.lx.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.join.lx.domain.ResponseResult;
import com.join.lx.domain.entity.Comment;


/**
 * 评论表(Comment)表服务接口
 *
 * @author makejava
 * @since 2022-04-27 21:02:22
 */
public interface CommentService extends IService<Comment> {

    ResponseResult commentList(String commentType, Long articleId, Integer pageNum, Integer pageSize);

    ResponseResult addComment(Comment comment);
}

