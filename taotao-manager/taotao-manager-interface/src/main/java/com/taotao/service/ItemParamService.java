package com.taotao.service;

import com.taotao.bo.ItemParamBO;
import com.taotao.common.pojo.EasyUIDataGridResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.pojo.*;

import java.util.List;

public interface ItemParamService {
    TbItemParam selectByPrimaryKey(Long id);
    List<TbItemParam> selectByExample(TbItemParamExample example);
    List<TbItemParam> selectByItemCatId(Long itemCatId);
    EasyUIDataGridResult<TbItemParam> getItemParamList(Integer page, Integer rows);

    List<TbItemParam> getTbItemParamsForPage(Integer page, Integer rows);

    EasyUIDataGridResult<ItemParamBO> getItemParamBOList(Integer page, Integer rows);

    TaotaoResult addItemParam(String paramData, Long itemCatId);

    TaotaoResult delItemParam(List<Long> ids);
}
