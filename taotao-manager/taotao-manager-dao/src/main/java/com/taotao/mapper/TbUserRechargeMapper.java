package com.taotao.mapper;

import com.taotao.pojo.TbUserRecharge;
import com.taotao.pojo.TbUserRechargeExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TbUserRechargeMapper {
    int countByExample(TbUserRechargeExample example);

    int deleteByExample(TbUserRechargeExample example);

    int deleteByPrimaryKey(Long id);

    int insert(TbUserRecharge record);

    int insertSelective(TbUserRecharge record);

    List<TbUserRecharge> selectByExample(TbUserRechargeExample example);

    TbUserRecharge selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") TbUserRecharge record, @Param("example") TbUserRechargeExample example);

    int updateByExample(@Param("record") TbUserRecharge record, @Param("example") TbUserRechargeExample example);

    int updateByPrimaryKeySelective(TbUserRecharge record);

    int updateByPrimaryKey(TbUserRecharge record);
}