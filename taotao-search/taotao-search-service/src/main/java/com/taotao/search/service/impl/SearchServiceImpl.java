package com.taotao.search.service.impl;

import com.taotao.common.pojo.SearchResult;
import com.taotao.search.service.SearchService;
import org.springframework.stereotype.Service;

@Service
public class SearchServiceImpl implements SearchService {
    @Override
    public SearchResult search(String queryString, Integer page, Integer searchResultRows) {
        return null;
    }
}
