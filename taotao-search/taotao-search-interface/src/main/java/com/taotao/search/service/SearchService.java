package com.taotao.search.service;

import com.taotao.common.pojo.SearchResult;

public interface SearchService {
    SearchResult search(String queryString, Integer page, Integer searchResultRows);
}
