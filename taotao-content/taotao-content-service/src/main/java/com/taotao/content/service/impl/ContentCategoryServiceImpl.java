package com.taotao.content.service.impl;

import com.taotao.common.pojo.EasyUITreeNode;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.content.service.ContentCategoryService;
import com.taotao.mapper.TbContentCategoryMapper;
import com.taotao.pojo.TbContentCategory;
import com.taotao.pojo.TbContentCategoryExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ContentCategoryServiceImpl implements ContentCategoryService {
    @Autowired
    private TbContentCategoryMapper tbContentCategoryMapper;
    @Override
    public List<EasyUITreeNode> getContentCategoryList(Long parentId) {
        TbContentCategoryExample example = new TbContentCategoryExample();
        TbContentCategoryExample.Criteria criteria = example.createCriteria();
        criteria.andParentIdEqualTo(parentId);
        List<TbContentCategory> tbContentCategories = tbContentCategoryMapper.selectByExample(example);
        return tbContentCategories.stream().map(tbContentCategorie->{
            EasyUITreeNode easyUITreeNode = new EasyUITreeNode();
            easyUITreeNode.setId(tbContentCategorie.getId());
            easyUITreeNode.setText(tbContentCategorie.getName());
            easyUITreeNode.setState(tbContentCategorie.getIsParent()?"closed":"open");
            return easyUITreeNode;
        }).collect(Collectors.toList());
    }

    @Override
    public TaotaoResult addContentCategory(Long parentId, String name) {
        //创建一个pojo对象
        TbContentCategory contentCategory = new TbContentCategory();
        //补全对象属性
        contentCategory.setParentId(parentId);
        contentCategory.setName(name);
        //状态。可靠值：1（正常），2（删除）
        contentCategory.setStatus(1);
        //排序，默认为1
        contentCategory.setSortOrder(1);
        contentCategory.setIsParent(false);
        contentCategory.setCreated(new Date());
        contentCategory.setUpdated(new Date());
        //插入到数据库
        tbContentCategoryMapper.insert(contentCategory);
        TbContentCategory parent = tbContentCategoryMapper.selectByPrimaryKey(parentId);
        if(!parent.getIsParent()){
            parent.setIsParent(true);
            tbContentCategoryMapper.updateByPrimaryKey(parent);
        }
        return TaotaoResult.ok(contentCategory);
    }

    @Override
    public TaotaoResult updateContentCategorySelective(TbContentCategory tbContentCategory) {
        return TaotaoResult.ok(tbContentCategoryMapper.updateByPrimaryKeySelective(tbContentCategory));
    }

    @Override
    public TaotaoResult delContentCategory(Long id) {
        TbContentCategory contentCategory = tbContentCategoryMapper.selectByPrimaryKey(id);
        if (contentCategory != null) {
            tbContentCategoryMapper.deleteByPrimaryKey(id);
            //如果节点被删除后，其父节点下无子节点，则为叶子节点，把isParent设置为false
            TbContentCategoryExample tbContentCategoryExample = new TbContentCategoryExample();
            TbContentCategoryExample.Criteria criteria = tbContentCategoryExample.createCriteria();
            criteria.andParentIdEqualTo(contentCategory.getParentId());
            List<TbContentCategory> tbContentCategories = tbContentCategoryMapper.selectByExample(tbContentCategoryExample);
            if (CollectionUtils.isEmpty(tbContentCategories)) {
                TbContentCategory contentCategoryParent = tbContentCategoryMapper.selectByPrimaryKey(contentCategory.getParentId());
                contentCategoryParent.setIsParent(false);
                tbContentCategoryMapper.updateByPrimaryKeySelective(contentCategoryParent);
            }
            return TaotaoResult.ok();
        }
        return TaotaoResult.build(500,"未找到该数据");
    }
}
