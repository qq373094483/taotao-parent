package com.taotao.controller;

import com.taotao.bo.ItemParamBO;
import com.taotao.common.pojo.EasyUIDataGridResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemParam;
import com.taotao.service.ItemParamService;
import com.taotao.service.ItemService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("item/param")
public class ItemParamController {
    @Autowired
    private ItemParamService itemParamService;
    @Autowired
    private ItemService itemService;
    @RequestMapping("/list")
    @ResponseBody
    public EasyUIDataGridResult<ItemParamBO> getItemList(Integer page, Integer rows){
        return itemParamService.getItemParamBOList(page, rows);
    }

    @RequestMapping(value = "save/{itemCatId}",method = RequestMethod.POST)
    @ResponseBody
    public TaotaoResult save(String paramData,@PathVariable Long itemCatId) {
        return itemParamService.addItemParam(paramData, itemCatId);
    }

    @RequestMapping(value = "update/{itemCatId}",method = RequestMethod.POST)
    @ResponseBody
    public TaotaoResult update(TbItemParam tbItemParam,@PathVariable Long itemCatId) {
        tbItemParam.setItemCatId(itemCatId);
        return itemParamService.updateItemParam(tbItemParam);
    }
    @RequestMapping(value = "delete",method = RequestMethod.POST)
    @ResponseBody
    public TaotaoResult delete(String ids) {
        List<Long> itemParamIds = Arrays.stream(StringUtils.split(ids, ",")).map(id -> Long.valueOf(id)).collect(Collectors.toList());
        return itemParamService.delItemParam(itemParamIds);
    }
    @RequestMapping(value = "query/itemcatid/{itemCatId}")
    @ResponseBody
    public TaotaoResult getByItemCatId(@PathVariable Long itemCatId) {
        return TaotaoResult.ok(itemParamService.selectByItemCatId(itemCatId));
    }

    @RequestMapping("query/{itemParamId}")
    @ResponseBody
    public TaotaoResult getItemById(@PathVariable Long itemParamId){
        return TaotaoResult.ok(itemParamService.selectByPrimaryKey(itemParamId));
    }

}
