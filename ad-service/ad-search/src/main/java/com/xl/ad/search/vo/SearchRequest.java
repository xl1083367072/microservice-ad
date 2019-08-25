package com.xl.ad.search.vo;


import com.xl.ad.search.vo.feature.DistrictFeature;
import com.xl.ad.search.vo.feature.FeatureRelation;
import com.xl.ad.search.vo.feature.ItFeature;
import com.xl.ad.search.vo.feature.KeywordFeature;
import com.xl.ad.search.vo.media.AdSlot;
import com.xl.ad.search.vo.media.App;
import com.xl.ad.search.vo.media.Device;
import com.xl.ad.search.vo.media.Geo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchRequest {

//    媒体方请求标识
    private String mediaId;
//  请求基本信息
    private RequestInfo requestInfo;
//  匹配基本信息
    private FeatureInfo featureInfo;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RequestInfo{
//        请求id
        private String requestId;
        private List<AdSlot> adSlots;
        private App app;
        private Geo geo;
        private Device device;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FeatureInfo{
        private KeywordFeature keywordFeature;
        private ItFeature itFeature;
        private DistrictFeature districtFeature;
        private FeatureRelation relation = FeatureRelation.AND;
    }


}
