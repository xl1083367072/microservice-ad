package com.xl.ad.search;


import com.xl.ad.search.vo.SearchRequest;
import com.xl.ad.search.vo.SearchResponse;

public interface Search {

    SearchResponse search(SearchRequest request);

}
