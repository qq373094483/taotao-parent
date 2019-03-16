package com.taotao.mapper;

import com.taotao.pojo.TbUserConsumeDayReport;
import com.taotao.pojo.TbUserConsumeDayReportExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TbUserConsumeDayReportMapper {
    int countByExample(TbUserConsumeDayReportExample example);

    int deleteByExample(TbUserConsumeDayReportExample example);

    int deleteByPrimaryKey(Long id);

    int insert(TbUserConsumeDayReport record);

    int insertSelective(TbUserConsumeDayReport record);

    List<TbUserConsumeDayReport> selectByExample(TbUserConsumeDayReportExample example);

    TbUserConsumeDayReport selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") TbUserConsumeDayReport record, @Param("example") TbUserConsumeDayReportExample example);

    int updateByExample(@Param("record") TbUserConsumeDayReport record, @Param("example") TbUserConsumeDayReportExample example);

    int updateByPrimaryKeySelective(TbUserConsumeDayReport record);

    int updateByPrimaryKey(TbUserConsumeDayReport record);
}