package com.xl.ad.index;

import com.alibaba.fastjson.JSON;
import com.xl.ad.dump.DConstant;
import com.xl.ad.dump.table.*;
import com.xl.ad.handler.AdLevelDataHandler;
import com.xl.ad.mysql.constant.OpType;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

@Component
@DependsOn("dataTable")
public class IndexFileLoader {

//    服务启动就要加载全量索引
    @PostConstruct
    public void init(){
//        按层级顺序加载
        List<String> adPlans = loadDumpData(String.format("%s%s", DConstant.DATA_ROOT_DIR, DConstant.AD_PLAN));
        for (String adPlan:adPlans){
            AdLevelDataHandler.handleLevel2(JSON.parseObject(adPlan, AdPlanTable.class), OpType.ADD);
        }
        List<String> creatives = loadDumpData(String.format("%s%s", DConstant.DATA_ROOT_DIR, DConstant.AD_CREATIVE));
        for (String creative:creatives){
            AdLevelDataHandler.handleLevel2(JSON.parseObject(creative, CreativeTable.class), OpType.ADD);
        }
        List<String> adUnits = loadDumpData(String.format("%s%s", DConstant.DATA_ROOT_DIR, DConstant.AD_UNIT));
        for (String adUnit:adUnits){
            AdLevelDataHandler.handleLevel3(JSON.parseObject(adUnit, AdUnitTable.class), OpType.ADD);
        }
        List<String> creativeUnits = loadDumpData(String.format("%s%s", DConstant.DATA_ROOT_DIR, DConstant.AD_CREATIVE_UNIT));
        for (String creativeUnit:creativeUnits){
            AdLevelDataHandler.handleLevel3(JSON.parseObject(creativeUnit, AdUnitTable.class), OpType.ADD);
        }
        List<String> unitKeywords = loadDumpData(String.format("%s%s", DConstant.DATA_ROOT_DIR, DConstant.AD_UNIT_KEYWORD));
        for (String unitKeyword:unitKeywords){
            AdLevelDataHandler.handleLevel4(JSON.parseObject(unitKeyword, UnitKeywordTable.class), OpType.ADD);
        }
        List<String> unitIts = loadDumpData(String.format("%s%s", DConstant.DATA_ROOT_DIR, DConstant.AD_UNIT_IT));
        for (String unitIt:unitIts){
            AdLevelDataHandler.handleLevel4(JSON.parseObject(unitIt, UnitItTable.class), OpType.ADD);
        }
        List<String> unitDistricts = loadDumpData(String.format("%s%s", DConstant.DATA_ROOT_DIR, DConstant.AD_UNIT_DISTRICT));
        for (String unitDistrict:unitDistricts){
            AdLevelDataHandler.handleLevel4(JSON.parseObject(unitDistrict, UnitDistrictTable.class), OpType.ADD);
        }
    }

//    加载文件，文件是一行行的json字符串
    private List<String> loadDumpData(String fileName){
        try (BufferedReader br = Files.newBufferedReader(Paths.get(fileName))){
            return br.lines().collect(Collectors.toList());
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

}
