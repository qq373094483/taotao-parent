package com.taotao.content.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taotao.common.pojo.EasyUIDataGridResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.utils.JsonUtils;
import com.taotao.content.service.ContentService;
import com.taotao.jedis.JedisClient;
import com.taotao.mapper.TbContentMapper;
import com.taotao.pojo.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ContentServiceImpl implements ContentService {
    @Autowired
    private TbContentMapper tbContentMapper;
    @Autowired
    private JedisClient jedisClient;

    @Value("${INDEX_CONTENT}")
    private String INDEX_CONTENT;
    @Override
    public TaotaoResult addContent(TbContent tbContent) {
        //补全pojo的属性
        tbContent.setCreated( new Date());
        tbContent.setUpdated(new Date());
        //插入到内容表
        tbContentMapper.insert(tbContent);
        //同步缓存
        //删除对应的缓存信息
        jedisClient.hdel(INDEX_CONTENT, tbContent.getCategoryId().toString());
        return TaotaoResult.ok();
    }

    @Override
    public TaotaoResult updateContent(TbContent tbContent) {
        tbContent.setUpdated(new Date());
        //更新到内容表
        tbContentMapper.updateByPrimaryKeyWithBLOBs(tbContent);
        //同步缓存
        //删除对应的缓存信息
        jedisClient.hdel(INDEX_CONTENT, tbContent.getCategoryId().toString());
        return TaotaoResult.ok();
    }

    @Override
    public List<TbContent> getContentByCid(Long cid) {
        //先查询缓存
        //添加缓存不能影响正常业务逻辑
        try{
            //查询缓存
            String json = jedisClient.hget(INDEX_CONTENT, cid + "");
            //查询到结果，把json转换成List返回
            if (StringUtils.isNotBlank(json)) {
                return JsonUtils.jsonToList(json, TbContent.class);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        //缓存中没有命中，需要查询数据库
        TbContentExample tbContentExample = new TbContentExample();
        TbContentExample.Criteria criteria = tbContentExample.createCriteria();
        //设置查询条件
        criteria.andCategoryIdEqualTo(cid);
        //执行查询
        List<TbContent> tbContents = tbContentMapper.selectByExample(tbContentExample);
        //把结果添加到缓存
        try{
            jedisClient.hset(INDEX_CONTENT, cid + "", JsonUtils.objectToJson(tbContents));
        }catch (Exception e){
            e.printStackTrace();
        }
        return tbContents;
    }

    @Override
    public EasyUIDataGridResult<TbContent> getItemList(Long categoryId, Integer page, Integer rows) {
        PageHelper.startPage(page, rows);
        TbContentExample tbContentExample = new TbContentExample();
        TbContentExample.Criteria criteria = tbContentExample.createCriteria();
        criteria.andCategoryIdEqualTo(categoryId);
        //web层如果不加pagehelper依赖就会报警告说Page类不存在
        List<TbContent> tbItems = tbContentMapper.selectByExample(tbContentExample);
        return EasyUIDataGridResult.build(new PageInfo<>(tbItems).getTotal(), tbItems);
    }

    @Override
    public TaotaoResult delContent(List<Long> ids) {
        int delNum=0;
        for (Long id:ids) {
            delNum+=tbContentMapper.deleteByPrimaryKey(id);
        }
        return TaotaoResult.ok(delNum);
    }
}
