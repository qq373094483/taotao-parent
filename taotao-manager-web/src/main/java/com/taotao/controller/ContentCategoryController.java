package com.taotao.controller;

import com.taotao.common.pojo.EasyUITreeNode;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.content.service.ContentCategoryService;
import com.taotao.pojo.TbContentCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("content/category")
public class ContentCategoryController {
    @Autowired
    private ContentCategoryService contentCategoryService;
    @RequestMapping("list")
    @ResponseBody
    public List<EasyUITreeNode> getContentCategoryList(@RequestParam(value="id",defaultValue = "0") Long parentId) {
        return contentCategoryService.getContentCategoryList(parentId);
    }

    @RequestMapping(value="create",method = RequestMethod.POST)
    @ResponseBody
    public TaotaoResult addContentCategory(Long parentId,String name) {
        return contentCategoryService.addContentCategory(parentId, name);
    }
    @RequestMapping(value="update",method = RequestMethod.POST)
    @ResponseBody
    public TaotaoResult updateContentCategory(TbContentCategory tbContentCategory) {
        return contentCategoryService.updateContentCategorySelective(tbContentCategory);
    }
    @RequestMapping(value="delete",method = RequestMethod.POST)
    @ResponseBody
    public TaotaoResult delContentCategory(Long id) {
        return contentCategoryService.delContentCategory(id);
    }
}
