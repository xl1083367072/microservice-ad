package com.xl.ad.index.adPlan;

import com.xl.ad.index.IndexAware;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

//推广计划索引服务实现
@Slf4j
@Component
public class AdPlanIndex implements IndexAware<Long,AdPlanObject> {

    private static Map<Long,AdPlanObject> map;

    static {
//        并发时会有多个线程更新map索引，防止数据错乱，用线程安全的
        map = new ConcurrentHashMap<>();
    }

    @Override
    public void add(Long key, AdPlanObject value) {
        log.info("adPlan index: before add -> {}",map);
        map.put(key,value);
        log.info("adPlan index: after add -> {}",map);
    }

    @Override
    public void delete(Long key, AdPlanObject value) {
        log.info("adPlan index: before delete -> {}",map);
        map.remove(key);
        log.info("adPlan index: after delete -> {}",map);
    }

    @Override
    public void update(Long key, AdPlanObject value) {
        AdPlanObject adPlanObject = map.get(key);
        log.info("adPlan index: before update -> {}",map);
        if(adPlanObject==null){
            map.put(key, value);
        }else {
            adPlanObject.update(value);
        }
        log.info("adPlan index: after update -> {}",map);
    }

    @Override
    public AdPlanObject get(Long key) {
        log.info("adPlan index: get -> {}",map);
        return map.get(key);
    }
}
