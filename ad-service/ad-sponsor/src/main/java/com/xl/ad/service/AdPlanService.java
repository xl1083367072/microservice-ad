package com.xl.ad.service;

import com.xl.ad.entity.AdPlan;
import com.xl.ad.exception.AdException;
import com.xl.ad.vo.AdPlanGetRequest;
import com.xl.ad.vo.AdPlanRequest;
import com.xl.ad.vo.AdplanResponse;

import java.util.List;

public interface AdPlanService {


    AdplanResponse createPlan(AdPlanRequest request) throws AdException;

    List<AdPlan> findAllByIdAndUserId(AdPlanGetRequest request) throws AdException;

    AdplanResponse updatePlan(AdPlanRequest request) throws AdException;

    void deletePlan(AdPlanRequest request) throws AdException;

    AdPlan findByPlanId(Long planId);


}
