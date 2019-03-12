package com.taotao.service.impl;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.taotao.mapper.TbItemParamItemMapper;
import com.taotao.pojo.TbItemParamItem;
import com.taotao.pojo.TbItemParamItemExample;
import com.taotao.service.ItemParamItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemParamItemServiceImpl implements ItemParamItemService {
    @Autowired
    private TbItemParamItemMapper tbItemParamItemMapper;
    @Override
    public TbItemParamItem selectByPrimaryKey(Long itemCatId) {
        return tbItemParamItemMapper.selectByPrimaryKey(itemCatId);
    }

    @Override
    public List<TbItemParamItem> selectByExample(TbItemParamItemExample example) {
        return tbItemParamItemMapper.selectByExampleWithBLOBs(example);
    }

    @Override
    public TbItemParamItem selectByItemId(Long itemId) {
        TbItemParamItemExample tbItemParamItemExample = new TbItemParamItemExample();
        TbItemParamItemExample.Criteria criteria = tbItemParamItemExample.createCriteria();
        criteria.andItemIdEqualTo(itemId);
        List<TbItemParamItem> tbItemParamItems = selectByExample(tbItemParamItemExample);
        if (CollectionUtils.isNotEmpty(tbItemParamItems)) {
            return tbItemParamItems.get(0);
        }
        return null;
    }
}
