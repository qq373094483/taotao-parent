package com.taotao.service.impl;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taotao.bo.ItemParamBO;
import com.taotao.common.pojo.EasyUIDataGridResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.mapper.TbItemCatMapper;
import com.taotao.mapper.TbItemParamMapper;
import com.taotao.pojo.TbItemCat;
import com.taotao.pojo.TbItemParam;
import com.taotao.pojo.TbItemParamExample;
import com.taotao.service.ItemParamService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ItemParamServiceImpl implements ItemParamService {
    @Autowired
    private TbItemParamMapper tbItemParamMapper;
    @Autowired
    private TbItemCatMapper tbItemCatMapper;
    @Override
    public TbItemParam selectByPrimaryKey(Long id) {
        return tbItemParamMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<TbItemParam> selectByExampleWithBLOBs(TbItemParamExample example) {
        return tbItemParamMapper.selectByExampleWithBLOBs(example);
    }

    @Override
    public TbItemParam selectByItemCatId(Long itemCatId) {
        TbItemParamExample tbItemParamExample = new TbItemParamExample();
        TbItemParamExample.Criteria criteria = tbItemParamExample.createCriteria();
        criteria.andItemCatIdEqualTo(itemCatId);
        List<TbItemParam> tbItemParams = selectByExampleWithBLOBs(tbItemParamExample);
        if (CollectionUtils.isNotEmpty(tbItemParams)) {
            return tbItemParams.get(0);
        }
        return null;
    }

    @Override
    public EasyUIDataGridResult<TbItemParam> getItemParamList(Integer page, Integer rows) {
        List<TbItemParam> tbItemParams = getTbItemParamsForPage(page, rows);
        return EasyUIDataGridResult.build(new PageInfo<>(tbItemParams).getTotal(), tbItemParams);
    }

    @Override
    public List<TbItemParam> getTbItemParamsForPage(Integer page, Integer rows) {
        PageHelper.startPage(page, rows);
        TbItemParamExample tbItemParamExample = new TbItemParamExample();
        //web层如果不加pagehelper依赖就会报警告说Page类不存在
        return tbItemParamMapper.selectByExampleWithBLOBs(tbItemParamExample);
    }

    @Override
    public EasyUIDataGridResult<ItemParamBO> getItemParamBOList(Integer page, Integer rows) {
        List<TbItemParam> tbItemParams = getTbItemParamsForPage(page, rows);
//        List<Long> itemCatIds = tbItemParams.stream().map(tbItemParam -> tbItemParam.getItemCatId()).collect(Collectors.toList());
        List<ItemParamBO> itemParamBOs=tbItemParams.stream().map(tbItemParam -> {
            ItemParamBO itemParamBO = new ItemParamBO();
            BeanUtils.copyProperties(tbItemParam, itemParamBO);
            if (tbItemParam.getItemCatId() != null) {
                TbItemCat tbItemCat = tbItemCatMapper.selectByPrimaryKey(tbItemParam.getItemCatId());
                if (tbItemCat != null) {
                    itemParamBO.setItemCatName(tbItemCat.getName());
                }
            }
            return itemParamBO;
        }).collect(Collectors.toList());
        //组装分页对象
        Page<ItemParamBO> pageComponent = new Page<>();
        BeanUtils.copyProperties(tbItemParams, pageComponent);
        tbItemParams.clear();
        pageComponent.addAll(itemParamBOs);
        return EasyUIDataGridResult.build(new PageInfo<>(pageComponent).getTotal(), pageComponent);
    }

    @Override
    public TaotaoResult addItemParam(String paramData, Long itemCatId) {
        TbItemParam tbItemParam = new TbItemParam();
        tbItemParam.setItemCatId(itemCatId);
        tbItemParam.setParamData(paramData);
        tbItemParam.setCreated(new Date());
        tbItemParam.setUpdated(tbItemParam.getCreated());
        tbItemParamMapper.insert(tbItemParam);
        return TaotaoResult.ok();
    }
    @Override
    public TaotaoResult updateItemParam(TbItemParam tbItemParam) {
        tbItemParam.setUpdated(new Date());
        tbItemParamMapper.updateByPrimaryKeyWithBLOBs(tbItemParam);
        return TaotaoResult.ok();
    }

    @Override
    public TaotaoResult delItemParam(List<Long> ids) {
        int delNum = 0;
        for (Long id : ids) {
            delNum+=tbItemParamMapper.deleteByPrimaryKey(id);
        }
        return TaotaoResult.ok(delNum);
    }
}
