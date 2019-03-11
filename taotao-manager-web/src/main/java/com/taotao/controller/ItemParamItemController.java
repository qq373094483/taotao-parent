package com.taotao.controller;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.service.ItemParamItemService;
import com.taotao.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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

}
