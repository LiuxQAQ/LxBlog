package com.join.lx.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.join.lx.constants.SystemConstants;
import com.join.lx.domain.ResponseResult;
import com.join.lx.domain.entity.Link;
import com.join.lx.domain.vo.LinkVo;
import com.join.lx.mapper.LinkMapper;
import com.join.lx.service.LinkService;
import com.join.lx.utils.BeanCopyUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 友链(Link)表服务实现类
 *
 * @author makejava
 * @since 2022-04-26 18:53:02
 */
@Service
public class LinkServiceImpl extends ServiceImpl<LinkMapper, Link> implements LinkService {

    @Override
    public ResponseResult getAllLink() {

        // 查询所有审核通过的友链
        LambdaQueryWrapper<Link> linkWrapper = new LambdaQueryWrapper<>();
        linkWrapper.eq(Link::getStatus, SystemConstants.Link_STATUS_NORMAL);
        List<Link> list = list(linkWrapper);
        // 转换成vo
        List<LinkVo> linkVos = BeanCopyUtils.copyBeanList(list, LinkVo.class);

        return ResponseResult.okResult(linkVos);
    }
}
