package com.xl.ad.index.it;

import com.xl.ad.index.IndexAware;
import com.xl.ad.util.CommonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;

@Slf4j
@Component
public class UnitItIndex implements IndexAware<String, Set<Long>> {

    private static Map<String,Set<Long>> itUnitMap;
    private static Map<Long,Set<String>> unitItMap;

    static{
        itUnitMap = new ConcurrentHashMap<>();
        unitItMap = new ConcurrentHashMap<>();
    }

    @Override
    public void add(String key, Set<Long> value) {
        log.info("unitIt index: before add -> {}",unitItMap);
        Set<Long> units = CommonUtils.getOrCreate(key, itUnitMap, ConcurrentSkipListSet::new);
        units.addAll(value);
        for (Long unit:value){
            Set<String> its = CommonUtils.getOrCreate(unit, unitItMap, ConcurrentSkipListSet::new);
            its.add(key);
        }
        log.info("unitIt index: after add -> {}",unitItMap);
    }

    @Override
    public void delete(String key, Set<Long> value) {
        log.info("unitIt index: before delete -> {}",unitItMap);
        Set<Long> units = CommonUtils.getOrCreate(key, itUnitMap, ConcurrentSkipListSet::new);
        units.removeAll(value);
        for (Long unit:value){
            Set<String> its = CommonUtils.getOrCreate(unit, unitItMap, ConcurrentSkipListSet::new);
            its.remove(key);
        }
        log.info("unitIt index: after delete -> {}",unitItMap);
    }

    @Override
    public void update(String key, Set<Long> value) {
        log.error("不支持更新");
    }

    @Override
    public Set<Long> get(String key) {
        log.info("unitIt index: get -> {}",unitItMap);
        return itUnitMap.get(key);
    }


    public boolean match(Long unitId, List<String> itTags){
        if(unitItMap.containsKey(unitId) && CollectionUtils.isNotEmpty(
                unitItMap.get(unitId)
        )){
            Set<String> currentTags = unitItMap.get(unitId);
            return CollectionUtils.isSubCollection(itTags,currentTags);
        }
        return false;
    }

}
