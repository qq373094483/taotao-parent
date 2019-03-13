package com.taotao.service;

import com.taotao.bo.ItemParamItemBO;
import com.taotao.common.pojo.EasyUIDataGridResult;
import com.taotao.pojo.TbItemParamItem;
import com.taotao.pojo.TbItemParamItemExample;

import java.util.List;

public interface ItemParamItemService {
    TbItemParamItem selectByPrimaryKey(Long id);

    ItemParamItemBO getItemParamItemBOByPrimaryKey(Long id);

    List<TbItemParamItem> selectByExample(TbItemParamItemExample example);

    TbItemParamItem selectByItemId(Long itemId);

    List<TbItemParamItem> getTbItemParamsItemForPage(Integer page, Integer rows);

    EasyUIDataGridResult<ItemParamItemBO> getItemParamItemBOList(Integer page, Integer rows);

    int addItemParam(String paramData, Long itemId);

    int updateByPrimaryKeySelective(TbItemParamItem tbItemParamItem);

    int delItemParam(List<Long> ids);
}
