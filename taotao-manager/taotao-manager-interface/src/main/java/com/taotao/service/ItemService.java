package com.taotao.service;

import com.taotao.common.pojo.EasyUIDataGridResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemDesc;

public interface ItemService {
    TbItem getItemById(Long itemId);
    EasyUIDataGridResult<TbItem> getItemList(Integer page, Integer rows);
    TaotaoResult addItem(TbItem item, String desc);

    TbItemDesc getItemDescById(Long itemId);
}
