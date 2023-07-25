package com.join.lx.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.join.lx.constants.SystemConstants;
import com.join.lx.domain.ResponseResult;
import com.join.lx.domain.entity.Comment;
import com.join.lx.domain.vo.CommentVo;
import com.join.lx.domain.vo.PageVo;
import com.join.lx.enums.AppHttpCodeEnum;
import com.join.lx.exception.SystemException;
import com.join.lx.mapper.CommentMapper;
import com.join.lx.service.CommentService;
import com.join.lx.service.UserService;
import com.join.lx.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import java.util.List;

/**
 * 评论表(Comment)表服务实现类
 *
 * @author liuxin
 * @since 2022-04-27 21:02:22
 */
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {


    @Autowired
    private UserService userService;


    @Override
    public ResponseResult commentList(String commentType, Long articleId, Integer pageNum, Integer pageSize) {
        // 查询对应文章的根评论
        LambdaQueryWrapper<Comment> commentWrapper = new LambdaQueryWrapper<>();
        // 对articleId进行判断
        commentWrapper.eq(SystemConstants.COMMENT_TYPE_ARTICLE.equals((commentType)),Comment::getArticleId, articleId);
        // 根评论 rootId为 -1
        commentWrapper.eq(Comment::getRootId,SystemConstants.COMMENT_ROOT_ID);
        // 评论类型 判断
        commentWrapper.eq(Comment::getType,commentType);
        // 分页查询
        Page<Comment> page = new Page<>(pageNum,pageSize);
        page(page,commentWrapper);
        List<CommentVo> commentVoList = toCommentVoList(page.getRecords());

        // 查询所有根评论对应的子评论集合
        for (CommentVo commentVo : commentVoList) {
            List<CommentVo> children = getChildren(commentVo.getId());
            commentVo.setChildren(children);
        }

        return ResponseResult.okResult(new PageVo(commentVoList,page.getTotal()));
    }

    @Override
    public ResponseResult addComment(Comment comment) {
        if (!StringUtils.hasText(comment.getContent()))
            // 提示 评论不能为空
            throw new SystemException(AppHttpCodeEnum.COMMENT_NOT_NULL);

        save(comment);
        return ResponseResult.okResult();
    }


    /**
     *  根据根评论的id查询所有子评论
     * @param id
     * @return
     */
    private List<CommentVo> getChildren(Long id) {
        LambdaQueryWrapper<Comment> commentWrapper = new LambdaQueryWrapper<>();
        commentWrapper.eq(Comment::getRootId,id);
        commentWrapper.orderByAsc(Comment::getCreateTime);
        List<Comment> list = list(commentWrapper);
        return toCommentVoList(list);
    }


    private List<CommentVo> toCommentVoList(List<Comment> list){
        List<CommentVo> commentVos = BeanCopyUtils.copyBeanList(list, CommentVo.class);
        // 遍历Vo集合
        for (CommentVo commentVo : commentVos) {
            // 通过creatBy 查询用户的昵称并赋值
            String nickName = userService.getById(commentVo.getCreateBy()).getNickName();
            commentVo.setUsername(nickName);
            // 通过toCommentUserId 查询用户昵称
            // 如果toCommentUserId不为-1 才进行查询
            if (commentVo.getToCommentUserId()!=-1){
                String toCommentUserName = userService.getById(commentVo.getToCommentUserId()).getNickName();
                commentVo.setToCommentUserName(toCommentUserName);
            }

        }
        return commentVos;

    }
}
