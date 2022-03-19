package com.tz.mall.search.service;

import com.tz.mall.search.vo.SearchParam;
import com.tz.mall.search.vo.SearchResult;

public interface MallSearchService {
    SearchResult search(SearchParam param);

}
