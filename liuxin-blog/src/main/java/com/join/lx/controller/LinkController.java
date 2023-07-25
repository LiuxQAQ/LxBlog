package com.join.lx.controller;


import com.join.lx.annotation.SystemLog;
import com.join.lx.domain.ResponseResult;
import com.join.lx.service.LinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/link")
public class LinkController {

    @Autowired
    private LinkService linkService;

    @GetMapping("/getAllLink")
    @SystemLog(businessName = "友链获取")
    public ResponseResult getAllLink(){
        return linkService.getAllLink();
    }

}
