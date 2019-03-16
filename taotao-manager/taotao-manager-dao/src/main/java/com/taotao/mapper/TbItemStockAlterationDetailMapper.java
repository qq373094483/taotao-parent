package com.taotao.mapper;

import com.taotao.pojo.TbItemStockAlterationDetail;
import com.taotao.pojo.TbItemStockAlterationDetailExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TbItemStockAlterationDetailMapper {
    int countByExample(TbItemStockAlterationDetailExample example);

    int deleteByExample(TbItemStockAlterationDetailExample example);

    int deleteByPrimaryKey(Long id);

    int insert(TbItemStockAlterationDetail record);

    int insertSelective(TbItemStockAlterationDetail record);

    List<TbItemStockAlterationDetail> selectByExample(TbItemStockAlterationDetailExample example);

    TbItemStockAlterationDetail selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") TbItemStockAlterationDetail record, @Param("example") TbItemStockAlterationDetailExample example);

    int updateByExample(@Param("record") TbItemStockAlterationDetail record, @Param("example") TbItemStockAlterationDetailExample example);

    int updateByPrimaryKeySelective(TbItemStockAlterationDetail record);

    int updateByPrimaryKey(TbItemStockAlterationDetail record);
}