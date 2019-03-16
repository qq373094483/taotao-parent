package com.taotao.mapper;

import com.taotao.pojo.TbUserConsumeMonthReport;
import com.taotao.pojo.TbUserConsumeMonthReportExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TbUserConsumeMonthReportMapper {
    int countByExample(TbUserConsumeMonthReportExample example);

    int deleteByExample(TbUserConsumeMonthReportExample example);

    int deleteByPrimaryKey(Long id);

    int insert(TbUserConsumeMonthReport record);

    int insertSelective(TbUserConsumeMonthReport record);

    List<TbUserConsumeMonthReport> selectByExample(TbUserConsumeMonthReportExample example);

    TbUserConsumeMonthReport selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") TbUserConsumeMonthReport record, @Param("example") TbUserConsumeMonthReportExample example);

    int updateByExample(@Param("record") TbUserConsumeMonthReport record, @Param("example") TbUserConsumeMonthReportExample example);

    int updateByPrimaryKeySelective(TbUserConsumeMonthReport record);

    int updateByPrimaryKey(TbUserConsumeMonthReport record);
}