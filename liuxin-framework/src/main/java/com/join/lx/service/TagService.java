package com.join.lx.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.join.lx.domain.ResponseResult;
import com.join.lx.domain.dto.TagDto;
import com.join.lx.domain.dto.TagListDto;
import com.join.lx.domain.entity.Tag;
import com.join.lx.domain.vo.PageVo;

/**
 *
 */
public interface TagService extends IService<Tag> {

    ResponseResult<PageVo> pageTagList(Integer pageNum, Integer pageSize, TagListDto tagListDto);

    ResponseResult addTag(TagListDto tagListDto);

    ResponseResult deleteTag(Long id);

    ResponseResult getTag(Long id);

    ResponseResult updateTag(TagDto tagDto);

    ResponseResult listAllTag();

}
