package com.cs.parking.service;

import com.cs.parking.dto.ReportDTO;
import com.cs.parking.pojo.Report;

import java.util.List;
import java.util.Map;

public interface ReportService {
    /*
    * 添加违规举报记录
    * */
    void insert(Report report);
    /*
    * 查询违规举报记录
    * */
    Map<String, Map<String, Map<String, Map<String, List<Report>>>>> selectAll(boolean deal, int reportType);
    /*
     * 查询违规举报记录
     * */
    List<ReportDTO> selectAll(String province,String city,String county,String town,boolean deal, int reportType);
    /*
    * 查询违规举报记录（经纬度）
    * */
    List<ReportDTO> selectByLongLat(double TLat,double BLat,double LLong,double RLong,boolean deal,int reportType);
    /*
    * 处理违规
    * */
    void updateDeal(Integer rid);
    /*
    * 撤销上报
    * */
    void deleteCancel(Integer rid);
}
