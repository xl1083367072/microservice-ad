package com.xl.ad.search.vo;


import com.xl.ad.index.creative.CreativeObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchResponse {

//    AdSlot请求信息中的adSlotCode属性作为key，广告位id和创意映射
    private Map<String,List<Creative>> creatives = new HashMap<>();

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Creative{
        private Long adId;
        private String adUrl;
        private Integer width;
        private Integer height;
        private Integer type;
        private Integer materialType;
        private List<String> showMonitorUrl = new ArrayList<>(Arrays.asList(
                "www.example.com","www.example.com"
        ));
        private List<String> clickMonitorUrl = new ArrayList<>(Arrays.asList(
                "www.example.com","www.example.com"
        ));
    }

    public static Creative convert(CreativeObject creativeObject){
        Creative creative = new Creative();
        creative.setAdId(creativeObject.getAdId());
        creative.setAdUrl(creativeObject.getUrl());
        creative.setWidth(creativeObject.getWidth());
        creative.setHeight(creativeObject.getHeight());
        creative.setType(creativeObject.getType());
        creative.setMaterialType(creativeObject.getMaterialType());
        return creative;
    }

}
