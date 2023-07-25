package com.join.lx.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.join.lx.domain.ResponseResult;
import com.join.lx.domain.entity.Link;


/**
 * 友链(Link)表服务接口
 *
 * @author makejava
 * @since 2022-04-26 18:53:01
 */
public interface LinkService extends IService<Link> {

    ResponseResult getAllLink();
}

