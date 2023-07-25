package com.join.lx.controller;


import com.join.lx.annotation.SystemLog;
import com.join.lx.constants.SystemConstants;
import com.join.lx.domain.ResponseResult;
import com.join.lx.domain.dto.AddCommentDto;
import com.join.lx.domain.entity.Comment;
import com.join.lx.service.CommentService;
import com.join.lx.utils.BeanCopyUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequestMapping("/comment")
@Api(description = "评论相关接口")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @GetMapping("/commentList")
    @SystemLog(businessName = "文章评论获取")
    public ResponseResult commentList(Long articleId,Integer pageNum,Integer pageSize){
        return commentService.commentList(SystemConstants.COMMENT_TYPE_ARTICLE,articleId,pageNum,pageSize);
    }





    @PostMapping
    // 其实最好是用DTO接受json
    @SystemLog(businessName = "添加评论")
    public ResponseResult addComment(@RequestBody AddCommentDto addCommentDto){
        Comment comment = BeanCopyUtils.copyBean(addCommentDto, Comment.class);
        return commentService.addComment(comment);
    }



    @GetMapping("/linkCommentList")
    @SystemLog(businessName = "友链评论获取")
    @ApiOperation(value = "友链评论列表",notes = "获取一页友链评论")  // notes  描述信息
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "页号"),
            @ApiImplicitParam(name = "pageSize",value = "大小")
    })
    public ResponseResult linkCommentList(Integer pageNum,Integer pageSize){
        return commentService.commentList(SystemConstants.COMMENT_TYPE_FRIENDLINK,null,pageNum,pageSize);

    }
}
