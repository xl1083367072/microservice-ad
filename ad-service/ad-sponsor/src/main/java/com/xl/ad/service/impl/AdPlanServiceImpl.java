package com.xl.ad.service.impl;

import com.xl.ad.constant.CommonStatus;
import com.xl.ad.constant.Constants;
import com.xl.ad.dao.AdPlanRepository;
import com.xl.ad.entity.AdPlan;
import com.xl.ad.entity.AdUser;
import com.xl.ad.exception.AdException;
import com.xl.ad.service.AdPlanService;
import com.xl.ad.service.UserService;
import com.xl.ad.util.CommonUtils;
import com.xl.ad.vo.AdPlanGetRequest;
import com.xl.ad.vo.AdPlanRequest;
import com.xl.ad.vo.AdplanResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class AdPlanServiceImpl implements AdPlanService {

    private final AdPlanRepository adPlanRepository;
    private final UserService userService;

    @Autowired
    public AdPlanServiceImpl(AdPlanRepository adPlanRepository, UserService userService) {
        this.adPlanRepository = adPlanRepository;
        this.userService = userService;
    }

//    创建推广计划
    @Override
    @Transactional
    public AdplanResponse createPlan(AdPlanRequest request) throws AdException {
//        验证请求数据
        if(!request.createValidate()){
            throw new AdException(Constants.ErrorMsg.REQUEST_PARAM_ERROR);
        }
//        在业务层验证是否有要插入的外键关联
        AdUser adUser = userService.findByUserId(request.getUserId());
//        查看数据库有没有外键对应的记录
        if(adUser==null){
            throw new AdException(Constants.ErrorMsg.CAN_COT_FIND_RECORD);
        }
//        查看数据库是否已经有这条记录了
        AdPlan adPlan = adPlanRepository.findByUserIdAndPlanName(request.getUserId(), request.getPlanName());
        if(adPlan!=null){
            throw new AdException(Constants.ErrorMsg.ADPLAN_EXISTS_ERROR);
        }
        adPlan = adPlanRepository.save(new AdPlan(request.getUserId(), request.getPlanName(),
                CommonUtils.convertString2Date(request.getStartDate()),
                CommonUtils.convertString2Date(request.getEndDate())));
        return new AdplanResponse(adPlan.getId(),adPlan.getPlanName());
    }

//    根据id和用户id查找所有推广单元
    @Override
    public List<AdPlan> findAllByIdAndUserId(AdPlanGetRequest request) throws AdException {
        if(!request.validate()){
            throw new AdException(Constants.ErrorMsg.REQUEST_PARAM_ERROR);
        }
        return adPlanRepository.findAllByIdAndUserId(request.getIds(),request.getUserId());
    }

//    更新推广单元
    @Override
    @Transactional
    public AdplanResponse updatePlan(AdPlanRequest request) throws AdException {
        if(!request.updateValidate()){
            throw new AdException(Constants.ErrorMsg.REQUEST_PARAM_ERROR);
        }
        AdPlan adPlan = adPlanRepository.findByUserIdAndPlanName(request.getUserId(), request.getPlanName());
        if(adPlan==null){
            throw new AdException(Constants.ErrorMsg.CAN_COT_FIND_RECORD);
        }
        if(request.getPlanName()!=null){
            adPlan.setPlanName(request.getPlanName());
        }
        if(request.getStartDate()!=null){
            adPlan.setStartDate(CommonUtils.convertString2Date(request.getStartDate()));
        }
        if(request.getEndDate()!=null){
            adPlan.setEndDate(CommonUtils.convertString2Date(request.getEndDate()));
        }
        adPlan.setUpdateTime(new Date());
        adPlan = adPlanRepository.save(adPlan);
        return new AdplanResponse(adPlan.getId(),adPlan.getPlanName());
    }

//    删除推广单元
    @Override
    @Transactional
    public void deletePlan(AdPlanRequest request) throws AdException {
        if(!request.deleteValidate()){
            throw new AdException(Constants.ErrorMsg.REQUEST_PARAM_ERROR);
        }
        AdPlan adPlan = adPlanRepository.findByUserIdAndPlanName(request.getUserId(), request.getPlanName());
        if(adPlan==null){
            throw new AdException(Constants.ErrorMsg.CAN_COT_FIND_RECORD);
        }
//        不是删除，而是状态设为不可用
        adPlan.setPlanStatus(CommonStatus.INVALID.getStatus());
        adPlan.setUpdateTime(new Date());
        adPlanRepository.save(adPlan);
    }

    @Override
    public AdPlan findByPlanId(Long planId) {
        return adPlanRepository.findById(planId).get();
    }
}
