package com.taotao.service;

import com.taotao.common.pojo.EasyUIDataGridResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemDesc;

import java.util.List;

public interface ItemService {
    TbItem getItemById(Long itemId);
    EasyUIDataGridResult<TbItem> getItemList(Integer page, Integer rows);
    TaotaoResult<Long> addItem(TbItem item, String desc);

    TaotaoResult delItem(List<Long> ids);

    TbItemDesc getItemDescById(Long itemId);
}
