package com.join.lx.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.join.lx.constants.SystemConstants;
import com.join.lx.domain.ResponseResult;
import com.join.lx.domain.dto.TagDto;
import com.join.lx.domain.dto.TagListDto;
import com.join.lx.domain.entity.Tag;
import com.join.lx.domain.vo.PageVo;
import com.join.lx.domain.vo.TagListVo;
import com.join.lx.domain.vo.TagVo;
import com.join.lx.enums.AppHttpCodeEnum;
import com.join.lx.exception.SystemException;
import com.join.lx.service.TagService;
import com.join.lx.mapper.TagMapper;
import com.join.lx.utils.BeanCopyUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.stream.Collectors;

/**
 *Error:Kotlin: Module was compiled with an incompatible version of Kotlin. The binary version of its metadata is 1.5.1, expected version is 1.1.16.
 */
@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag>
    implements TagService{

    @Override
    public ResponseResult<PageVo> pageTagList(Integer pageNum, Integer pageSize, TagListDto tagListDto) {


        LambdaQueryWrapper<Tag> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StringUtils.hasText(tagListDto.getName()),Tag::getName,tagListDto.getName());    //  还有非空的判断   要用三个参数的eq方法
        wrapper.eq(StringUtils.hasText(tagListDto.getRemark()),Tag::getRemark,tagListDto.getRemark());
        // 分页查询
        Page<Tag> page = new Page<>();
        page.setCurrent(pageNum);
        page.setSize(pageSize);
        page(page,wrapper);
        // 封装数据返回
        PageVo pageVo = new PageVo(page.getRecords(), page.getTotal());
        return ResponseResult.okResult(pageVo);

    }

    @Override
    public ResponseResult addTag(TagListDto tagListDto) {

        if (!StringUtils.hasText(tagListDto.getName()))
            throw new SystemException(AppHttpCodeEnum.TAG_NAME_NOT_NULL);

        if (!StringUtils.hasText(tagListDto.getRemark()))
            throw new SystemException(AppHttpCodeEnum.TAG_REMARK_NOT_NULL);

        Tag tag = BeanCopyUtils.copyBean(tagListDto, Tag.class);

        save(tag);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult deleteTag(Long id) {
        LambdaUpdateWrapper<Tag> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(Tag::getId,id)
                .set(Tag::getDelFlag,SystemConstants.TAG_DELETE);
        update(wrapper);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult getTag(Long id) {
        Tag tag = getById(id);
        TagVo tagVo = BeanCopyUtils.copyBean(tag, TagVo.class);
        return ResponseResult.okResult(tagVo);
    }

    @Override
    public ResponseResult updateTag(TagDto tagDto) {
        LambdaUpdateWrapper<Tag> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(Tag::getId,tagDto.getId())
                .set(Tag::getName,tagDto.getName())
                .set(Tag::getRemark,tagDto.getRemark());
        update(wrapper);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult listAllTag() {
        List<Tag> list = list();
        List<TagListVo> tagListVos = BeanCopyUtils.copyBeanList(list, TagListVo.class);
        return ResponseResult.okResult(tagListVos);
    }
}




