package com.taotao.controller;

import com.taotao.common.pojo.EasyUIDataGridResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.content.service.ContentService;
import com.taotao.pojo.TbContent;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("content")
public class ContentController {

    @Autowired
    private ContentService contentService;

    @RequestMapping(value="save",method = RequestMethod.POST)
    @ResponseBody
    public TaotaoResult addContent(TbContent tbContent) {
        return contentService.addContent(tbContent);
    }

    @RequestMapping("query/list")
    @ResponseBody
    public EasyUIDataGridResult<TbContent> getContentList(Long categoryId,Integer page, Integer rows){
        return contentService.getItemList(categoryId,page,rows);
    }

    @RequestMapping(value="edit",method = RequestMethod.POST)
    @ResponseBody
    public TaotaoResult updateContent(TbContent tbContent) {
        return contentService.updateContent(tbContent);
    }

    @RequestMapping(value="delete",method = RequestMethod.POST)
    @ResponseBody
    public TaotaoResult delContent(String ids) {
        if (StringUtils.isBlank(ids)) {
            TaotaoResult.build(500, "请选择要删除的对象");
        }
        List<Long> contentIds = Arrays.stream(StringUtils.split(ids, ",")).map(id -> Long.valueOf(id)).collect(Collectors.toList());
        return contentService.delContent(contentIds);
    }
}
