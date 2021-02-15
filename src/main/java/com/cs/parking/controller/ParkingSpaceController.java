package com.cs.parking.controller;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.cs.parking.base.DTO.ResultDTO;
import com.cs.parking.base.baseinterface.ParameterVerify;
import com.cs.parking.base.baseinterface.VerifyToken;
import com.cs.parking.base.utils.JWTUtil;
import com.cs.parking.base.utils.RedisUtil;
import com.cs.parking.base.utils.ResultUtils;
import com.cs.parking.code.BaseCode;
import com.cs.parking.code.ParameterCode;
import com.cs.parking.code.TimeOfDay;
import com.cs.parking.dto.ParkingSpaceCustomer;
import com.cs.parking.dto.ParkingSpaceOwner;
import com.cs.parking.exception.ErrorException;
import com.cs.parking.exception.SystemException;
import com.cs.parking.pojo.ParkingSpace;
import com.cs.parking.service.DIYService.AppointmentTimeService;
import com.cs.parking.service.OrderRecordService;
import com.cs.parking.service.ParkingSpaceService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @ClassName ParkingSpaceController
 * @Description TODO
 * @Author QQ163
 * @Date 2021/1/20 18:03
 **/
@Controller
@RequestMapping("/parkingSpace")
@Api(tags = "停车位信息操作接口")
public class ParkingSpaceController {
    @Autowired
    ParkingSpaceService parkingSpaceService;
    @Autowired
    AppointmentTimeService appointmentTimeService;
    @Autowired
    OrderRecordService orderRecordService;


    @PostMapping("/append")
    @Transactional
    @ResponseBody
    @VerifyToken
    @ApiOperation(value = "添加共享车位")
    public ResultDTO append(
            HttpServletRequest request,
            @ApiParam(value = "简称",required = true)@RequestParam(value = "shortName",required = true)String shortName,
            @ApiParam(value = "共享开始时间",required = true)@RequestParam(value = "startTimeYear",required = true)Date startTimeYear,
            @ApiParam(value = "共享结束时间",required = true)@RequestParam(value = "endTimeYear",required = true)Date endTimeYear,
            @ApiParam(value = "每日开始共享时间|0~22",required = true)@RequestParam(value = "startTimeDay",required = true)Integer startTimeDay,
            @ApiParam(value = "每日结束共享时间|1~23",required = true)@RequestParam(value = "endTimeDay",required = true)Integer endTimeDay,
            @ApiParam(value = "车位地址|省",required = true)@RequestParam(value = "province",required = true)String province,
            @ApiParam(value = "车位地址|市",required = true)@RequestParam(value = "city",required = true)String city,
            @ApiParam(value = "车位地址|县",required = true)@RequestParam(value = "county",required = true)String county,
            @ApiParam(value = "车位地址|镇",required = true)@RequestParam(value = "town",required = true)String town,
            @ApiParam(value = "车位地址|详细地址",required = true)@RequestParam(value = "address",required = true)String address,
            @ApiParam(value = "租车位电话",required = true)@RequestParam(value = "rentPhoneNumber",required = true)String rentPhoneNumber,
            @ApiParam(value = "价格",required = true)@RequestParam(value = "price",required = true)double price,
            @ApiParam(value = "经度",required = true)@RequestParam(value = "longitude",required = true)double longitude,
            @ApiParam(value = "纬度",required = true)@RequestParam(value = "latitude",required = true)double latitude
            ){
            try {
                DecodedJWT decodedJWT = JWTUtil.getInstance().decodedJWT(request);
                Integer uid = decodedJWT.getClaim("uid").asInt();
                ParkingSpace parkingSpace = new ParkingSpace(uid,shortName,province,city,county,town,address,rentPhoneNumber,price,startTimeYear,endTimeYear,startTimeDay,endTimeDay,longitude,latitude,true,false);
                parkingSpaceService.insert(parkingSpace);
                return ResultUtils.success();
            }catch (SystemException e){
                return ResultUtils.error(e.getCode(), e.getMessage(),null);
            }catch (ErrorException e){
                return ResultUtils.error(e.getCode(),e.getMessage()+'\n'+e.getErrorMessage(),null);
            }
    }

    @PutMapping("/resetParkingSpace")
    @Transactional
    @ResponseBody
    @VerifyToken
    @ApiOperation(value = "修改共享车位")
    @ParameterVerify(parameterKey = "pid",parameterName = "车位id",parameterCode = ParameterCode.ParkingSpaceParameter)
    public ResultDTO resetParkingSpace(
            HttpServletRequest request,
            @ApiParam(value = "车位id",required = true)@RequestParam(value = "pid",required = true)Integer pid,
            @ApiParam(value = "简称",required = false)@RequestParam(value = "shortName",required = false)String shortName,
            @ApiParam(value = "共享开始时间,Mon Oct 01 17:16:04 CST 2020",required = false)@RequestParam(value = "startTimeYear",required = false)Date startTimeYear,
            @ApiParam(value = "共享结束时间",required = false)@RequestParam(value = "endTimeYear",required = false)Date endTimeYear,
            @ApiParam(value = "每日开始共享时间|0~22",required = false)@RequestParam(value = "startTimeDay",required = false)Integer startTimeDay,
            @ApiParam(value = "每日结束共享时间|1~23",required = false)@RequestParam(value = "endTimeDay",required = false)Integer endTimeDay,
            @ApiParam(value = "车位地址|省",required = false)@RequestParam(value = "province",required = false)String province,
            @ApiParam(value = "车位地址|市",required = false)@RequestParam(value = "city",required = false)String city,
            @ApiParam(value = "车位地址|县",required = false)@RequestParam(value = "county",required = false)String county,
            @ApiParam(value = "车位地址|镇",required = false)@RequestParam(value = "town",required = false)String town,
            @ApiParam(value = "车位地址|详细地址",required = false)@RequestParam(value = "address",required = false)String address,
            @ApiParam(value = "租车位电话",required = false)@RequestParam(value = "rentPhoneNumber",required = false)String rentPhoneNumber,
            @ApiParam(value = "价格",required = false)@RequestParam(value = "price",required = false)Double price,
            @ApiParam(value = "经度",required = false)@RequestParam(value = "longitude",required = false)Double longitude,
            @ApiParam(value = "纬度",required = false)@RequestParam(value = "latitude",required = false)Double latitude
    ){
        try {
            DecodedJWT decodedJWT = JWTUtil.getInstance().decodedJWT(request);
            Integer uid = decodedJWT.getClaim("uid").asInt();
            ParkingSpace parkingSpace = new ParkingSpace(pid,uid,shortName,province,city,county,town,address,rentPhoneNumber,price,startTimeYear,endTimeYear,startTimeDay,endTimeDay,longitude,latitude);
            parkingSpaceService.update(parkingSpace);
            return ResultUtils.success();
        }catch (SystemException e){
            return ResultUtils.error(e.getCode(), e.getMessage(),null);
        }catch (ErrorException e){
            return ResultUtils.error(e.getCode(),e.getMessage()+'\n'+e.getErrorMessage(),null);
        }
    }

    @DeleteMapping("/cancelShareParkingSpace")
    @ResponseBody
    @Transactional
    @VerifyToken
    @ApiOperation(value = "取消共享")
    @ParameterVerify(parameterKey = "pid",parameterName = "车位id",parameterCode = ParameterCode.ParkingSpaceParameter)
    public ResultDTO cancelShareParkingSpace(@ApiParam(value = "车位id",required = true)@RequestParam(value = "pid",required = true)Integer pid){
        try {
            parkingSpaceService.unShare(pid);
            return ResultUtils.success();
        }catch (SystemException e){
            return ResultUtils.error(e.getCode(), e.getMessage(),null);
        }catch (ErrorException e){
            return ResultUtils.error(e.getCode(),e.getMessage()+'\n'+e.getErrorMessage(),null);
        }
    }

    @PutMapping("/shareParkingSpace")
    @ResponseBody
    @Transactional
    @VerifyToken
    @ApiOperation(value = "恢复共享")
    @ParameterVerify(parameterKey = "pid",parameterName = "车位id",parameterCode = ParameterCode.ParkingSpaceParameter)
    public ResultDTO shareParkingSpace(@ApiParam(value = "车位id",required = true)@RequestParam(value = "pid",required = true)Integer pid){
        try {
            parkingSpaceService.share(pid);
            return ResultUtils.success();
        }catch (SystemException e){
            return ResultUtils.error(e.getCode(), e.getMessage(),null);
        }catch (ErrorException e){
            return ResultUtils.error(e.getCode(),e.getMessage()+'\n'+e.getErrorMessage(),null);
        }
    }

    @DeleteMapping("/removeParkingSpace")
    @ResponseBody
    @Transactional
    @VerifyToken
    @ApiOperation(value = "删除车位")
    @ParameterVerify(parameterKey = "pid",parameterName = "车位id",parameterCode = ParameterCode.ParkingSpaceParameter)
    public ResultDTO removeParkingSpace(@ApiParam(value = "车位id",required = true)@RequestParam(value = "pid",required = true)Integer pid){
        try {
            parkingSpaceService.delete(pid);
            return ResultUtils.success();
        }catch (SystemException e){
            return ResultUtils.error(e.getCode(), e.getMessage(),null);
        }catch (ErrorException e){
            return ResultUtils.error(e.getCode(),e.getMessage()+'\n'+e.getErrorMessage(),null);
        }
    }

    @GetMapping("/searchOwnerParkingSpace")
    @ResponseBody
    @Transactional
    @VerifyToken
    @ApiOperation(value = "获取用户所拥有车位信息")
    public ResultDTO<PageInfo<ParkingSpaceOwner>> searchOwnerParkingSpace(
            HttpServletRequest request,
            @ApiParam(value = "起始页>=0",required = true)@RequestParam(value = "offset",required = true)Integer offset,
            @ApiParam(value = "每页数据数",required = true)@RequestParam(value = "limit",required = true)Integer limit,
            @ApiParam(value = "是否分享",required = true)@RequestParam(value = "share",required = true)boolean share
    ){
        try {
            DecodedJWT decodedJWT = JWTUtil.getInstance().decodedJWT(request);
            Integer uid = decodedJWT.getClaim("uid").asInt();
            PageHelper.startPage(offset,limit);
            List<ParkingSpaceOwner> parkingSpaceOwnerList = parkingSpaceService.selectParkingSpaceUser(uid, share);
            PageInfo<ParkingSpaceOwner> parkingSpaceOwnerPageInfo = new PageInfo<>(parkingSpaceOwnerList);
            return ResultUtils.success(parkingSpaceOwnerPageInfo);
        }catch (SystemException e){
            return ResultUtils.error(e.getCode(), e.getMessage(),null);
        }catch (ErrorException e){
            return ResultUtils.error(e.getCode(),e.getMessage()+'\n'+e.getErrorMessage(),null);
        }
    }

    @GetMapping("/searchCustomerParkingSpace")
    @ResponseBody
    @VerifyToken
    @Transactional
    @ApiOperation(value = "经纬度获取附件停车位")
    public ResultDTO<PageInfo<ParkingSpaceCustomer>> searchCustomerParkingSpace(
            HttpServletRequest request,
            @ApiParam(value = "最小经度",required = true)@RequestParam(value = "LLong",required = true)double LLong,
            @ApiParam(value = "最大经度",required = true)@RequestParam(value = "RLong",required = true)double RLong,
            @ApiParam(value = "最小纬度",required = true)@RequestParam(value = "BLat",required = true)double BLat,
            @ApiParam(value = "最大纬度",required = true)@RequestParam(value = "TLat",required = true)double TLat,
            @ApiParam(value = "起始页>=0",required = true)@RequestParam(value = "offset",required = true)Integer offset,
            @ApiParam(value = "每页数据数",required = true)@RequestParam(value = "limit",required = true)Integer limit
    ){
        try {
            PageHelper.startPage(offset,limit);
            List<ParkingSpaceCustomer> parkingSpaceCustomerList = parkingSpaceService.selectParkingSpaceCustomer(TLat, BLat, LLong, RLong);
            PageInfo<ParkingSpaceCustomer> parkingSpaceOwnerPageInfo = new PageInfo<>(parkingSpaceCustomerList);
            return ResultUtils.success(parkingSpaceOwnerPageInfo);
        }catch (SystemException e){
            return ResultUtils.error(e.getCode(), e.getMessage(),null);
        }catch (ErrorException e){
            return ResultUtils.error(e.getCode(),e.getMessage()+'\n'+e.getErrorMessage(),null);
        }
    }

    @GetMapping("/searchAppointmentTime")
    @ResponseBody
    @Transactional
    @VerifyToken
    @ApiOperation(value = "获取车位今日起全部可预约时间段",notes = "对数据库操作时间长，不建议使用")
    public ResultDTO<Map<LocalDate, Map<Integer, String>>> searchAppointmentTime(@ApiParam(value = "车位id",required = true)@RequestParam(value = "pid",required = true)Integer pid){
        try {
            Map<LocalDate, Map<Integer, String>> localDateMapMap = appointmentTimeService.searchAppointmentTime(pid);
            return ResultUtils.success(localDateMapMap);
        }catch (SystemException e){
            return ResultUtils.error(e.getCode(), e.getMessage(),null);
        }catch (ErrorException e){
            return ResultUtils.error(e.getCode(),e.getMessage()+'\n'+e.getErrorMessage(),null);
        }
    }

    @GetMapping("/searchAppointmentTimeDate")
    @ResponseBody
    @Transactional
    @VerifyToken
    @ApiOperation(value = "获取车位特定日期可预约时间段",notes = "方法一")
    public ResultDTO<Map<Integer, String>> searchAppointmentTimeDate(
            @ApiParam(value = "车位id",required = true)@RequestParam(value = "pid",required = true)Integer pid,
            @ApiParam(value = "预约日期",required = true)@RequestParam(value = "date",required = true)Date date
    ){
        try {
            Map<Integer, String> localDateMapMap = appointmentTimeService.searchAppointmentTime(pid,date);
            return ResultUtils.success(localDateMapMap);
        }catch (SystemException e){
            return ResultUtils.error(e.getCode(), e.getMessage(),null);
        }catch (ErrorException e){
            return ResultUtils.error(e.getCode(),e.getMessage()+'\n'+e.getErrorMessage(),null);
        }
    }

    @GetMapping("/searchAppointmentTimeDateTwo")
    @ResponseBody
    @Transactional
    @VerifyToken
    @ApiOperation(value = "获取车位特定日期可预约时间段",notes = "方法二")
    public ResultDTO<Map<Integer, String>> searchAppointmentTimeDateTwo(
            @ApiParam(value = "车位id",required = true)@RequestParam(value = "pid",required = true)Integer pid,
            @ApiParam(value = "预约日期",required = true)@RequestParam(value = "date",required = true)Date date
    ){
        try {
            ParkingSpace parkingSpace = parkingSpaceService.selectId(pid);
            if (parkingSpace == null){
                throw new ErrorException(BaseCode.Null,"没有pid为："+pid+" 的车位信息");
            }
            if (date.after(parkingSpace.getEndTimeYear())){
                throw new ErrorException(BaseCode.FailOperation,"预约时间不能在车位共享截至日期后");
            }
            if (date.before(parkingSpace.getStartTimeYear())){
                throw new ErrorException(BaseCode.FailOperation,"预约时间不能在车位共享开始日期前");
            }
            Map<Integer, String> localDateMapMap = orderRecordService.searchAppointmentTime(pid,date,TimeOfDay.getAppointment(parkingSpace.getStartTimeDay(),parkingSpace.getEndTimeDay()));
            return ResultUtils.success(localDateMapMap);
        }catch (SystemException e){
            return ResultUtils.error(e.getCode(), e.getMessage(),null);
        }catch (ErrorException e){
            return ResultUtils.error(e.getCode(),e.getMessage()+'\n'+e.getErrorMessage(),null);
        }
    }
}
