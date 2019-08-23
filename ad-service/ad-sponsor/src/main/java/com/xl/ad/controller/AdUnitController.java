package com.xl.ad.controller;

import com.alibaba.fastjson.JSON;
import com.xl.ad.exception.AdException;
import com.xl.ad.service.AdUnitService;
import com.xl.ad.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class AdUnitController {

    @Autowired
    private AdUnitService adUnitService;

    @PostMapping("/create/unit")
    public AdUnitResponse createAdUnit(@RequestBody AdUnitRequest request) throws AdException{
        log.info("ad-sponsor: createAdUnit -> {}", JSON.toJSONString(request));
        return adUnitService.createAdUnit(request);
    }

    @PostMapping("/create/unitKeyword")
    public AdUnitKeywordResponse createAdUnitKeyword(@RequestBody AdUnitKeywordRequest request) throws  AdException{
        log.info("ad-sponsor: createAdUnitKeyword -> {}",JSON.toJSONString(request));
        return adUnitService.createAdUnitKeyword(request);
    }

    @PostMapping("/create/unitIt")
    public AdUnitItResponse createAdUnitIt(@RequestBody AdUnitItRequest request) throws AdException{
        log.info("ad-sponsor: createAdUnitIt -> {}",JSON.toJSONString(request));
        return adUnitService.createAdUnitIt(request);
    }

    @PostMapping("/create/unitDistrict")
    public AdUnitDistrictResponse createAdUnitDistrict(@RequestBody AdUnitDistrictRequest request) throws AdException{
        log.info("ad-sponsor: createAdUnitDistrict -> {}",JSON.toJSONString(request));
        return adUnitService.createAdUnitDistrict(request);
    }

    @PostMapping("/create/creativeUnit")
    public CreativeUnitResponse createCreativeUnit(@RequestBody CreativeUnitRequest request) throws AdException{
        log.info("ad-sponsor: createCreativeUnit -> {}",JSON.toJSONString(request));
        return adUnitService.createCreativeUnit(request);
    }

}
