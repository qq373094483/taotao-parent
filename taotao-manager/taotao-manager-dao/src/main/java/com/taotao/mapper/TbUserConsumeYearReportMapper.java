package com.taotao.mapper;

import com.taotao.pojo.TbUserConsumeYearReport;
import com.taotao.pojo.TbUserConsumeYearReportExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TbUserConsumeYearReportMapper {
    int countByExample(TbUserConsumeYearReportExample example);

    int deleteByExample(TbUserConsumeYearReportExample example);

    int deleteByPrimaryKey(Long id);

    int insert(TbUserConsumeYearReport record);

    int insertSelective(TbUserConsumeYearReport record);

    List<TbUserConsumeYearReport> selectByExample(TbUserConsumeYearReportExample example);

    TbUserConsumeYearReport selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") TbUserConsumeYearReport record, @Param("example") TbUserConsumeYearReportExample example);

    int updateByExample(@Param("record") TbUserConsumeYearReport record, @Param("example") TbUserConsumeYearReportExample example);

    int updateByPrimaryKeySelective(TbUserConsumeYearReport record);

    int updateByPrimaryKey(TbUserConsumeYearReport record);
}