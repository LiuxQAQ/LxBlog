package com.join.lx.controller;


import com.join.lx.annotation.SystemLog;
import com.join.lx.domain.ResponseResult;
import com.join.lx.service.OssUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class UploadController {

    @Autowired
    private OssUploadService ossUploadService;

    @PostMapping("/upload")
    @SystemLog(businessName = "头像上传")
    public ResponseResult uploadImg(MultipartFile img){
        return ossUploadService.uploadImg(img);
    }
}
