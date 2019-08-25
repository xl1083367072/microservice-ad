package com.xl.ad.index.adUnit;

import com.xl.ad.index.IndexAware;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class AdUnitIndex implements IndexAware<Long, AdUnitObject> {

    private static Map<Long,AdUnitObject> map;
    static {
        map = new ConcurrentHashMap<>();
    }

//    匹配请求的广告位类型
    public Set<Long> match(int positionType){
        Set<Long> unitIds = new HashSet<>();
        map.forEach((unitId, adUnitObject) -> {
            if(AdUnitObject.isAdSlotType(positionType,adUnitObject.getPositionType())){
                unitIds.add(unitId);
            }
        });
        return unitIds;
    }

    public List<AdUnitObject> fetch(Set<Long> unitIds){
        if(CollectionUtils.isEmpty(unitIds)){
            return Collections.emptyList();
        }
        List<AdUnitObject> adUnitObjects = new ArrayList<>();
        unitIds.forEach(unitId -> {
            AdUnitObject adUnitObject = get(unitId);
            if(adUnitObject==null) {
                log.error("没有找到索引对应对象 -> {}", unitId);
                return;
            }
            adUnitObjects.add(adUnitObject);
        });
        return adUnitObjects;
    }

    @Override
    public void add(Long key, AdUnitObject value) {
        log.info("adUnit index: before add -> {}",map);
        map.put(key,value);
        log.info("adUnit index: after add -> {}",map);
    }

    @Override
    public void delete(Long key,AdUnitObject value) {
        log.info("adUnit index: before delete -> {}",map);
        map.remove(key);
        log.info("adUnit index: after delete -> {}",map);
    }

    @Override
    public void update(Long key, AdUnitObject value) {
        log.info("adUnit index: before update -> {}",map);
        AdUnitObject adUnitObject = map.get(key);
        if(adUnitObject==null){
            map.put(key, value);
        }else {
            adUnitObject.update(value);
        }
        log.info("adUnit index: after update -> {}",map);
    }

    @Override
    public AdUnitObject get(Long key) {
        log.info("adUnit index: get -> {}",map);
        return map.get(key);
    }
}
