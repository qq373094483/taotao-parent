package com.taotao.mapper;

import com.taotao.pojo.TbUserCashFlow;
import com.taotao.pojo.TbUserCashFlowExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TbUserCashFlowMapper {
    int countByExample(TbUserCashFlowExample example);

    int deleteByExample(TbUserCashFlowExample example);

    int deleteByPrimaryKey(Long id);

    int insert(TbUserCashFlow record);

    int insertSelective(TbUserCashFlow record);

    List<TbUserCashFlow> selectByExample(TbUserCashFlowExample example);

    TbUserCashFlow selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") TbUserCashFlow record, @Param("example") TbUserCashFlowExample example);

    int updateByExample(@Param("record") TbUserCashFlow record, @Param("example") TbUserCashFlowExample example);

    int updateByPrimaryKeySelective(TbUserCashFlow record);

    int updateByPrimaryKey(TbUserCashFlow record);
}