package com.taotao.content.service;

import com.taotao.common.pojo.EasyUIDataGridResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.pojo.TbContent;

import java.util.List;

public interface ContentService {
    TaotaoResult addContent(TbContent tbContent);

    TaotaoResult updateContent(TbContent tbContent);

    List<TbContent> getContentByCid(Long cid);

    EasyUIDataGridResult<TbContent> getItemList(Long categoryId,Integer page, Integer rows);

    TaotaoResult delContent(List<Long> ids);
}
