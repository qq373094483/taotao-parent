package com.taotao.mapper;

import com.taotao.pojo.TbUserConsumeHourReport;
import com.taotao.pojo.TbUserConsumeHourReportExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TbUserConsumeHourReportMapper {
    int countByExample(TbUserConsumeHourReportExample example);

    int deleteByExample(TbUserConsumeHourReportExample example);

    int deleteByPrimaryKey(Long id);

    int insert(TbUserConsumeHourReport record);

    int insertSelective(TbUserConsumeHourReport record);

    List<TbUserConsumeHourReport> selectByExample(TbUserConsumeHourReportExample example);

    TbUserConsumeHourReport selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") TbUserConsumeHourReport record, @Param("example") TbUserConsumeHourReportExample example);

    int updateByExample(@Param("record") TbUserConsumeHourReport record, @Param("example") TbUserConsumeHourReportExample example);

    int updateByPrimaryKeySelective(TbUserConsumeHourReport record);

    int updateByPrimaryKey(TbUserConsumeHourReport record);
}