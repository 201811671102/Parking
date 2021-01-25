package com.cs.parking.controller;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.cs.parking.base.DTO.ResultDTO;
import com.cs.parking.base.baseinterface.ParameterVerify;
import com.cs.parking.base.baseinterface.VerifyToken;
import com.cs.parking.base.utils.JWTUtil;
import com.cs.parking.base.utils.ResultUtils;
import com.cs.parking.code.BaseCode;
import com.cs.parking.code.ParameterCode;
import com.cs.parking.code.QuartzJobCode;
import com.cs.parking.dto.UserCouponsDTO;
import com.cs.parking.exception.ErrorException;
import com.cs.parking.exception.SystemException;
import com.cs.parking.manager.SchedulerManager;
import com.cs.parking.pojo.Coupons;
import com.cs.parking.service.CouponsService;
import com.cs.parking.service.UserCouponsService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.quartz.JobDataMap;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

/**
 * @ClassName UserCouponsController
 * @Description TODO
 * @Author QQ163
 * @Date 2021/1/19 16:28
 **/
@Controller
@Api(tags = {"用户优惠卷信息操作接口"})
@RequestMapping("/userCoupons")
public class UserCouponsController {
    @Autowired
    UserCouponsService userCouponsService;
    @Autowired
    CouponsService couponsService;
    @Autowired
    SchedulerManager schedulerManager;

    @PostMapping("/append")
    @VerifyToken
    @ApiOperation(value = "用户新增优惠卷")
    @Transactional
    @ResponseBody
    public ResultDTO append(
            HttpServletRequest request,
            @ApiParam(value = "优惠卷id",required = true)@RequestParam(value = "cid",required = true)Integer cid
    ){
        try {
            Coupons coupons = couponsService.selectById(cid);
            if (coupons == null){
                throw new ErrorException(BaseCode.Null,"没有id为："+cid+" 的优惠卷");
            }
            DecodedJWT decodedJWT = JWTUtil.getInstance().decodedJWT(request);
            Integer uid = decodedJWT.getClaim("uid").asInt();
            couponsService.reduce(cid);
            userCouponsService.insert(uid,cid);
            LocalDateTime endlocalDateTime = coupons.getEndTime().toInstant().atZone(ZoneId.of("Asia/Shanghai")).toLocalDateTime();
            LocalDateTime startlocalDateTime = coupons.getStartTime().toInstant().atZone(ZoneId.of("Asia/Shanghai")).toLocalDateTime();
            long until = startlocalDateTime.until(endlocalDateTime, ChronoUnit.DAYS);
            JobDataMap jobDataMap = new JobDataMap();
            jobDataMap.put("type", QuartzJobCode.Order_Coming_Remind_First);
            jobDataMap.put("uid",uid);
            jobDataMap.put("CouponsTime",endlocalDateTime);
            String jobKey =  String.valueOf(System.currentTimeMillis()) + uid.toString();
            String jobGroup = QuartzJobCode.Coupons_Over.getCode().toString();
            schedulerManager.add(jobKey,jobGroup,endlocalDateTime.minusDays(until/2),jobDataMap);
            return ResultUtils.success();
        }catch (SystemException e){
            return ResultUtils.error(e.getCode(), e.getMessage(),null);
        }catch (ErrorException e){
            return ResultUtils.error(e.getCode(),e.getMessage()+'\n'+e.getErrorMessage(),null);
        } catch (SchedulerException e) {
            return ResultUtils.error(BaseCode.System_Error.getCode(),e.getMessage());
        }
    }


    @GetMapping("/searchCoupons")
    @VerifyToken
    @ApiOperation(value = "用户查询所拥有优惠卷")
    @Transactional
    @ResponseBody
    public ResultDTO<PageInfo<UserCouponsDTO>> searchCoupons(
            HttpServletRequest request,
            @ApiParam(value = "是否保留过期优惠卷",required = true)@RequestParam(value = "overdue",required = true)boolean overdue,
            @ApiParam(value = "是否保留已使用优惠卷",required = true)@RequestParam(value = "used",required = true)boolean used,
            @ApiParam(value = "起始页>=0",required = true)@RequestParam(value = "offset",required = true)Integer offset,
            @ApiParam(value = "每页数据数",required = true)@RequestParam(value = "limit",required = true)Integer limit
        ){
        try {
            PageHelper.startPage(offset,limit);
            DecodedJWT decodedJWT = JWTUtil.getInstance().decodedJWT(request);
            List<UserCouponsDTO> userCouponsDTOList = userCouponsService.selectAll(decodedJWT.getClaim("uid").asInt(), overdue, used);
            PageInfo<UserCouponsDTO> pageInfo = new PageInfo<>(userCouponsDTOList);
            return ResultUtils.success(pageInfo);
        }catch (SystemException e){
            return ResultUtils.error(e.getCode(), e.getMessage(),null);
        }catch (ErrorException e){
            return ResultUtils.error(e.getCode(),e.getMessage()+'\n'+e.getErrorMessage(),null);
        }
    }
}
