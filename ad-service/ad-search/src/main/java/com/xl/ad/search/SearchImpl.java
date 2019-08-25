package com.xl.ad.search;

import com.alibaba.fastjson.JSON;
import com.xl.ad.index.CommonStatus;
import com.xl.ad.index.DataTable;
import com.xl.ad.index.adUnit.AdUnitIndex;
import com.xl.ad.index.adUnit.AdUnitObject;
import com.xl.ad.index.creative.CreativeIndex;
import com.xl.ad.index.creative.CreativeObject;
import com.xl.ad.index.creativeUnit.CreativeUnitIndex;
import com.xl.ad.index.district.UnitDistrictIndex;
import com.xl.ad.index.it.UnitItIndex;
import com.xl.ad.index.keyword.UnitKeywordIndex;
import com.xl.ad.search.vo.SearchRequest;
import com.xl.ad.search.vo.SearchResponse;
import com.xl.ad.search.vo.feature.DistrictFeature;
import com.xl.ad.search.vo.feature.FeatureRelation;
import com.xl.ad.search.vo.feature.ItFeature;
import com.xl.ad.search.vo.feature.KeywordFeature;
import com.xl.ad.search.vo.media.AdSlot;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
public class SearchImpl implements Search{

//    根据媒体方的请求，层层过滤，筛选符合的创意
    @Override
    public SearchResponse search(SearchRequest request) {
//        流量类型
        List<AdSlot> adSlots = request.getRequestInfo().getAdSlots();
//        各种限制
        KeywordFeature keywordFeature = request.getFeatureInfo().getKeywordFeature();
        ItFeature itFeature = request.getFeatureInfo().getItFeature();
        DistrictFeature districtFeature = request.getFeatureInfo().getDistrictFeature();
        FeatureRelation relation = request.getFeatureInfo().getRelation();

//        封装响应信息
        SearchResponse response = new SearchResponse();
        Map<String, List<SearchResponse.Creative>> adSlotCreatives = response.getCreatives();

//        过滤目的是过滤出符合条件的最终有哪些推广单元,它们对应的创意
//      第一层过滤，筛选符合流量类型的初始推广单元
        for (AdSlot adSlot:adSlots) {
            Set<Long> result;
            Set<Long> unitIds = DataTable.of(AdUnitIndex.class).match(adSlot.getPositionType());
//            第二层过滤，限制过滤
//        如果限制是And要求
            if (relation == FeatureRelation.AND) {
                filterKeywordFeature(unitIds, keywordFeature);
                filterItFeature(unitIds,itFeature);
                filterDistrictFeature(unitIds,districtFeature);
//                经过层层过滤后的推广单元
                result = unitIds;
            }else {
//                或限制
                result = getOrFeature(unitIds,keywordFeature,itFeature,districtFeature);
            }
//            获取两次过滤后的推广单元
            List<AdUnitObject> adUnitObjects = DataTable.of(AdUnitIndex.class).fetch(unitIds);
//            第三层过滤，按推广计划状态过滤
            filterByStatus(adUnitObjects,CommonStatus.VALID);
//            获取过滤后的推广单元对应的创意id
            List<Long> ads = DataTable.of(CreativeUnitIndex.class).getAds(adUnitObjects);
//            根据创意id获取创意
            List<CreativeObject> creatives = DataTable.of(CreativeIndex.class).getCreative(ads);
//            第四层过滤，根据广告位的其他信息进行过滤
            filterByAdSlot(creatives,adSlot.getWidth(),adSlot.getHeight(),adSlot.getType());
//            填充响应对象
            adSlotCreatives.put(adSlot.getAdSlotCode(),fill(creatives));
        }
        log.info("请求信息 -> {} 响应信息 -> {}", JSON.toJSONString(request),JSON.toJSONString(response));
        return response;
    }

//    封装响应对象
    private List<SearchResponse.Creative> fill(List<CreativeObject> creativeObjects){
        if(CollectionUtils.isEmpty(creativeObjects)){
            return Collections.emptyList();
        }
        CreativeObject random = creativeObjects.get(Math.abs(new Random().nextInt()) % creativeObjects.size());
        return Collections.singletonList(SearchResponse.convert(random));
    }

    private void filterByAdSlot(List<CreativeObject> creatives,Integer width,Integer height,List<Integer> type){
        if(CollectionUtils.isEmpty(creatives)){
            return;
        }
        CollectionUtils.filter(creatives,creativeObject -> creativeObject.getAuditStatus().
                equals(CommonStatus.VALID.getStatus()) && creativeObject.getWidth().equals(width) &&
                creativeObject.getHeight().equals(height) && type.contains(creativeObject.getType()));
    }

    private void filterByStatus(List<AdUnitObject> adUnitObjects, CommonStatus status){
        if(CollectionUtils.isEmpty(adUnitObjects)){
            return;
        }
        CollectionUtils.filter(adUnitObjects,adUnitObject ->
                adUnitObject.getUnitStatus().equals(status.getStatus())
                && adUnitObject.getAdPlanObject().getPlanStatus().equals(status.getStatus()));
    }

    private Set<Long> getOrFeature(Collection<Long> unitIds,KeywordFeature keywordFeature,
                                   ItFeature itFeature,DistrictFeature districtFeature){
        if(CollectionUtils.isEmpty(unitIds)){
            return Collections.emptySet();
        }
//        因为是或过滤，保存三各自的副本
        Set<Long> keywordFilter = new HashSet<>(unitIds);
        Set<Long> itFilter = new HashSet<>(unitIds);
        Set<Long> districtFilter = new HashSet<>(unitIds);
        filterKeywordFeature(keywordFilter,keywordFeature);
        filterItFeature(itFilter,itFeature);
        filterDistrictFeature(districtFilter,districtFeature);
//        返回三者过滤结果的并集
        return new HashSet<>(CollectionUtils.union(CollectionUtils.union(
                keywordFilter,itFilter
        ),districtFilter));
    }


    private void filterKeywordFeature(Collection<Long> unitIds,KeywordFeature keywordFeature){
        if(CollectionUtils.isEmpty(unitIds)){
            return;
        }
//        如果请求中的关键词限制不为空,则进行过滤,得到符合关键词限制的推广单元
        if(CollectionUtils.isNotEmpty(keywordFeature.getKeywords())){
            CollectionUtils.filter(unitIds,unitId -> DataTable.of(UnitKeywordIndex.class)
            .match(unitId,keywordFeature.getKeywords()));
        }
    }

    private void filterItFeature(Collection<Long> unitIds,ItFeature itFeature){
        if(CollectionUtils.isEmpty(unitIds)){
            return;
        }
//        如果请求中的关键词限制不为空,则进行过滤,得到符合关键词限制的推广单元
        if(CollectionUtils.isNotEmpty(itFeature.getIts())){
            CollectionUtils.filter(unitIds,unitId -> DataTable.of(UnitItIndex.class)
                    .match(unitId,itFeature.getIts()));
        }
    }

    private void filterDistrictFeature(Collection<Long> unitIds,DistrictFeature districtFeature){
        if(CollectionUtils.isEmpty(unitIds)){
            return;
        }
//        如果请求中的关键词限制不为空,则进行过滤,得到符合关键词限制的推广单元
        if(CollectionUtils.isNotEmpty(districtFeature.getDistricts())){
            CollectionUtils.filter(unitIds,unitId -> DataTable.of(UnitDistrictIndex.class)
                    .match(unitId,districtFeature.getDistricts()));
        }
    }

}
