package com.xl.ad.index.creative;

import com.xl.ad.index.IndexAware;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class CreativeIndex implements IndexAware<Long,CreativeObject> {

    private static Map<Long,CreativeObject> map;

    static {
//        并发时会有多个线程更新map索引，防止数据错乱，用线程安全的
        map = new ConcurrentHashMap<>();
    }

    @Override
    public void add(Long key, CreativeObject value) {
        log.info("adCreative index: add -> {}",map);
        map.put(key,value);
    }

    @Override
    public void delete(Long key, CreativeObject value) {
        log.info("adCreative index: before delete -> {}",map);
        map.remove(key);
        log.info("adCreative index: after delete -> {}",map);
    }

    @Override
    public void update(Long key, CreativeObject value) {
        CreativeObject adPlanObject = map.get(key);
        log.info("adCreative index: before update -> {}",map);
        if(adPlanObject==null){
            map.put(key, value);
        }else {
            adPlanObject.update(value);
        }
        log.info("adCreative index: after update -> {}",map);
    }

    @Override
    public CreativeObject get(Long key) {
        log.info("adCreative index: get -> {}",map);
        return map.get(key);
    }
    
}
