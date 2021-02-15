package com.cs.parking.controller;

import com.alibaba.fastjson.JSONObject;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.cs.parking.base.DTO.ResultDTO;
import com.cs.parking.base.baseinterface.ParameterVerify;
import com.cs.parking.base.baseinterface.VerifyToken;
import com.cs.parking.base.utils.CornTimeUtil;
import com.cs.parking.base.utils.JWTUtil;
import com.cs.parking.base.utils.MessageUtil;
import com.cs.parking.base.utils.ResultUtils;
import com.cs.parking.code.BaseCode;
import com.cs.parking.code.ParameterCode;
import com.cs.parking.code.Protocol;
import com.cs.parking.code.QuartzJobCode;
import com.cs.parking.dto.OrderRecordCustomerDTO;
import com.cs.parking.dto.OrderRecordOwnerDTO;
import com.cs.parking.dto.ParkingSpaceOwner;
import com.cs.parking.dto.ParkingSpaceRent;
import com.cs.parking.exception.ErrorException;
import com.cs.parking.exception.SystemException;
import com.cs.parking.manager.ConnManager;
import com.cs.parking.manager.SchedulerManager;
import com.cs.parking.pojo.OrderRecord;
import com.cs.parking.pojo.ScheduleJob;
import com.cs.parking.service.DIYService.DIYOrderRecordService;
import com.cs.parking.service.OrderRecordService;
import com.cs.parking.service.ParkingSpaceService;
import com.cs.parking.service.ScheduleJobService;
import com.cs.parking.service.UserCouponsService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import javafx.concurrent.ScheduledService;
import org.apache.commons.lang3.StringUtils;
import org.quartz.JobDataMap;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName OrderRecordController
 * @Description TODO
 * @Author QQ163
 * @Date 2021/1/22 0:04
 **/
@Controller
@RequestMapping("/orderRecord")
@Api(tags = "订单信息操作接口")
public class OrderRecordController {
    @Autowired
    OrderRecordService orderRecordService;
    @Autowired
    UserCouponsService userCouponsService;
    @Autowired
    DIYOrderRecordService diyOrderRecordService;
    @Autowired
    ParkingSpaceService parkingSpaceService;
    @Autowired
    SchedulerManager schedulerManager;

    @DeleteMapping("/cancelOrderRecord")
    @ResponseBody
    @VerifyToken
    @Transactional
    @ApiOperation(value = "取消订单")
    public ResultDTO cancelOrderRecord(@ApiParam(value = "订单id",required = true)@RequestParam(value = "orId",required = true)Integer orId){
        try {
            orderRecordService.delete(orId);
            return ResultUtils.success();
        }catch (SystemException e) {
            return ResultUtils.error(e.getCode(), e.getMessage(),null);
        }catch (ErrorException e){
            return ResultUtils.error(e.getCode(),e.getMessage()+'\n'+e.getErrorMessage(),null);
        }
    }

    @PostMapping("/appendOrderRecord")
    @ResponseBody
    @VerifyToken
    @Transactional
    @ApiOperation(value = "新增订单")
    public ResultDTO appendOrderRecord(
            HttpServletRequest request,
            @ApiParam(value = "车位id",required = true)@RequestParam(value = "pid",required = true)Integer pid,
            @ApiParam(value = "用户优惠卷id",required = false)@RequestParam(value = "ucIdArr",required = false)Integer[] ucIdArr,
            @ApiParam(value = "车牌号",required = true)@RequestParam(value = "carNumber",required = true)String carNumber,
            @ApiParam(value = "车主电话",required = true)@RequestParam(value = "carOwnerPhoneNumber",required = true)String carOwnerPhoneNumber,
            @ApiParam(value = "支付金额",required = true)@RequestParam(value = "paymentAmount",required = true)Double paymentAmount,
            @ApiParam(value = "预约日期",required = true)@RequestParam(value = "appointmentDate",required = true)Date appointmentDate,
            @ApiParam(value = "预约时间段，0~23",required = true)@RequestParam(value = "appointmentTimeArr",required = true)Integer[] appointmentTimeArr
    ){
        try {
            List<Integer> appointmentTimeList = Arrays.stream(appointmentTimeArr)
                    .sorted(Integer::compareTo)
                    .collect(Collectors.toList());
            String appointmentTime = StringUtils.join(appointmentTimeList, ";");
            DecodedJWT decodedJWT = JWTUtil.getInstance().decodedJWT(request);
            Integer uid = decodedJWT.getClaim("uid").asInt();
            LocalDateTime localDateTime = appointmentDate.toInstant().atZone(ZoneId.of("Asia/Shanghai")).toLocalDateTime();
            LocalDateTime effectTimeLocal = localDateTime.withHour(appointmentTimeList.get(0)).withMinute(0).withSecond(0);
            LocalDateTime failureTimeLocal = localDateTime.withHour(appointmentTimeList.get(appointmentTimeList.size() - 1)+1).withMinute(0).withSecond(0);
            Date effectTime = Date.from(effectTimeLocal.atZone(ZoneId.of("Asia/Shanghai")).toInstant());
            Date failureTime = Date.from(failureTimeLocal.atZone(ZoneId.of("Asia/Shanghai")).toInstant());
            OrderRecord orderRecord = new OrderRecord(pid,uid,carNumber,carOwnerPhoneNumber,new Date(),effectTime,failureTime,appointmentTime,paymentAmount,0);
            if (ucIdArr != null && ucIdArr.length > 0) {
                StringBuilder stringBuilder = new StringBuilder();
                for (Integer ucId : ucIdArr) {
                    userCouponsService.delete(ucId);
                    stringBuilder.append(ucId);
                    stringBuilder.append(";");
                }
                orderRecord.setCouponsId(stringBuilder.toString());
            }
            orderRecordService.insert(orderRecord);
            /*
            * 定时任务
            * */
            appointmentTimeList
                    .forEach(integer -> {
                        CornTimeUtil cornTimeUtil = CornTimeUtil.getInstance();
                        LocalDateTime orderComingRemindFirst = localDateTime.withHour(integer).withMinute(0).withSecond(0).minusMinutes(30);
                        JobDataMap jobDataMap = new JobDataMap();
                        jobDataMap.put("type", QuartzJobCode.Order_Coming_Remind_First);
                        jobDataMap.put("uid",uid);
                        jobDataMap.put("effectTime",orderComingRemindFirst);
                        jobDataMap.put("orId",orderRecord.getId());
                        String jobKey =  String.valueOf(System.currentTimeMillis()) + uid.toString();
                        String jobGroup = QuartzJobCode.Order_Coming_Remind_First.getCode().toString();
                        Date jobTime = Date.from(localDateTime.withHour(integer).withMinute(0).withSecond(0).atZone(ZoneId.of("Asia/Shanghai")).toInstant());
                        try {
                            schedulerManager.add(jobKey,jobGroup,orderComingRemindFirst,jobDataMap);
                        } catch (SchedulerException e) {
                            throw new SystemException(BaseCode.System_Error);
                        }
                    });
            ParkingSpaceRent parkingSpaceRent = parkingSpaceService.selectIdNotice(pid);
            Protocol parking_space_rent_notice = Protocol.Parking_Space_Rent_Notice;
            parking_space_rent_notice.setData(parkingSpaceRent);
            MessageUtil.unicast(parkingSpaceRent.getUid(),parking_space_rent_notice);
            return ResultUtils.success();
        }catch (SystemException e) {
            return ResultUtils.error(e.getCode(), e.getMessage(),null);
        }catch (ErrorException e){
            return ResultUtils.error(e.getCode(),e.getMessage()+'\n'+e.getErrorMessage(),null);
        } catch (IOException e) {
            return ResultUtils.error(BaseCode.System_Error.getCode(),e.getMessage(),null);
        }
    }

    @GetMapping("/searchOrderRecordUser")
    @ResponseBody
    @VerifyToken
    @Transactional
    @ApiOperation(value = "查询用户订单记录",notes = "订单状态 0未开始，1进行中，2已完成")
    public ResultDTO<PageInfo<OrderRecordCustomerDTO>> searchOrderRecordUser(
            HttpServletRequest request,
            @ApiParam(value = "起始页>=0",required = true)@RequestParam(value = "offset",required = true)Integer offset,
            @ApiParam(value = "每页数据数",required = true)@RequestParam(value = "limit",required = true)Integer limit
    ){
        try {
            PageHelper.startPage(offset,limit);
            DecodedJWT decodedJWT = JWTUtil.getInstance().decodedJWT(request);
            Integer uid = decodedJWT.getClaim("uid").asInt();
            List<OrderRecordCustomerDTO> orderRecordCustomerDTOList = diyOrderRecordService.selectOrderRecordUser(uid);
            PageInfo<OrderRecordCustomerDTO> orderRecordCustomerDTOPageInfo = new PageInfo<>(orderRecordCustomerDTOList);
            return ResultUtils.success(orderRecordCustomerDTOPageInfo);
        }catch (SystemException e) {
            return ResultUtils.error(e.getCode(), e.getMessage(),null);
        }catch (ErrorException e){
            return ResultUtils.error(e.getCode(),e.getMessage()+'\n'+e.getErrorMessage(),null);
        }
    }

    @GetMapping("/searchOrderRecordOwner")
    @ResponseBody
    @Transactional
    @VerifyToken
    @ApiOperation(value = "查询用户某车位某日订单情况")
    @ParameterVerify(parameterKey = "pid",parameterName = "车位id",parameterCode = ParameterCode.ParkingSpaceParameter)
    public ResultDTO<List<OrderRecordOwnerDTO>> searchOrderRecordOwner(
            @ApiParam(value = "车位id",required = true)@RequestParam(value = "pid",required = true)Integer pid,
            @ApiParam(value = "日期",required = true)@RequestParam(value = "date",required = true)Date date
    ){
        try {
            List<OrderRecordOwnerDTO> orderRecordOwnerDTOS = diyOrderRecordService.searchOrderRecordOwner(pid, date);
            return ResultUtils.success(orderRecordOwnerDTOS);
        }catch (SystemException e) {
            return ResultUtils.error(e.getCode(), e.getMessage(),null);
        }catch (ErrorException e){
            return ResultUtils.error(e.getCode(),e.getMessage()+'\n'+e.getErrorMessage(),null);
        }
    }
}
