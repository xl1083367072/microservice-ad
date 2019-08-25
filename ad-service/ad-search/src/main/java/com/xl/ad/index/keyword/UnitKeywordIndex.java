package com.xl.ad.index.keyword;

import com.xl.ad.index.IndexAware;
import com.xl.ad.util.CommonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;

@Slf4j
@Component
public class UnitKeywordIndex implements IndexAware<String, Set<Long>> {

//    一个关键字可以找到多个对应的推广单元，倒排索引
    private static Map<String,Set<Long>> unitMap;
//    一个推广单元也可以对应多个关键字，正向索引
    private static Map<Long,Set<String>> keywordMap;

    static {
        unitMap = new ConcurrentHashMap<>();
        keywordMap = new ConcurrentHashMap<>();
    }

    @Override
    public void add(String key, Set<Long> value) {
        log.info("unitKeyword index: before add -> {}",keywordMap);
//        如果key对应的value为空，则会创建一个ConcurrentSkipListSet对象作为值，并返回这个值
        Set<Long> units = CommonUtils.getOrCreate(key, unitMap, ConcurrentSkipListSet::new);
//        此时再添加进去
        units.addAll(value);
//        相反，正向索引的每个推广单元也都要插入进去这个关键词
        for (Long unit:value){
            Set<String> keywords = CommonUtils.getOrCreate(unit, keywordMap, ConcurrentSkipListSet::new);
            keywords.add(key);
        }
        log.info("unitKeyword index: after add -> {}",keywordMap);
    }

    @Override
    public void delete(String key,Set<Long> value) {
        log.info("unitKeyword index: before delete -> {}",keywordMap);
        Set<Long> units = CommonUtils.getOrCreate(key, unitMap, ConcurrentSkipListSet::new);
        units.removeAll(value);
        for (Long unit:value){
            Set<String> keywords = CommonUtils.getOrCreate(unit, keywordMap, ConcurrentSkipListSet::new);
            keywords.remove(key);
        }
        log.info("unitKeyword index: after delete -> {}",keywordMap);
    }

    @Override
    public void update(String key, Set<Long> value) {
        log.error("不支持更新");
    }

    @Override
    public Set<Long> get(String key) {
        log.info("unitKeyword index: get -> {}",keywordMap);
        if(StringUtils.isEmpty(key)){
            return Collections.emptySet();
        }
        Set<Long> units = unitMap.get(key);
        if(units==null){
            return Collections.emptySet();
        }
        return units;
    }

//    匹配这个推广单元是否包含了这些keyword
    public boolean match(Long unitId, List<String> keywords){
        if(keywordMap.containsKey(unitId)&& CollectionUtils.isNotEmpty(keywordMap.get(unitId))){
            Set<String> strings = keywordMap.get(unitId);
            return CollectionUtils.isSubCollection(keywords,strings);
        }
        return false;
    }

}
