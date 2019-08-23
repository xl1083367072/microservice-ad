package com.xl.ad.controller;

import com.xl.ad.entity.AdPlan;
import com.xl.ad.hsytrix.SponsorClientHystrix;
import com.xl.ad.vo.AdPlanGetRequest;
import com.xl.ad.vo.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

//feign声明式调用，配置断路器
@FeignClient(value = "ad-sponsor",fallback = SponsorClientHystrix.class)
public interface SponsorClient {

    @RequestMapping(value = "/ad-sponsor/get/plan",method = RequestMethod.POST)
    Result<List<AdPlan>> getAdPlansByRibbon(@RequestBody AdPlanGetRequest request);

}
