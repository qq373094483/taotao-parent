package com.taotao.service.impl;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taotao.bo.ItemParamItemBO;
import com.taotao.common.pojo.EasyUIDataGridResult;
import com.taotao.mapper.TbItemMapper;
import com.taotao.mapper.TbItemParamItemMapper;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemParamItem;
import com.taotao.pojo.TbItemParamItemExample;
import com.taotao.service.ItemParamItemService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ItemParamItemServiceImpl implements ItemParamItemService {
    @Autowired
    private TbItemParamItemMapper tbItemParamItemMapper;
    @Autowired
    private TbItemMapper tbItemMapper;

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

    @Override
    public List<TbItemParamItem> getTbItemParamsItemForPage(Integer page, Integer rows) {
        PageHelper.startPage(page, rows);
        TbItemParamItemExample tbItemParamItemExample = new TbItemParamItemExample();
        //web层如果不加pagehelper依赖就会报警告说Page类不存在
        return tbItemParamItemMapper.selectByExampleWithBLOBs(tbItemParamItemExample);
    }

    @Override
    public EasyUIDataGridResult<ItemParamItemBO> getItemParamItemBOList(Integer page, Integer rows) {
        List<TbItemParamItem> tbItemParamItems = getTbItemParamsItemForPage(page, rows);
//        List<Long> itemCatIds = tbItemParams.stream().map(tbItemParam -> tbItemParam.getItemCatId()).collect(Collectors.toList());
        List<ItemParamItemBO> itemParamItemBOs = tbItemParamItems.stream().map(tbItemParamItem -> {
            ItemParamItemBO itemParamItemBO = new ItemParamItemBO();
            BeanUtils.copyProperties(tbItemParamItem, itemParamItemBO);
            if (tbItemParamItem.getItemId() != null) {
                TbItem tbItem = tbItemMapper.selectByPrimaryKey(tbItemParamItem.getItemId());
                if (tbItem != null) {
                    itemParamItemBO.setItemTitle(tbItem.getTitle());
                }
            }
            return itemParamItemBO;
        }).collect(Collectors.toList());
        //组装分页对象
        Page<ItemParamItemBO> pageComponent = new Page<>();
        BeanUtils.copyProperties(tbItemParamItems, pageComponent);
        tbItemParamItems.clear();
        pageComponent.addAll(itemParamItemBOs);
        return EasyUIDataGridResult.build(new PageInfo<>(pageComponent).getTotal(), pageComponent);
    }

    @Override
    public int addItemParam(String paramData, Long itemId) {
        TbItemParamItem tbItemParamItem = new TbItemParamItem();
        tbItemParamItem.setCreated(new Date());
        tbItemParamItem.setUpdated(tbItemParamItem.getCreated());
        tbItemParamItem.setItemId(itemId);
        tbItemParamItem.setParamData(paramData);

        return tbItemParamItemMapper.insert(tbItemParamItem);
    }

    @Override
    public int updateItemParam(TbItemParamItem tbItemParamItem) {
        tbItemParamItem.setUpdated(new Date());
        return tbItemParamItemMapper.updateByPrimaryKey(tbItemParamItem);
    }

    @Override
    public int delItemParam(List<Long> ids) {
        int delNum = 0;
        for (Long id : ids) {
            delNum += tbItemParamItemMapper.deleteByPrimaryKey(id);
        }
        return delNum;
    }
}
