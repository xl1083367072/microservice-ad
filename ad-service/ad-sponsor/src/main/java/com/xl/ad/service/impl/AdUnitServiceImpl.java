package com.xl.ad.service.impl;

import com.xl.ad.constant.Constants;
import com.xl.ad.dao.*;
import com.xl.ad.entity.AdPlan;
import com.xl.ad.entity.AdUnit;
import com.xl.ad.entity.unit_condition.AdUnitDistrict;
import com.xl.ad.entity.unit_condition.AdUnitIt;
import com.xl.ad.entity.unit_condition.AdUnitKeyword;
import com.xl.ad.entity.unit_condition.CreativeUnit;
import com.xl.ad.exception.AdException;
import com.xl.ad.service.AdUnitService;
import com.xl.ad.service.AdPlanService;
import com.xl.ad.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdUnitServiceImpl implements AdUnitService {

    private final AdUnitRepository adUnitRepository;
    private final AdPlanService adPlanService;
    private final AdUnitKeywordRepository KeywordRepository;
    private final AdUnitItRepository itRepository;
    private final AdUnitDistrictRepository districtRepository;
    private final CreativeRepository creativeRepository;
    private final CreativeUnitRepository creativeUnitRepository;

    @Autowired
    public AdUnitServiceImpl(AdUnitRepository adUnitRepository, AdPlanService adPlanService, AdUnitKeywordRepository keywordRepository, AdUnitItRepository itRepository, AdUnitDistrictRepository districtRepository, CreativeRepository creativeRepository, CreativeUnitRepository creativeUnitRepository) {
        this.adUnitRepository = adUnitRepository;
        this.adPlanService = adPlanService;
        KeywordRepository = keywordRepository;
        this.itRepository = itRepository;
        this.districtRepository = districtRepository;
        this.creativeRepository = creativeRepository;
        this.creativeUnitRepository = creativeUnitRepository;
    }

//    创建推广单元
    @Transactional
    @Override
    public AdUnitResponse createAdUnit(AdUnitRequest request) throws AdException {
        if(!request.validate()){
            throw new AdException(Constants.ErrorMsg.REQUEST_PARAM_ERROR);
        }
//        先确认外键关联记录存在
        AdPlan adPlan = adPlanService.findByPlanId(request.getPlanId());
        if(adPlan==null){
            throw new AdException(Constants.ErrorMsg.CAN_COT_FIND_RECORD);
        }
//        再查是否已经有这条记录了
        AdUnit adUnit = adUnitRepository.findByPlanIdAndUnitName(request.getPlanId(), request.getUnitName());
        if(adUnit!=null){
            throw new AdException(Constants.ErrorMsg.ADUNIT_EXISTS_ERROR);
        }
        adUnit = adUnitRepository.save(new AdUnit(request.getPlanId(), request.getUnitName(),
                request.getPositionType(), request.getBudget()));
        return new AdUnitResponse(adUnit.getId(),adUnit.getUnitName());
    }

//    创建推广单元关键词限制
    @Override
    public AdUnitKeywordResponse createAdUnitKeyword(AdUnitKeywordRequest request) throws AdException {
        List<Long> unitIds = request.getUnitKeywords().stream().
                map(AdUnitKeywordRequest.UnitKeyword::getUnitId).
                collect(Collectors.toList());
//        判断外键关键是否存在
        if(!isUnitIdsExists(unitIds)){
            throw new AdException(Constants.ErrorMsg.REQUEST_PARAM_ERROR);
        }
//        存放创建成功后的id
        List<Long> ids = Collections.emptyList();
//        AdUnitKeyword虽然和request中集合属性一样，但是类型不一样
//        而添加数据库的实体必须是AdUnitKeyword，所以这里是做个转换
        List<AdUnitKeyword> adUnitKeywords = new ArrayList<>();
        if(!CollectionUtils.isEmpty(request.getUnitKeywords())){
            request.getUnitKeywords().forEach(unitKeyword -> adUnitKeywords.add(
                    new AdUnitKeyword(unitKeyword.getUnitId(),unitKeyword.getKeyword())));
//            插入成功后返回id集合
            ids = KeywordRepository.saveAll(adUnitKeywords).stream().
                    map(AdUnitKeyword::getId).collect(Collectors.toList());
        }
        return new AdUnitKeywordResponse(ids);
    }

//    创建推广单元兴趣标签限制
    @Override
    public AdUnitItResponse createAdUnitIt(AdUnitItRequest request) throws AdException {
        List<Long> unitIds = request.getUnitKeywords().stream().
                map(AdUnitItRequest.UnitIt::getUnitId)
                .collect(Collectors.toList());
        if(!isUnitIdsExists(unitIds)){
            throw new AdException(Constants.ErrorMsg.REQUEST_PARAM_ERROR);
        }
        List<Long> ids = Collections.emptyList();
        List<AdUnitIt> adUnitIts = new ArrayList<>();
        if(!CollectionUtils.isEmpty(request.getUnitKeywords())){
            request.getUnitKeywords().forEach(unitIt -> adUnitIts.add(
                    new AdUnitIt(unitIt.getUnitId(),unitIt.getItTag())));
            ids = itRepository.saveAll(adUnitIts).stream().map(AdUnitIt::getId)
                    .collect(Collectors.toList());
        }
        return new AdUnitItResponse(ids);
    }

//    创建推广单元地域限制
    @Override
    public AdUnitDistrictResponse createAdUnitDistrict(AdUnitDistrictRequest request) throws AdException {
        List<Long> unitIds = request.getUnitDistricts().stream()
                .map(AdUnitDistrictRequest.UnitDistrict::getUnitId)
                .collect(Collectors.toList());
        if(!isUnitIdsExists(unitIds)){
            throw new AdException(Constants.ErrorMsg.REQUEST_PARAM_ERROR);
        }
        List<Long> ids = Collections.emptyList();
        List<AdUnitDistrict> adUnitDistricts = new ArrayList<>();
        if(!CollectionUtils.isEmpty(request.getUnitDistricts())){
            request.getUnitDistricts().forEach(unitDistrict -> adUnitDistricts.add(
                    new AdUnitDistrict(unitDistrict.getUnitId(),unitDistrict.getProvince(),
                            unitDistrict.getCity())));
            ids = districtRepository.saveAll(adUnitDistricts).stream()
                    .map(AdUnitDistrict::getId).collect(Collectors.toList());
        }
        return new AdUnitDistrictResponse(ids);
    }

//    创建创意和推广单元的多对多关系
    @Override
    public CreativeUnitResponse createCreativeUnit(CreativeUnitRequest request) throws AdException {
        List<Long> creativeIds = request.getUnitItems().stream().
                map(CreativeUnitRequest.UnitItems::getCreativeId).
                collect(Collectors.toList());
        List<Long> unitIds = request.getUnitItems().stream().
                map(CreativeUnitRequest.UnitItems::getUnitId)
                .collect(Collectors.toList());
        if(!(isCreativeExists(creativeIds)&&isUnitIdsExists(unitIds))){
            throw new AdException(Constants.ErrorMsg.REQUEST_PARAM_ERROR);
        }
        List<Long> ids = Collections.emptyList();
        List<CreativeUnit> creativeUnits = new ArrayList<>();
        if(!CollectionUtils.isEmpty(request.getUnitItems())){
            request.getUnitItems().forEach(unitItems -> creativeUnits.add(
                    new CreativeUnit(unitItems.getCreativeId(),unitItems.getUnitId())
            ));
            ids = creativeUnitRepository.saveAll(creativeUnits).stream().
                        map(CreativeUnit::getId).collect(Collectors.toList());
        }
        return new CreativeUnitResponse(ids);
    }

    private boolean isUnitIdsExists(List<Long> unitIds){
//        判断广告单元id是否都存在且不为空
        return adUnitRepository.findAllById(unitIds).size() == new HashSet<>(unitIds).size();
    }

    private boolean isCreativeExists(List<Long> creativeIds){
        return creativeRepository.findAllById(creativeIds).size()== new HashSet<>().size();
    }
}
