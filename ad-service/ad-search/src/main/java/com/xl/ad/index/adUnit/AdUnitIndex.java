package com.xl.ad.index.adUnit;

import com.xl.ad.index.IndexAware;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class AdUnitIndex implements IndexAware<Long, AdUnitObject> {

    private static Map<Long,AdUnitObject> map;
    static {
        map = new ConcurrentHashMap<>();
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
