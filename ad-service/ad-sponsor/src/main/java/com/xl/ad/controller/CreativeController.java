package com.xl.ad.controller;

import com.alibaba.fastjson.JSON;
import com.xl.ad.service.CreativeService;
import com.xl.ad.vo.CreativeRequest;
import com.xl.ad.vo.CreativeResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class CreativeController {

    @Autowired
    private CreativeService creativeService;

    @PostMapping("/create/Creative")
    public CreativeResponse createCreative(@RequestBody CreativeRequest request){
        log.info("ad-sponsor: createCreative -> {}", JSON.toJSONString(request));
        return creativeService.createCreative(request);
    }

}
