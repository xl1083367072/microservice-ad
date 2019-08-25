package com.xl.ad.sender.index;

import com.alibaba.fastjson.JSON;
import com.xl.ad.dump.table.*;
import com.xl.ad.handler.AdLevelDataHandler;
import com.xl.ad.index.DataLevel;
import com.xl.ad.mysql.constant.Constant;
import com.xl.ad.mysql.dto.MySqlRowData;
import com.xl.ad.sender.Sender;
import com.xl.ad.util.CommonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Component("indexSender")
public class IndexSender implements Sender {

//    完成各个层级的增量数据投递
    @Override
    public void sender(MySqlRowData rowData) {
        String level = rowData.getLevel();
        if(level.equals(DataLevel.LEVEL2.getLevel())){
            level2Sender(rowData);
        }else if(level.equals(DataLevel.LEVEL3.getLevel())){
            level3Sender(rowData);
        }else if(level.equals(DataLevel.LEVEL4.getLevel())){
            level4Sender(rowData);
        }else {
            log.error("没有对应的索引层级 -> {}", JSON.toJSONString(rowData));
        }
    }

    private void level2Sender(MySqlRowData rowData){
//        如果是AdPlan表
        if(rowData.getTableName().equals(Constant.AD_PLAN_TABLE_INFO.TABLE_NAME)){
            List<AdPlanTable> adPlanTables = new ArrayList<>();
//            将rowData中的数据转换成增量索引处理类能处理的AdPlanTable

//            每一个对应一条记录
            for (Map<String,String> map:rowData.getFieldValueMap()){
                AdPlanTable adPlanTable = new AdPlanTable();
                map.forEach((column, value) -> {
                    switch (column){
                        case Constant.AD_PLAN_TABLE_INFO.COLUMN_ID:
                            adPlanTable.setId(Long.parseLong(value));
                            break;
                        case Constant.AD_PLAN_TABLE_INFO.COLUMN_USER_ID:
                            adPlanTable.setUserId(Long.parseLong(value));
                            break;
                        case Constant.AD_PLAN_TABLE_INFO.COLUMN_PLAN_STATUS:
                            adPlanTable.setPlanStatus(Integer.valueOf(value));
                            break;
                        case Constant.AD_PLAN_TABLE_INFO.COLUMN_START_DATE:
                            adPlanTable.setStartDate(CommonUtils.parseDateStirng(value));
                            break;
                        case Constant.AD_PLAN_TABLE_INFO.COLUMN_END_DATE:
                            adPlanTable.setEndDate(CommonUtils.parseDateStirng(value));
                            break;
                    }
                });
                adPlanTables.add(adPlanTable);
            }
            adPlanTables.forEach(adPlanTable -> AdLevelDataHandler.handleLevel2(adPlanTable,rowData.getOpType()));
        }else if(rowData.getTableName().equals(Constant.AD_CREATIVE_TABLE_INFO.TABLE_NAME)){
//            如果是Creative表
            List<CreativeTable> creativeTables = new ArrayList<>();
           for (Map<String,String> map:rowData.getFieldValueMap()){
               CreativeTable creativeTable = new CreativeTable();
               map.forEach((column, value) -> {
                   switch (column){
                       case Constant.AD_CREATIVE_TABLE_INFO.COLUMN_ID:
                           creativeTable.setAdId(Long.parseLong(value));
                           break;
                       case Constant.AD_CREATIVE_TABLE_INFO.COLUMN_TYPE:
                           creativeTable.setType(Integer.valueOf(value));
                           break;
                       case Constant.AD_CREATIVE_TABLE_INFO.COLUMN_MATERIAL_TYPE:
                           creativeTable.setMaterialType(Integer.valueOf(value));
                           break;
                       case Constant.AD_CREATIVE_TABLE_INFO.COLUMN_HEIGHT:
                           creativeTable.setHeight(Integer.valueOf(value));
                           break;
                       case Constant.AD_CREATIVE_TABLE_INFO.COLUMN_WIDTH:
                           creativeTable.setWidth(Integer.valueOf(value));
                           break;
                       case Constant.AD_CREATIVE_TABLE_INFO.COLUMN_AUDIT_STATUS:
                           creativeTable.setAuditStatus(Integer.valueOf(value));
                           break;
                       case Constant.AD_CREATIVE_TABLE_INFO.COLUMN_URL:
                           creativeTable.setUrl(value);
                           break;
                   }
               });
               creativeTables.add(creativeTable);
           }
           creativeTables.forEach(creativeTable -> AdLevelDataHandler.handleLevel2(creativeTable,rowData.getOpType()));
        }
    }

    private void level3Sender(MySqlRowData rowData){
        if(rowData.getTableName().equals(Constant.AD_UNIT_TABLE_INFO.TABLE_NAME)){
            List<AdUnitTable> adUnitTables = new ArrayList<>();
            for (Map<String,String> map:rowData.getFieldValueMap()){
                AdUnitTable adUnitTable = new AdUnitTable();
                map.forEach((column, value) -> {
                    switch (column){
                        case Constant.AD_UNIT_TABLE_INFO.COLUMN_ID:
                            adUnitTable.setUnitId(Long.parseLong(value));
                            break;
                        case Constant.AD_UNIT_TABLE_INFO.COLUMN_PLAN_ID:
                            adUnitTable.setPlanId(Long.parseLong(value));
                            break;
                        case Constant.AD_UNIT_TABLE_INFO.COLUMN_UNIT_STATUS:
                            adUnitTable.setUnitStatus(Integer.valueOf(value));
                            break;
                        case Constant.AD_UNIT_TABLE_INFO.COLUMN_POSITION_TYPE:
                            adUnitTable.setPositionType(Integer.valueOf(value));
                            break;
                    }
                });
                adUnitTables.add(adUnitTable);
            }
            adUnitTables.forEach(adUnitTable -> AdLevelDataHandler.handleLevel3(adUnitTable,rowData.getOpType()));
        }else if(rowData.getTableName().equals(Constant.AD_CREATIVE_UNIT_TABLE_INFO.TABLE_NAME)){
            List<CreativeUnitTable> creativeUnitTables = new ArrayList<>();
            for (Map<String,String> map:rowData.getFieldValueMap()){
                CreativeUnitTable creativeUnitTable = new CreativeUnitTable();
                map.forEach((column, value) -> {
                    switch (column){
                        case Constant.AD_CREATIVE_UNIT_TABLE_INFO.COLUMN_CREATIVE_ID:
                            creativeUnitTable.setAdId(Long.parseLong(value));
                            break;
                        case Constant.AD_CREATIVE_UNIT_TABLE_INFO.COLUMN_UNIT_ID:
                            creativeUnitTable.setUnitId(Long.parseLong(value));
                            break;
                    }
                });
                creativeUnitTables.add(creativeUnitTable);
            }
            creativeUnitTables.forEach(creativeUnitTable -> AdLevelDataHandler.handleLevel3(creativeUnitTable,rowData.getOpType()));
        }
    }

    private void level4Sender(MySqlRowData rowData){
        if(rowData.getTableName().equals(Constant.AD_UNIT_KEYWORD_TABLE_INFO.TABLE_NAME)){
            List<UnitKeywordTable> unitKeywordTables = new ArrayList<>();
            for (Map<String,String> map:rowData.getFieldValueMap()){
                UnitKeywordTable unitKeywordTable = new UnitKeywordTable();
                map.forEach((column, value) -> {
                    switch (column){
                        case Constant.AD_UNIT_KEYWORD_TABLE_INFO.COLUMN_UNIT_ID:
                            unitKeywordTable.setUnitId(Long.parseLong(value));
                            break;
                        case Constant.AD_UNIT_KEYWORD_TABLE_INFO.COLUMN_KEYWORD:
                            unitKeywordTable.setKeyword(value);
                            break;
                    }
                });
                unitKeywordTables.add(unitKeywordTable);
            }
            unitKeywordTables.forEach(unitKeywordTable -> AdLevelDataHandler.handleLevel4(unitKeywordTable,rowData.getOpType()));
        }else if(rowData.getTableName().equals(Constant.AD_UNIT_IT_TABLE_INFO.TABLE_NAME)){
            List<UnitItTable> unitItTables = new ArrayList<>();
            for (Map<String,String> map:rowData.getFieldValueMap()){
                UnitItTable unitItTable = new UnitItTable();
                map.forEach((column, value) -> {
                    switch (column){
                        case Constant.AD_UNIT_IT_TABLE_INFO.COLUMN_UNIT_ID:
                            unitItTable.setUnitId(Long.parseLong(value));
                            break;
                        case Constant.AD_UNIT_IT_TABLE_INFO.COLUMN_IT_TAG:
                            unitItTable.setItTag(value);
                            break;
                    }
                });
                unitItTables.add(unitItTable);
            }
            unitItTables.forEach(unitItTable -> AdLevelDataHandler.handleLevel4(unitItTable,rowData.getOpType()));
        }else if(rowData.getTableName().equals(Constant.AD_UNIT_DISTRICT_TABLE_INFO.TABLE_NAME)){
            List<UnitDistrictTable> unitDistrictTables = new ArrayList<>();
            for (Map<String,String> map:rowData.getFieldValueMap()){
                UnitDistrictTable unitDistrictTable = new UnitDistrictTable();
                map.forEach((column, value) -> {
                    switch (column){
                        case Constant.AD_UNIT_DISTRICT_TABLE_INFO.COLUMN_UNIT_ID:
                            unitDistrictTable.setUnitId(Long.parseLong(value));
                            break;
                        case Constant.AD_UNIT_DISTRICT_TABLE_INFO.COLUMN_PROVINCE:
                            unitDistrictTable.setProvince(value);
                            break;
                        case Constant.AD_UNIT_DISTRICT_TABLE_INFO.COLUMN_CITY:
                            unitDistrictTable.setCity(value);
                            break;
                    }
                });
                unitDistrictTables.add(unitDistrictTable);
            }
            unitDistrictTables.forEach(unitDistrictTable -> AdLevelDataHandler.handleLevel4(unitDistrictTable,rowData.getOpType()));
        }
    }

}
