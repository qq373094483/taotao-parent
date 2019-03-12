package com.taotao.controller;

import com.taotao.bo.ItemParamItemBO;
import com.taotao.common.pojo.EasyUIDataGridResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.pojo.TbItemParam;
import com.taotao.pojo.TbItemParamItem;
import com.taotao.service.ItemParamItemService;
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
@RequestMapping("item/param/item")
public class ItemParamItemController {
    @Autowired
    private ItemParamItemService itemParamItemService;
    @RequestMapping(value = "query/{itemId}")
    @ResponseBody
    public TaotaoResult queryItemParamItem(@PathVariable Long itemId) {
        return TaotaoResult.ok(itemParamItemService.selectByItemId(itemId));
    }

    @RequestMapping("/list")
    @ResponseBody
    public EasyUIDataGridResult<ItemParamItemBO> getItemList(Integer page, Integer rows){
        return itemParamItemService.getItemParamItemBOList(page, rows);
    }

    @RequestMapping(value = "save/{itemId}",method = RequestMethod.POST)
    @ResponseBody
    public TaotaoResult save(String paramData,@PathVariable Long itemId) {
        return TaotaoResult.ok(itemParamItemService.addItemParam(paramData, itemId));
    }

    @RequestMapping(value = "update/{itemId}",method = RequestMethod.POST)
    @ResponseBody
    public TaotaoResult update(TbItemParamItem tbItemParamItem, @PathVariable Long itemId) {
        tbItemParamItem.setItemId(itemId);
        return TaotaoResult.ok(itemParamItemService.updateItemParam(tbItemParamItem));
    }
    @RequestMapping(value = "delete",method = RequestMethod.POST)
    @ResponseBody
    public TaotaoResult delete(String ids) {
        List<Long> itemParamIds = Arrays.stream(StringUtils.split(ids, ",")).map(id -> Long.valueOf(id)).collect(Collectors.toList());
        return TaotaoResult.ok(itemParamItemService.delItemParam(itemParamIds));
    }
}
