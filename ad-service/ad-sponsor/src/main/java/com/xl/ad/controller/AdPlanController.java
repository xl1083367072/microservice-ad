package com.xl.ad.controller;

import com.alibaba.fastjson.JSON;
import com.xl.ad.entity.AdPlan;
import com.xl.ad.exception.AdException;
import com.xl.ad.service.AdPlanService;
import com.xl.ad.vo.AdPlanGetRequest;
import com.xl.ad.vo.AdPlanRequest;
import com.xl.ad.vo.AdplanResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
public class AdPlanController {

    @Autowired
    private AdPlanService adPlanService;

    @PostMapping("/create/plan")
    public AdplanResponse createPlan(@RequestBody AdPlanRequest request) throws AdException{
        log.info("ad-sponsor: createPlan -> {}", JSON.toJSONString(request));
        return adPlanService.createPlan(request);
    }

    @PostMapping("/get/plan")
    public List<AdPlan> findAllByIdAndUserId(@RequestBody AdPlanGetRequest request) throws AdException{
        log.info("ad-sponsor: findAllByIdAndUserId -> {}",JSON.toJSONString(request));
        return adPlanService.findAllByIdAndUserId(request);
    }

    @PutMapping("/update/plan")
    public AdplanResponse updatePlan(@RequestBody AdPlanRequest request) throws AdException{
        log.info("ad-sponsor: updatePlan -> {}",JSON.toJSONString(request));
        return adPlanService.updatePlan(request);
    }

    @DeleteMapping("/delete/plan")
    public void deletePlan(@RequestBody AdPlanRequest request) throws AdException{
        log.info("ad-sponsor: deletePlan -> {}",JSON.toJSONString(request));
        adPlanService.deletePlan(request);
    }


}
