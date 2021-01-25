package com.cs.parking.controller;

import com.cs.parking.base.DTO.ResultDTO;
import com.cs.parking.base.baseinterface.ParameterVerify;
import com.cs.parking.base.utils.ResultUtils;
import com.cs.parking.code.ParameterCode;
import com.cs.parking.dto.ReportDTO;
import com.cs.parking.exception.ErrorException;
import com.cs.parking.exception.SystemException;
import com.cs.parking.pojo.Report;
import com.cs.parking.service.ReportService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @ClassName ReportController
 * @Description TODO
 * @Author QQ163
 * @Date 2021/1/19 22:49
 **/
@Controller
@RequestMapping("/report")
@Api(tags = {"违规举报操作接口"})
public class ReportController {

    @Autowired
    ReportService reportService;

    @GetMapping("/searchAllUnUsed")
    @Transactional
    @ResponseBody
    @ApiOperation(value = "获取所有违规举报记录",notes = "不建议使用")
    public ResultDTO<Map<String, Map<String, Map<String, Map<String, List<Report>>>>>> searchAllUnUsed(
            @ApiParam(value = "违规类型",required = false)@RequestParam(value = "reportType",required = false,defaultValue = "-1")String reportType,
            @ApiParam(value = "是否处理完毕",required = false)@RequestParam(value = "deal",required = false)boolean deal
    ){
        try {
            Map<String, Map<String, Map<String, Map<String, List<Report>>>>> stringMapMap = reportService.selectAll(deal, Integer.parseInt(reportType));
            return ResultUtils.success(stringMapMap);
        }catch (SystemException e) {
            return ResultUtils.error(e.getCode(), e.getMessage(),null);
        }catch (ErrorException e){
            return ResultUtils.error(e.getCode(),e.getMessage()+'\n'+e.getErrorMessage(),null);
        }
    }

    @GetMapping("/searchAll")
    @Transactional
    @ResponseBody
    @ApiOperation(value = "获取所有违规举报记录")
    public ResultDTO<PageInfo<ReportDTO>> searchAll(
            @ApiParam(value = "起始页>=0",required = true)@RequestParam(value = "offset",required = true)Integer offset,
            @ApiParam(value = "每页数据数",required = true)@RequestParam(value = "limit",required = true)Integer limit,
            @ApiParam(value = "违规类型",required = false)@RequestParam(value = "reportType",required = false,defaultValue = "-1")String reportType,
            @ApiParam(value = "是否处理完毕",required = false)@RequestParam(value = "deal",required = false)boolean deal,
            @ApiParam(value = "省",required = true)@RequestParam(value = "province",required = true)String province,
            @ApiParam(value = "市",required = true)@RequestParam(value = "city",required = true)String city,
            @ApiParam(value = "县",required = true)@RequestParam(value = "county",required = true)String county,
            @ApiParam(value = "镇",required = true)@RequestParam(value = "town",required = true)String town
    ){
        try {
            PageHelper.startPage(offset,limit);
            List<ReportDTO> reportDTOList = reportService.selectAll(province, city, county, town, deal, Integer.parseInt(reportType));
            PageInfo<ReportDTO> pageInfo = new PageInfo<>(reportDTOList);
            return ResultUtils.success(pageInfo);
        }catch (SystemException e) {
            return ResultUtils.error(e.getCode(), e.getMessage(),null);
        }catch (ErrorException e){
            return ResultUtils.error(e.getCode(),e.getMessage()+'\n'+e.getErrorMessage(),null);
        }
    }

    @GetMapping("/searchLongLat")
    @Transactional
    @ResponseBody
    @ApiOperation(value = "经纬度获取所有违规举报记录")
    public ResultDTO<PageInfo<ReportDTO>> searchLongLat(
            @ApiParam(value = "起始页>=0",required = true)@RequestParam(value = "offset",required = true)Integer offset,
            @ApiParam(value = "每页数据数",required = true)@RequestParam(value = "limit",required = true)Integer limit,
            @ApiParam(value = "违规类型",required = false)@RequestParam(value = "reportType",required = false,defaultValue = "-1")String reportType,
            @ApiParam(value = "是否处理完毕",required = false)@RequestParam(value = "deal",required = false)boolean deal,
            @ApiParam(value = "最小经度",required = true)@RequestParam(value = "LLong",required = true)double LLong,
            @ApiParam(value = "最大经度",required = true)@RequestParam(value = "RLong",required = true)double RLong,
            @ApiParam(value = "最小纬度",required = true)@RequestParam(value = "BLat",required = true)double BLat,
            @ApiParam(value = "最大纬度",required = true)@RequestParam(value = "TLat",required = true)double TLat
    ){
        try {
            PageHelper.startPage(offset,limit);
            List<ReportDTO> reportDTOList = reportService.selectByLongLat(TLat, BLat, LLong, RLong, deal, Integer.parseInt(reportType));
            PageInfo<ReportDTO> pageInfo = new PageInfo<>(reportDTOList);
            return ResultUtils.success(pageInfo);
        }catch (SystemException e) {
            return ResultUtils.error(e.getCode(), e.getMessage(),null);
        }catch (ErrorException e){
            return ResultUtils.error(e.getCode(),e.getMessage()+'\n'+e.getErrorMessage(),null);
        }
    }

    @PostMapping("/appendRepord")
    @Transactional
    @ResponseBody
    @ApiOperation(value = "上报违规记录")
    public ResultDTO appendReport(
            @ApiParam(value = "车辆号码",required = true)@RequestParam(value = "carNumber",required = true)String carNumber,
            @ApiParam(value = "违规类型",required = true)@RequestParam(value = "reportType",required = true)int reportType,
            @ApiParam(value = "市",required = true)@RequestParam(value = "province",required = true)String province,
            @ApiParam(value = "省",required = true)@RequestParam(value = "city",required = true)String city,
            @ApiParam(value = "县",required = true)@RequestParam(value = "county",required = true)String county,
            @ApiParam(value = "镇",required = true)@RequestParam(value = "town",required = true)String town,
            @ApiParam(value = "详细地址",required = true)@RequestParam(value = "address",required = true)String address,
            @ApiParam(value = "经度",required = true)@RequestParam(value = "longitude",required = true)double longitude,
            @ApiParam(value = "纬度",required = true)@RequestParam(value = "longitude",required = true)double latitude,
            @ApiParam(value = "违规照片",required = true)@RequestParam(value = "reportPhoto",required = true)String[] reportPhotoArr
    ){
        try {
            String reportPhoto = Arrays.stream(reportPhotoArr).collect(Collectors.joining(";"));
            Report report = new Report(carNumber,reportType,province,city,county,town,address,new Date(),longitude,latitude,reportPhoto,false,false);
            reportService.insert(report);
            return ResultUtils.success();
       }catch (SystemException e) {
            return ResultUtils.error(e.getCode(), e.getMessage(),null);
       }catch (ErrorException e) {
            return ResultUtils.error(e.getCode(), e.getMessage() + '\n' + e.getErrorMessage(), null);
       }
    }


    @PutMapping("/dealReport")
    @Transactional
    @ResponseBody
    @ApiOperation(value = "处理违规")
    @ParameterVerify(parameterKey = "rid",parameterName = "违规记录id",parameterCode = ParameterCode.ReportParameter)
    public ResultDTO dealReport(@ApiParam(value = "违规记录id",required = true)@RequestParam(value = "rid",required = true)Integer rid){
        try {
            reportService.updateDeal(rid);
            return ResultUtils.success();
        }catch (SystemException e) {
            return ResultUtils.error(e.getCode(), e.getMessage(),null);
        }catch (ErrorException e) {
            return ResultUtils.error(e.getCode(), e.getMessage() + '\n' + e.getErrorMessage(), null);
        }
    }

    @DeleteMapping("/cancelReport")
    @Transactional
    @ResponseBody
    @ApiOperation(value = "取消上报")
    public ResultDTO cancelReport(@ApiParam(value = "违规记录id",required = true)@RequestParam(value = "rid",required = true)Integer rid){
        try {
            reportService.deleteCancel(rid);
            return ResultUtils.success();
        }catch (SystemException e) {
            return ResultUtils.error(e.getCode(), e.getMessage(),null);
        }catch (ErrorException e) {
            return ResultUtils.error(e.getCode(), e.getMessage() + '\n' + e.getErrorMessage(), null);
        }
    }
}
