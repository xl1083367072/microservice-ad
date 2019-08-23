package com.xl.ad.controller;

import com.alibaba.fastjson.JSON;
import com.xl.ad.annotation.IgnoreResponseAdvice;
import com.xl.ad.entity.AdPlan;
import com.xl.ad.exception.AdException;
import com.xl.ad.vo.AdPlanGetRequest;
import com.xl.ad.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
public class AdPlanController {

    /*@Autowired
    private RestTemplate restTemplate;*/

    @Autowired
    private SponsorClient sponsorClient;

    @SuppressWarnings("all")
    @IgnoreResponseAdvice
    @PostMapping("/getAdPlansByRibbon")
    public Result<List<AdPlan>> getAdPlansByRibbon(@RequestBody AdPlanGetRequest request) throws AdException {
        log.info("ad-search: getAdPlansByRibbon -> {}", JSON.toJSONString(request));
       /* return restTemplate.postForEntity("http://ad-sponsor/ad-sponsor/get/plan",
                request,Result.class).getBody();*/
//       feign调用
       return sponsorClient.getAdPlansByRibbon(request);
    }



}
