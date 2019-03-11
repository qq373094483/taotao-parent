package com.taotao.controller;

import com.taotao.common.pojo.EasyUITreeNode;
import com.taotao.service.ItemCatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("item/cat")
public class ItemCatController {

    @Autowired
    private ItemCatService itemCatService;
    @RequestMapping("list")
    @ResponseBody
    public List<EasyUITreeNode> getItemCatList(@RequestParam(name="id",defaultValue = "0") Long parentId) {
        return itemCatService.getItemCatList(parentId);
    }
}
