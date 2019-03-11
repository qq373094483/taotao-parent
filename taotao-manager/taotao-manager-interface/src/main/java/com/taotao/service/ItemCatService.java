package com.taotao.service;

import com.taotao.common.pojo.EasyUITreeNode;
import com.taotao.pojo.TbItemCat;

import java.util.List;

public interface ItemCatService {
    List<EasyUITreeNode> getItemCatList(Long parentId);

    TbItemCat selectByPrimaryKey(Long id);
}
