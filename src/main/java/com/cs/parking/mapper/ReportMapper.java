package com.cs.parking.mapper;

import com.cs.parking.pojo.Report;
import com.cs.parking.pojo.ReportExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

@Component
public interface ReportMapper {
    long countByExample(ReportExample example);

    int deleteByExample(ReportExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(Report record);

    int insertSelective(Report record);

    List<Report> selectByExample(ReportExample example);

    Report selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") Report record, @Param("example") ReportExample example);

    int updateByExample(@Param("record") Report record, @Param("example") ReportExample example);

    int updateByPrimaryKeySelective(Report record);

    int updateByPrimaryKey(Report record);
}