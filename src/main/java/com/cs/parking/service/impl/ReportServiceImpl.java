package com.cs.parking.service.impl;

import com.cs.parking.code.BaseCode;
import com.cs.parking.code.ReportExceptionCode;
import com.cs.parking.dto.ReportDTO;
import com.cs.parking.exception.ErrorException;
import com.cs.parking.exception.SystemException;
import com.cs.parking.mapper.ReportMapper;
import com.cs.parking.pojo.Report;
import com.cs.parking.pojo.ReportExample;
import com.cs.parking.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @ClassName ReportServiceImpl
 * @Description TODO
 * @Author QQ163
 * @Date 2021/1/19 22:07
 **/
@Service
public class ReportServiceImpl implements ReportService {
    @Autowired
    ReportMapper reportMapper;
    @Autowired
    ReportExample reportExample;

    @Override
    public void insert(Report report) {
        int i = reportMapper.insertSelective(report);
        if (i == -1){
            throw new ErrorException(BaseCode.System_Error,"新增违规举报记录失败");
        }
    }

    @Override
    public Map<String, Map<String, Map<String, Map<String, List<Report>>>>> selectAll(boolean deal, int reportType) {
        reportExample.clear();
        ReportExample.Criteria criteria = reportExample.createCriteria().andCancelEqualTo(false);
        if (!deal){
            criteria.andDealEqualTo(false);
        }
        if (reportType != -1){
            criteria.andReportTypeEqualTo(reportType);
        }
        List<Report> reports = reportMapper.selectByExample(reportExample);
        if (reports == null || reports.isEmpty()){
            throw new SystemException(BaseCode.Null);
        }
        Map<String, Map<String, Map<String, Map<String, List<Report>>>>> collect = reports.stream()
                .collect(Collectors.groupingBy
                        (Report::getProvince, Collectors.groupingBy
                                (Report::getCity, Collectors.groupingBy
                                        (Report::getCounty, Collectors.groupingBy
                                                (Report::getTown)))));

        return collect;
    }

    @Override
    public List<ReportDTO> selectAll(String province, String city, String county, String town, boolean deal, int reportType) {
        reportExample.clear();
        ReportExample.Criteria criteria = reportExample.createCriteria()
                .andCancelEqualTo(false)
                .andProvinceEqualTo(province)
                .andCityEqualTo(city)
                .andCountyEqualTo(county)
                .andTownEqualTo(town);
        if (!deal){
            criteria.andDealEqualTo(false);
        }
        if (reportType != -1){
            criteria.andReportTypeEqualTo(reportType);
        }
        List<Report> reports = reportMapper.selectByExample(reportExample);
        if (reports == null || reports.isEmpty()){
            throw new SystemException(BaseCode.Null);
        }
        List<ReportDTO> reportDTOList = new ArrayList<>();
        reports.stream()
                .sorted((a,b)->{
                    if (a.getLongitude().equals(b.getLongitude())){
                        return a.getLatitude().compareTo(b.getLatitude());
                    }else{
                        return a.getLongitude().compareTo(b.getLongitude());
                    }
                }).forEach((x)->{
                    reportDTOList.add(new ReportDTO(x));
        });
        return reportDTOList;
    }

    @Override
    public List<ReportDTO> selectByLongLat(double TLat,double BLat,double LLong,double RLong,boolean deal,int reportType) {
        reportExample.clear();
        ReportExample.Criteria criteria = reportExample.createCriteria()
                .andLongitudeGreaterThanOrEqualTo(LLong)
                .andLongitudeLessThanOrEqualTo(RLong)
                .andLatitudeGreaterThanOrEqualTo(BLat)
                .andLatitudeLessThanOrEqualTo(TLat);
        if (!deal){
            criteria.andDealEqualTo(false);
        }
        if (reportType != -1){
            criteria.andReportTypeEqualTo(reportType);
        }
        List<Report> reports = reportMapper.selectByExample(reportExample);
        if (reports == null || reports.isEmpty()){
            throw new SystemException(BaseCode.Null);
        }
        List<ReportDTO> reportDTOList = new ArrayList<>();
        reports.stream()
                .sorted((a,b)->{
                    if (a.getLongitude().equals(b.getLongitude())){
                        return a.getLatitude().compareTo(b.getLatitude());
                    }else{
                        return a.getLongitude().compareTo(b.getLongitude());
                    }
                }).forEach((x)->{
            reportDTOList.add(new ReportDTO(x));
        });
        return reportDTOList;
    }

    @Override
    public void updateDeal(Integer rid) {
        Report report = reportMapper.selectByPrimaryKey(rid);
        if (report == null){
            throw new SystemException(BaseCode.Null);
        }
        if (report.getCancel()){
            throw new SystemException(BaseCode.Null);
        }
        report.setDeal(true);
        int i = reportMapper.updateByPrimaryKeySelective(report);
        if (i == -1){
            throw new ErrorException(BaseCode.System_Error,"修改违规记录deal出错！");
        }
    }

    @Override
    public void deleteCancel(Integer rid) {
        Report report = reportMapper.selectByPrimaryKey(rid);
        if (report == null){
            throw new SystemException(BaseCode.Null);
        }
        if (report.getDeal()){
            report.setCancel(false);
        }
        if (new Date().getTime() - report.getReportTime().getTime() > 1000*60*60*24){
            throw new SystemException(ReportExceptionCode.UnCancelOverdue);
        }
        report.setCancel(true);
        int i = reportMapper.updateByPrimaryKeySelective(report);
        if (i == -1){
            throw new ErrorException(BaseCode.System_Error,"取消违规举报出错！");
        }
    }
}
