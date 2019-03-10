package com.taotao.service.impl;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taotao.common.pojo.EasyUIDataGridResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.utils.IDUtils;
import com.taotao.common.utils.JsonUtils;
import com.taotao.jedis.JedisClient;
import com.taotao.mapper.TbItemDescMapper;
import com.taotao.mapper.TbItemMapper;
import com.taotao.mapper.TbItemParamItemMapper;
import com.taotao.pojo.*;
import com.taotao.service.ItemService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 商品管理Service
 */
@Service
public class ItemServiceImpl implements ItemService {
    @Autowired
    private TbItemMapper tbItemMapper;
    @Autowired
    private TbItemDescMapper tbItemDescMapper;
    @Autowired
    private TbItemParamItemMapper tbItemParamItemMapper;
    @Autowired
    private JedisClient jedisClient;

    @Value("${ITEM_INFO}")
    private String ITEM_INFO;
    @Value("${ITEM_EXPIRE}")
    private Integer ITEM_EXPIRE;

    @Override
    public TbItem getItemById(Long itemId) {
        try {
            String json = jedisClient.get(ITEM_INFO + ":" + itemId + ":BASE");
            if (StringUtils.isNotBlank(json)) {
                return JsonUtils.jsonToPojo(json, TbItem.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        TbItem tbItem = tbItemMapper.selectByPrimaryKey(itemId);
        try {
            jedisClient.set(ITEM_INFO + ":" + itemId + ":BASE", JsonUtils.objectToJson(tbItem));
            jedisClient.expire(ITEM_INFO + ":" + itemId + ":BASE", ITEM_EXPIRE);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tbItem;
    }

    @Override
    public EasyUIDataGridResult<TbItem> getItemList(Integer page, Integer rows) {
        PageHelper.startPage(page, rows);
        TbItemExample tbItemExample = new TbItemExample();
        //web层如果不加pagehelper依赖就会报警告说Page类不存在
        List<TbItem> tbItems = tbItemMapper.selectByExample(tbItemExample);
        return EasyUIDataGridResult.build(new PageInfo<>(tbItems).getTotal(), tbItems);
    }

    @Override
    public TaotaoResult<Long> addItem(TbItem item, String desc) {
        //生成商品id
        final long itemId = IDUtils.genItemId();
        //补全item的属性
        item.setId(itemId);
        //商品状态，1-正常，2-下架，3-删除
        item.setStatus((byte) 1);
        item.setCreated(new Date());
        item.setUpdated(new Date());
        //向商品表插入数据
        tbItemMapper.insert(item);
        //创建一个商品描述表对应的pojo
        TbItemDesc itemDesc = new TbItemDesc();
        //补全pojo的属性
        itemDesc.setItemId(itemId);
        itemDesc.setItemDesc(desc);
        itemDesc.setUpdated(new Date());
        itemDesc.setCreated(new Date());
        //向商品描述表插入数据
        tbItemDescMapper.insert(itemDesc);
        //返回结果
        return TaotaoResult.ok(itemId);
    }

    @Override
    public TaotaoResult delItem(List<Long> ids) {
        int delNum = tbItemMapper.deleteByPrimaryKeys(ids);
        tbItemDescMapper.deleteByItemIds(ids);
        //返回结果
        return TaotaoResult.ok(delNum);
    }

    @Override
    public TbItemDesc getItemDescById(Long itemId) {
        try {
            String json = jedisClient.get(ITEM_INFO + ":" + itemId + ":DESC");
            if (StringUtils.isNotBlank(json)) {
                return JsonUtils.jsonToPojo(json, TbItemDesc.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        TbItemDesc tbItemDesc = tbItemDescMapper.selectByPrimaryKey(itemId);
        try {
            jedisClient.set(ITEM_INFO + ":" + itemId + ":DESC", JsonUtils.objectToJson(tbItemDesc));
            jedisClient.expire(ITEM_INFO + ":" + itemId + ":DESC", ITEM_EXPIRE);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tbItemDesc;
    }

    @Override
    public TaotaoResult updateItem(TbItem item, String desc) {
        //商品状态，1-正常，2-下架，3-删除
        item.setStatus((byte) 1);
        item.setUpdated(new Date());
        //向商品表更新数据
        tbItemMapper.updateByPrimaryKey(item);
        //创建一个商品描述表对应的pojo
        TbItemDesc itemDesc = new TbItemDesc();
        //补全pojo的属性
        itemDesc.setItemId(item.getId());
        itemDesc.setItemDesc(desc);
        itemDesc.setUpdated(new Date());
        //向商品描述表更新数据
        tbItemDescMapper.updateByPrimaryKeyWithBLOBs(itemDesc);
        jedisClient.expire(ITEM_INFO + ":" + item.getId() + ":DESC",-1);
        //返回结果
        return TaotaoResult.ok();
    }

    @Override
    public TbItemParamItem getItemParamItemByItemId(Long itemId) {
        TbItemParamItemExample tbItemParamItemExample = new TbItemParamItemExample();
        TbItemParamItemExample.Criteria criteria = tbItemParamItemExample.createCriteria();
        criteria.andItemIdEqualTo(itemId);
        List<TbItemParamItem> tbItemParamItems = tbItemParamItemMapper.selectByExample(tbItemParamItemExample);
        if (CollectionUtils.isNotEmpty(tbItemParamItems)) {
            return tbItemParamItems.get(0);
        }
        return null;
    }
}
