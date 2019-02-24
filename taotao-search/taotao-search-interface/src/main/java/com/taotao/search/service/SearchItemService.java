package com.taotao.search.service;

import com.taotao.common.pojo.TaotaoResult;

public interface SearchItemService {

    /**
     * 导入商品到solr索引库
     * @return
     */
    TaotaoResult importItemsToIndex();
}
