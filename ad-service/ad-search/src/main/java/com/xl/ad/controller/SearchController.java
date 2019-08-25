package com.xl.ad.controller;

import com.alibaba.fastjson.JSON;
import com.xl.ad.search.Search;
import com.xl.ad.search.vo.SearchRequest;
import com.xl.ad.search.vo.SearchResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class SearchController {

    @Autowired
    private Search search;

    @PostMapping("/fetchAds")
    public SearchResponse fetchAds(@RequestBody SearchRequest request){
        log.info("fetchAds -> {}", JSON.toJSONString(request));
        return search.search(request);
    }

}
