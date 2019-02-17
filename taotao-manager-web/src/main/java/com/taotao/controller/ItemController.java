package com.taotao.controller;

import com.taotao.common.pojo.EasyUIDataGridResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.pojo.TbItem;
import com.taotao.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ItemController {

    @Autowired
    private ItemService itemService;
    /**
     * 如果/item/{itemId}最后一个URI和参数名一致，可以不加@PathVariable，否则要在@PathVariable设置与路径一致的名称到注解中
     * 如果报406错误，有可能是因为没有加jackson包加进入，这里通过common包传递了该包的引用
     * @param itemId
     * @return
     */
    @RequestMapping("/item/{itemId}")
    @ResponseBody
    public TbItem getItemById(@PathVariable Long itemId){
        return itemService.getItemById(itemId);
    }

    @RequestMapping("/item/list")
    @ResponseBody
    public EasyUIDataGridResult<TbItem> getItemList(Integer page,Integer rows){
        return itemService.getItemList(page, rows);
    }

    @RequestMapping(value = "/item/save", method = RequestMethod.POST)
    @ResponseBody
    public TaotaoResult adItem(TbItem tbItem, String desc) {
        return itemService.addItem(tbItem, desc);
    }
}
