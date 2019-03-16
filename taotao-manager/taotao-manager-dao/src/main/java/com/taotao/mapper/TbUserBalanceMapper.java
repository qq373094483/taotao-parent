package com.taotao.mapper;

import com.taotao.pojo.TbUserBalance;
import com.taotao.pojo.TbUserBalanceExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TbUserBalanceMapper {
    int countByExample(TbUserBalanceExample example);

    int deleteByExample(TbUserBalanceExample example);

    int deleteByPrimaryKey(Long id);

    int insert(TbUserBalance record);

    int insertSelective(TbUserBalance record);

    List<TbUserBalance> selectByExample(TbUserBalanceExample example);

    TbUserBalance selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") TbUserBalance record, @Param("example") TbUserBalanceExample example);

    int updateByExample(@Param("record") TbUserBalance record, @Param("example") TbUserBalanceExample example);

    int updateByPrimaryKeySelective(TbUserBalance record);

    int updateByPrimaryKey(TbUserBalance record);
}