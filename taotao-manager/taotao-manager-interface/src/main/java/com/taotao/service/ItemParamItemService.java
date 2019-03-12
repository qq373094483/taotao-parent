package com.taotao.service;

import com.taotao.pojo.TbItemParamItem;
import com.taotao.pojo.TbItemParamItemExample;

import java.util.List;

public interface ItemParamItemService {
    TbItemParamItem selectByPrimaryKey(Long itemCatId);
    List<TbItemParamItem> selectByExample(TbItemParamItemExample example);

    TbItemParamItem selectByItemId(Long itemId);
}
