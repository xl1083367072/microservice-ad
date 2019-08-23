package com.xl.ad.index.district;

import com.xl.ad.index.IndexAware;
import com.xl.ad.util.CommonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;

@Slf4j
@Component
public class UnitDistrictIndex implements IndexAware<String, Set<Long>> {

    private static Map<String,Set<Long>> districtUnitMap;
    private static Map<Long,Set<String>> unitDistrictMap;

    static{
        districtUnitMap = new ConcurrentHashMap<>();
        unitDistrictMap = new ConcurrentHashMap<>();
    }

    @Override
    public void add(String key, Set<Long> value) {
        log.info("unitDistrict index: before add -> {}",unitDistrictMap);
        Set<Long> units = CommonUtils.getOrCreate(key, districtUnitMap, ConcurrentSkipListSet::new);
        units.addAll(value);
        for (Long unit:value){
            Set<String> its = CommonUtils.getOrCreate(unit, unitDistrictMap, ConcurrentSkipListSet::new);
            its.add(key);
        }
        log.info("unitDistrict index: after add -> {}",unitDistrictMap);
    }

    @Override
    public void delete(String key, Set<Long> value) {
        log.info("unitDistrict index: before delete -> {}",unitDistrictMap);
        Set<Long> units = CommonUtils.getOrCreate(key, districtUnitMap, ConcurrentSkipListSet::new);
        units.removeAll(value);
        for (Long unit:value){
            Set<String> its = CommonUtils.getOrCreate(unit, unitDistrictMap, ConcurrentSkipListSet::new);
            its.remove(key);
        }
        log.info("unitDistrict index: after delete -> {}",unitDistrictMap);
    }

    @Override
    public void update(String key, Set<Long> value) {
        log.error("不支持更新");
    }

    @Override
    public Set<Long> get(String key) {
        log.info("unitDistrict index: get -> {}",unitDistrictMap);
        return districtUnitMap.get(key);
    }
}
