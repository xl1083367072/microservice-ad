package com.xl.ad.index.creativeUnit;

import com.xl.ad.index.IndexAware;
import com.xl.ad.index.adUnit.AdUnitObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;

@Slf4j
@Component
public class CreativeUnitIndex implements IndexAware<String, CreativeUnitObject> {

//    根据unitId查创意对象
    private static Map<String,CreativeUnitObject> map;
//    两个一对多
    private static Map<Long, Set<Long>> creativeUnitMap;
    private static Map<Long,Set<Long>> unitCreativeMap;

    static {
        map = new ConcurrentHashMap<>();
        creativeUnitMap = new ConcurrentHashMap<>();
        unitCreativeMap = new ConcurrentHashMap<>();
    }
    @Override
    public void add(String key, CreativeUnitObject value) {
        log.info("creativeUnit index: before add -> {}",map);
        map.put(key,value);
        Set<Long> units = creativeUnitMap.get(value.getAdId());
        if(CollectionUtils.isEmpty(units)){
            units = new ConcurrentSkipListSet<>();
            creativeUnitMap.put(value.getAdId(), units);
        }
        units.add(value.getUnitId());
        Set<Long> ads = unitCreativeMap.get(value.getUnitId());
        if(CollectionUtils.isEmpty(ads)){
            ads = new ConcurrentSkipListSet<>();
            unitCreativeMap.put(value.getUnitId(),ads);
        }
        ads.add(value.getAdId());
        log.info("creativeUnit index: after add -> {}",map);
    }

    @Override
    public void delete(String key, CreativeUnitObject value) {
        log.info("creativeUnit index: before delete -> {}",map);
        map.remove(key);
        Set<Long> units = creativeUnitMap.get(value.getAdId());
        if(CollectionUtils.isNotEmpty(units)){
            units.remove(value.getUnitId());
        }
        Set<Long> ads = unitCreativeMap.get(value.getUnitId());
        if(CollectionUtils.isNotEmpty(ads)){
            ads.remove(value.getAdId());
        }
        log.info("creativeUnit index: after delete -> {}",map);
    }

    @Override
    public void update(String key, CreativeUnitObject value) {
        log.error("不支持更新");
    }

    @Override
    public CreativeUnitObject get(String key) {
        log.info("creativeUnit index: get -> {}",map);
        return map.get(key);
    }

//    根据推广单元获得与之对应的创意id
    public List<Long> getAds(List<AdUnitObject> adUnitObjects){
        if(CollectionUtils.isEmpty(adUnitObjects)){
            return Collections.emptyList();
        }
        List<Long> adList = new ArrayList<>();
        for (AdUnitObject object:adUnitObjects){
            Set<Long> ads = unitCreativeMap.get(object.getUnitId());
            if(CollectionUtils.isNotEmpty(ads)) {
                adList.addAll(ads);
            }
        }
        return adList;
    }

}
