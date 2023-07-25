package com.join.lx.service;

import com.join.lx.domain.ResponseResult;
import org.springframework.web.multipart.MultipartFile;

public interface OssUploadService {
    ResponseResult uploadImg(MultipartFile img);
}
