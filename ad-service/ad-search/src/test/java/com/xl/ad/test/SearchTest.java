package com.xl.ad.test;

import com.alibaba.fastjson.JSON;
import com.xl.ad.Application;
import com.xl.ad.search.Search;
import com.xl.ad.search.vo.SearchRequest;
import com.xl.ad.search.vo.feature.DistrictFeature;
import com.xl.ad.search.vo.feature.FeatureRelation;
import com.xl.ad.search.vo.feature.ItFeature;
import com.xl.ad.search.vo.feature.KeywordFeature;
import com.xl.ad.search.vo.media.AdSlot;
import com.xl.ad.search.vo.media.App;
import com.xl.ad.search.vo.media.Device;
import com.xl.ad.search.vo.media.Geo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Application.class},webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class SearchTest {

    @Autowired
    private Search search;

    @Test
    public void run(){

        SearchRequest request1 = new SearchRequest();
        SearchRequest.RequestInfo requestInfo1 = new SearchRequest.RequestInfo("test-a", Collections.singletonList(
                new AdSlot("ad-a",1,1080,720, Arrays.asList(1,2),10000)
        ),buildApp(),buildGeo(),buildDevice());
        request1.setRequestInfo(requestInfo1);

        SearchRequest.FeatureInfo featureInfo1 = buildFeatureInfo(Arrays.asList("宝马","奥迪"),
                Arrays.asList("台球","游泳"),Collections.singletonList(new DistrictFeature.ProvinceCity(
                        "安徽省","合肥市"
                )),FeatureRelation.OR);
        request1.setFeatureInfo(featureInfo1);
        System.out.println("=====================================");
        System.out.println(JSON.toJSONString(request1));
        System.out.println(JSON.toJSONString(search.search(request1)));

        SearchRequest request2 = new SearchRequest();
        SearchRequest.RequestInfo requestInfo2 = new SearchRequest.RequestInfo("test-a", Collections.singletonList(
                new AdSlot("ad-b",1,1080,720, Arrays.asList(1,2),10000)
        ),buildApp(),buildGeo(),buildDevice());
        request2.setRequestInfo(requestInfo2);

        SearchRequest.FeatureInfo featureInfo2 = buildFeatureInfo(Arrays.asList("宝马","奥迪","兰博基尼"),
                Arrays.asList("台球","游泳"),Collections.singletonList(new DistrictFeature.ProvinceCity(
                        "安徽省","合肥市"
                )),FeatureRelation.AND);
        request2.setFeatureInfo(featureInfo2);
        System.out.println("=====================================");
        System.out.println(JSON.toJSONString(request2));
        System.out.println(JSON.toJSONString(search.search(request2)));

    }


    private SearchRequest.FeatureInfo buildFeatureInfo(List<String> keywords, List<String> its,
                                                       List<DistrictFeature.ProvinceCity> districts,
                                                       FeatureRelation relation){
        return new SearchRequest.FeatureInfo(
                new KeywordFeature(keywords),new ItFeature(its),
                new DistrictFeature(districts),relation
        );
    }


    private App buildApp(){
        return new App("app","app","app","app");
    }

    private Geo buildGeo(){
        return new Geo(121.5f,36.6f,"苏州","江苏");
    }

    private Device buildDevice(){
        return new Device("device","device","device","device","device","device","device");
    }

}
