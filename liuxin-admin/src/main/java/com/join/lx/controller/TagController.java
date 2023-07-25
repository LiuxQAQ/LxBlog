package com.join.lx.controller;


import com.join.lx.annotation.SystemLog;
import com.join.lx.domain.ResponseResult;
import com.join.lx.domain.dto.TagDto;
import com.join.lx.domain.dto.TagListDto;
import com.join.lx.domain.vo.PageVo;
import com.join.lx.service.TagService;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/content/tag")
public class TagController {

    @Autowired
    private TagService tagService;

    @GetMapping("/list")
    @PreAuthorize("@ps.hasPermission('content:tag:index')")
    public ResponseResult<PageVo> list(Integer pageNum, Integer pageSize, TagListDto tagListDto){
        return tagService.pageTagList(pageNum,pageSize,tagListDto);
    }

    @PostMapping
    @PreAuthorize("@ps.hasPermission('content:tag:index')")
    public ResponseResult addTag(@RequestBody TagListDto tagListDto){
        return tagService.addTag(tagListDto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("@ps.hasPermission('content:tag:index')")
    @SystemLog(businessName = "删除对应标签")
    public ResponseResult deleteTag(@PathVariable("id") Long id){
        return tagService.deleteTag(id);
    }

    @GetMapping("/{id}")
    @PreAuthorize("@ps.hasPermission('content:tag:index')")
    @SystemLog(businessName = "获取标签详情信息")
    public ResponseResult getTag(@PathVariable("id") Long id){
        return tagService.getTag(id);
    }

    @PutMapping
    @PreAuthorize("@ps.hasPermission('content:tag:index')")
    @SystemLog(businessName = "修改标签信息")
    public ResponseResult updateTag(@RequestBody TagDto tagDto){
        return tagService.updateTag(tagDto);
    }

    @GetMapping("listAllTag")
    @PreAuthorize("@ps.hasPermission('content:tag:index')")
    @SystemLog(businessName = "获取标签列表")
    public ResponseResult listAllTag(){
        return tagService.listAllTag();
    }
}
