package com.xl.ad.service;

import com.alibaba.fastjson.JSON;
import com.xl.ad.Application;
import com.xl.ad.constant.CommonStatus;
import com.xl.ad.dao.*;
import com.xl.ad.dump.DConstant;
import com.xl.ad.dump.table.*;
import com.xl.ad.entity.AdPlan;
import com.xl.ad.entity.AdUnit;
import com.xl.ad.entity.Creative;
import com.xl.ad.entity.unit_condition.AdUnitDistrict;
import com.xl.ad.entity.unit_condition.AdUnitIt;
import com.xl.ad.entity.unit_condition.AdUnitKeyword;
import com.xl.ad.entity.unit_condition.CreativeUnit;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.BufferedWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Application.class},webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class DumpDataService {

    @Autowired
    private AdPlanRepository adPlanRepository;
    @Autowired
    private AdUnitRepository adUnitRepository;
    @Autowired
    private CreativeRepository creativeRepository;
    @Autowired
    private CreativeUnitRepository creativeUnitRepository;
    @Autowired
    private AdUnitKeywordRepository adUnitKeywordRepository;
    @Autowired
    private AdUnitItRepository adUnitItRepository;
    @Autowired
    private AdUnitDistrictRepository adUnitDistrictRepository;

    @Test
    public void update(){
        dumpAdPlanTable(String.format("%s%s", DConstant.DATA_ROOT_DIR,DConstant.AD_PLAN));
        dumpAdUnitTable(String.format("%s%s", DConstant.DATA_ROOT_DIR,DConstant.AD_UNIT));
        dumpCreativeTable(String.format("%s%s", DConstant.DATA_ROOT_DIR,DConstant.AD_CREATIVE));
        dumpCreativeUnitTable(String.format("%s%s", DConstant.DATA_ROOT_DIR,DConstant.AD_CREATIVE_UNIT));
        dumpUnitKeyword(String.format("%s%s", DConstant.DATA_ROOT_DIR,DConstant.AD_UNIT_KEYWORD));
        dumpUnitIt(String.format("%s%s", DConstant.DATA_ROOT_DIR,DConstant.AD_UNIT_IT));
        dumpUnitDistrict(String.format("%s%s", DConstant.DATA_ROOT_DIR,DConstant.AD_UNIT_DISTRICT));
    }

    private void dumpAdPlanTable(String table){
        List<AdPlan> adPlans = adPlanRepository.findAllByPlanStatus(CommonStatus.VALID.getStatus());
        if(CollectionUtils.isEmpty(adPlans)){
            return;
        }
        List<AdPlanTable> adPlanTables = new ArrayList<>();
        adPlans.forEach(adPlan -> adPlanTables.add(new AdPlanTable(
                adPlan.getId(),adPlan.getUserId(),
                adPlan.getPlanStatus(),adPlan.getStartDate(),
                adPlan.getEndDate()
        )));
        Path path = Paths.get(table);
        try (BufferedWriter writer = Files.newBufferedWriter(path)){
            for (AdPlanTable adPlanTable:adPlanTables){
                writer.write(JSON.toJSONString(adPlanTable));
                writer.newLine();
            }
        }catch (Exception e){
            log.info("导出ad_plan表失败");
        }
    }

    private void dumpAdUnitTable(String table){
        List<AdUnit> adUnits = adUnitRepository.findAllByUnitStatus(CommonStatus.VALID.getStatus());
        if(CollectionUtils.isEmpty(adUnits)){
            return;
        }
        List<AdUnitTable> adUnitTables = new ArrayList<>();
        adUnits.forEach(adUnit -> adUnitTables.add(new AdUnitTable(
                adUnit.getId(),adUnit.getPlanId(),
                adUnit.getUnitStatus(),adUnit.getPositionType()

        )));
        Path path = Paths.get(table);
        try (BufferedWriter writer = Files.newBufferedWriter(path)){
            for (AdUnitTable adUnitTable:adUnitTables){
                writer.write(JSON.toJSONString(adUnitTable));
                writer.newLine();
            }
        }catch (Exception e){
            log.info("导出ad_unit表失败");
        }
    }

    private void dumpCreativeTable(String table){
        List<Creative> creatives = creativeRepository.findAll();
        if(CollectionUtils.isEmpty(creatives)){
            return;
        }
        List<CreativeTable> creativeTables = new ArrayList<>();
        creatives.forEach(creative -> creativeTables.add(new CreativeTable(
                creative.getId(),creative.getName(),
                creative.getType(),creative.getMaterialType(),
                creative.getHeight(),creative.getWidth(),
                creative.getAuditStatus(),creative.getUrl()
        )));
        Path path = Paths.get(table);
        try (BufferedWriter writer = Files.newBufferedWriter(path)){
            for (CreativeTable creativeTable:creativeTables){
                writer.write(JSON.toJSONString(creativeTable));
                writer.newLine();
            }
        }catch (Exception e){
            log.info("导出creative表失败");
        }
    }

    private void dumpCreativeUnitTable(String table){
        List<CreativeUnit> creativeUnits = creativeUnitRepository.findAll();
        if(CollectionUtils.isEmpty(creativeUnits)){
            return;
        }
        List<CreativeUnitTable> creativeUnitTables = new ArrayList<>();
        creativeUnits.forEach(creativeUnit -> creativeUnitTables.add(new CreativeUnitTable(
                creativeUnit.getCreativeId(),creativeUnit.getUnitId()
        )));
        Path path = Paths.get(table);
        try (BufferedWriter writer = Files.newBufferedWriter(path)){
            for (CreativeUnitTable creativeUnitTable:creativeUnitTables){
                writer.write(JSON.toJSONString(creativeUnitTable));
                writer.newLine();
            }
        }catch (Exception e){
            log.info("导出creative_unit表失败");
        }
    }

    private void dumpUnitKeyword(String table){
        List<AdUnitKeyword> unitKeywords = adUnitKeywordRepository.findAll();
        if(CollectionUtils.isEmpty(unitKeywords)){
            return;
        }
        List<UnitKeywordTable> unitKeywordTables = new ArrayList<>();
        unitKeywords.forEach(adUnitKeyword -> unitKeywordTables.add(new UnitKeywordTable(
                adUnitKeyword.getUnitId(),adUnitKeyword.getKeyword()
        )));
        Path path = Paths.get(table);
        try (BufferedWriter writer = Files.newBufferedWriter(path)){
            for (UnitKeywordTable unitKeywordTable:unitKeywordTables){
                writer.write(JSON.toJSONString(unitKeywordTable));
                writer.newLine();
            }
        }catch (Exception e){
            log.info("导出unit_keyword表失败");
        }
    }

    private void dumpUnitIt(String table){
        List<AdUnitIt> adUnitIts = adUnitItRepository.findAll();
        if(CollectionUtils.isEmpty(adUnitIts)){
            return;
        }
        List<UnitItTable> unitItTables = new ArrayList<>();
        adUnitIts.forEach(adUnitIt -> unitItTables.add(new UnitItTable(
                adUnitIt.getUnitId(),adUnitIt.getItTag()
        )));
        Path path = Paths.get(table);
        try (BufferedWriter writer = Files.newBufferedWriter(path)){
            for (UnitItTable unitItTable:unitItTables){
                writer.write(JSON.toJSONString(unitItTable));
                writer.newLine();
            }
        }catch (Exception e){
            log.info("导出unit_it表失败");
        }
    }

    private void dumpUnitDistrict(String table){
        List<AdUnitDistrict> adUnitDistricts = adUnitDistrictRepository.findAll();
        if(CollectionUtils.isEmpty(adUnitDistricts)){
            return;
        }
        List<UnitDistrictTable> unitDistrictTables = new ArrayList<>();
        adUnitDistricts.forEach(adUnitDistrict -> unitDistrictTables.add(new UnitDistrictTable(
                adUnitDistrict.getUnitId(),adUnitDistrict.getProvince(),adUnitDistrict.getCity()
        )));
        Path path = Paths.get(table);
        try (BufferedWriter writer = Files.newBufferedWriter(path)){
            for (UnitDistrictTable unitDistrictTable:unitDistrictTables){
                writer.write(JSON.toJSONString(unitDistrictTable));
                writer.newLine();
            }
        }catch (Exception e){
            log.info("导出unit_district表失败");
        }
    }

}
