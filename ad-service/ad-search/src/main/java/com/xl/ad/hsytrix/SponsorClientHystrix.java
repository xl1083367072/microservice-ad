package com.xl.ad.hsytrix;

import com.xl.ad.controller.SponsorClient;
import com.xl.ad.entity.AdPlan;
import com.xl.ad.vo.AdPlanGetRequest;
import com.xl.ad.vo.Result;
import org.springframework.stereotype.Component;

import java.util.List;

//断路器
@Component
public class SponsorClientHystrix implements SponsorClient {

    @Override
    public Result<List<AdPlan>> getAdPlansByRibbon(AdPlanGetRequest request) {
        return new Result<>(-1,"sponsor服务不可用");
    }

}
