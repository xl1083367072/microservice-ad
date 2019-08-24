package com.xl.ad.handler;

import com.alibaba.fastjson.JSON;
import com.xl.ad.dump.table.*;
import com.xl.ad.index.DataTable;
import com.xl.ad.index.IndexAware;
import com.xl.ad.index.adPlan.AdPlanIndex;
import com.xl.ad.index.adPlan.AdPlanObject;
import com.xl.ad.index.adUnit.AdUnitIndex;
import com.xl.ad.index.adUnit.AdUnitObject;
import com.xl.ad.index.creative.CreativeIndex;
import com.xl.ad.index.creative.CreativeObject;
import com.xl.ad.index.creativeUnit.CreativeUnitIndex;
import com.xl.ad.index.creativeUnit.CreativeUnitObject;
import com.xl.ad.index.district.UnitDistrictIndex;
import com.xl.ad.index.it.UnitItIndex;
import com.xl.ad.index.keyword.UnitKeywordIndex;
import com.xl.ad.mysql.constant.OpType;
import com.xl.ad.util.CommonUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

//索引之间存在层级关系，比如推广单元和推广计划关联
//加载全量索引是增量索引添加的一种特殊实现
@Slf4j
public class AdLevelDataHandler {

//    第二层级索引处理-AdPlanObject，此层级索引不与其他索引相关联
    public static void handleLevel2(AdPlanTable adPlanTable,OpType opType){
//        AdPlanTable对应的是索引文件，AdPlanObject对应的是索引，要转换
        AdPlanObject adPlanObject = new AdPlanObject(
                adPlanTable.getId(),adPlanTable.getUserId(),
                adPlanTable.getPlanStatus(),adPlanTable.getStartDate(),
                adPlanTable.getEndDate()
        );
//        处理索引操作
        handleBinlogEvent(DataTable.of(AdPlanIndex.class),adPlanObject.getPlanId(),adPlanObject,opType);
    }

//    第二层级索引-CreativeObject
    public static void handleLevel2(CreativeTable creativeTable,OpType opType){
        CreativeObject creativeObject = new CreativeObject(
                creativeTable.getAdId(),creativeTable.getName(),
                creativeTable.getType(),creativeTable.getMaterialType(),
                creativeTable.getHeight(),creativeTable.getWidth(),
                creativeTable.getAuditStatus(),creativeTable.getUrl()
        );
        handleBinlogEvent(DataTable.of(CreativeIndex.class),creativeObject.getAdId(),creativeObject,opType);
    }


//    第三层级索引-AdUnitObject，会有关联关系
    public static void handleLevel3(AdUnitTable adUnitTable, OpType opType){
//        验证是否已经有关联的索引对象存在
        AdPlanObject adPlanObject = DataTable.of(AdPlanIndex.class).get(adUnitTable.getPlanId());
        if(adPlanObject==null){
            log.error("handleLevel3 没有对应的关联索引 -> {}",adUnitTable.getPlanId());
            return;
        }
        AdUnitObject adUnitObject = new AdUnitObject(
                adUnitTable.getUnitId(),adUnitTable.getPlanId(),
                adUnitTable.getUnitStatus(),adUnitTable.getPositionType(),
                adPlanObject
        );
        handleBinlogEvent(DataTable.of(AdUnitIndex.class),adUnitObject.getUnitId(),adUnitObject, opType);
    }

//    第三层级索引-creativeUnitObject
    public static void handleLevel3(CreativeUnitTable creativeUnitTable,OpType opType){
        if(opType == OpType.UPDATE){
            log.error("不支持更新");
            return;
        }
        CreativeObject creativeObject = DataTable.of(CreativeIndex.class).get(creativeUnitTable.getAdId());
        AdUnitObject adUnitObject = DataTable.of(AdUnitIndex.class).get(creativeUnitTable.getUnitId());
        if(creativeObject==null||adUnitObject==null){
            log.error("handleLevel3 没有对应的关联索引 -> {}", JSON.toJSONString(creativeUnitTable));
            return;
        }
        CreativeUnitObject creativeUnitObject = new CreativeUnitObject(
                creativeUnitTable.getAdId(),creativeUnitTable.getUnitId()
        );
        handleBinlogEvent(DataTable.of(CreativeUnitIndex.class),CommonUtils.concat(
                creativeUnitObject.getAdId().toString(),
                creativeUnitObject.getUnitId().toString()),
                creativeUnitObject,opType
                );
    }

//    第四层级索引-UnitKeyword
    public static void handleLevel4(UnitKeywordTable unitKeywordTable,OpType opType){
        if (opType == OpType.UPDATE){
            log.error("不支持更新");
            return;
        }
        AdUnitObject adUnitObject = DataTable.of(AdUnitIndex.class).get(unitKeywordTable.getUnitId());
        if(adUnitObject==null){
            log.error("handleLevel4 没有对应的关联索引 -> {}",unitKeywordTable.getUnitId());
            return;
        }
        Set<Long> unitId = new HashSet<>(Collections.singleton(unitKeywordTable.getUnitId()));
        handleBinlogEvent(DataTable.of(UnitKeywordIndex.class),unitKeywordTable.getKeyword(),unitId,opType);
    }

    //    第四层级索引-unitIt
    public static void handleLevel4(UnitItTable unitItTable,OpType opType){
        if (opType == OpType.UPDATE){
            log.error("不支持更新");
            return;
        }
        AdUnitObject adUnitObject = DataTable.of(AdUnitIndex.class).get(unitItTable.getUnitId());
        if(adUnitObject==null){
            log.error("handleLevel4 没有对应的关联索引 -> {}",unitItTable.getUnitId());
            return;
        }
        Set<Long> unitId = new HashSet<>(Collections.singleton(unitItTable.getUnitId()));
        handleBinlogEvent(DataTable.of(UnitItIndex.class),unitItTable.getItTag(),unitId,opType);
    }

    //    第四层级索引-unitDistrict
    public static void handleLevel4(UnitDistrictTable unitDistrictTable,OpType opType){
        if (opType == OpType.UPDATE){
            log.error("不支持更新");
            return;
        }
        AdUnitObject adUnitObject = DataTable.of(AdUnitIndex.class).get(unitDistrictTable.getUnitId());
        if(adUnitObject==null){
            log.error("handleLevel4 没有对应的关联索引 -> {}",unitDistrictTable.getUnitId());
            return;
        }
        String key = CommonUtils.concat(unitDistrictTable.getProvince(),unitDistrictTable.getCity());
        Set<Long> unitId = new HashSet<>(Collections.singleton(unitDistrictTable.getUnitId()));
        handleBinlogEvent(DataTable.of(UnitDistrictIndex.class),key,unitId,opType);
    }

//    全量索引和增量索引统一处理
    public static <K,V> void  handleBinlogEvent(IndexAware<K,V> index,K key,V value,OpType opType){
        switch (opType){
            case ADD:
                index.add(key,value);
                break;
            case UPDATE:
                index.update(key,value);
                break;
            case DELETE:
                index.delete(key,value);
            default:
                break;
        }
    }

}
