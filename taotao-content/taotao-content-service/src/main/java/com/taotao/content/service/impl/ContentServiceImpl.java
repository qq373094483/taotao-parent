package com.taotao.content.service.impl;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.content.service.ContentService;
import com.taotao.mapper.TbContentMapper;
import com.taotao.pojo.TbContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class ContentServiceImpl implements ContentService {
    @Autowired
    private TbContentMapper tbContentMapper;
    @Override
    public TaotaoResult addContent(TbContent tbContent) {
        //补全pojo的属性
        tbContent.setCreated( new Date());
        tbContent.setUpdated(new Date());
        //插入到内容表
        tbContentMapper.insert(tbContent);
        return TaotaoResult.ok();
    }
}
