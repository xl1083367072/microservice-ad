package com.xl.ad.test;

import com.xl.ad.Application;
import com.xl.ad.entity.AdPlan;
import com.xl.ad.exception.AdException;
import com.xl.ad.service.AdPlanService;
import com.xl.ad.vo.AdPlanGetRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Application.class},webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class AdPlanServiceTest {

    @Autowired
    AdPlanService adPlanService;

    @Test
    public void run() throws AdException {
        List<AdPlan> adPlans = adPlanService.findAllByIdAndUserId(new AdPlanGetRequest(15L, Collections.singletonList(10L)));
        System.out.println(adPlans);
    }

}
