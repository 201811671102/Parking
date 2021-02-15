package com.cs.parking.controller;

import com.alibaba.fastjson.JSONObject;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.cs.parking.base.DTO.ResultDTO;
import com.cs.parking.base.baseinterface.VerifyToken;
import com.cs.parking.base.utils.*;
import com.cs.parking.code.BaseCode;
import com.cs.parking.code.Protocol;
import com.cs.parking.code.QuartzJobCode;
import com.cs.parking.code.UserExceptionCode;
import com.cs.parking.dto.LoginDTO;
import com.cs.parking.exception.ErrorException;
import com.cs.parking.exception.SystemException;
import com.cs.parking.manager.SchedulerManager;
import com.cs.parking.pojo.User;
import com.cs.parking.service.NoticeMessageService;
import com.cs.parking.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.quartz.JobDataMap;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import sun.rmi.runtime.Log;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

/**
 * @ClassName UserController
 * @Description TODO
 * @Author QQ163
 * @Date 2021/1/16 18:08
 **/
@Controller
@RequestMapping("/user")
@Api(tags = {"用户信息操作接口"})
@Log4j2
public class UserController {

    @Autowired
    RedisUtil redisUtil;
    @Autowired
    UserService userService;
    @Autowired
    NoticeMessageService noticeMessageService;
    @Autowired
    SchedulerManager schedulerManager;

    @PostMapping("/refreshToken")
    @ResponseBody
    @VerifyToken
    @ApiOperation(value = "token刷新",notes = "token失效前通过此接口刷新token")
    public ResultDTO<String> refreshToken(HttpServletRequest request){
        try {
            return ResultUtils.success(JWTUtil.getInstance().refreshJWT(request));
        }catch (SystemException e) {
            return ResultUtils.error(e.getCode(), e.getMessage(),null);
        }catch (ErrorException e){
            return ResultUtils.error(e.getCode(),e.getMessage()+'\n'+e.getErrorMessage(),null);
        }
    }


    @PostMapping("/login")
    //开启事务
    @Transactional
    @ResponseBody
    @ApiOperation(value = "用户登录",notes = "微信登录接口，返回用户信息及签名认证jwt，存放在消息头，键名为token,时效1小时")
    public ResultDTO<LoginDTO> login(@ApiParam(value = "微信小程序的code",required = true)@RequestParam(value = "code",required = true)String code){
        User user =  null;
        try {
            JSONObject SessionKeyOpenId = new WechatUtil().getSessionKeyOrOpenId(code);
            String openid = SessionKeyOpenId.getString("openid");
            if (!StringUtils.isNotBlank(openid)){
                throw new NullPointerException();
            }
            if (redisUtil.hasKey(openid)){
                user = (User) redisUtil.get(openid);
            }else {
                user = userService.selectByOpenid(openid);
                if (user == null){
                    user = new User();
                    user.setOpenId(openid);
                    userService.insertUser(user);
                }
                redisUtil.set(openid,user);
            }
            String jwt = JWTUtil.getInstance().createJWT(user.getId());
            LoginDTO loginDTO = new LoginDTO(user,jwt);
            return ResultUtils.success(loginDTO);
        }catch (SystemException e) {
            return ResultUtils.error(e.getCode(), e.getMessage(),null);
        }catch (ErrorException e){
            return ResultUtils.error(e.getCode(),e.getMessage()+'\n'+e.getErrorMessage(),null);
        }finally {

        }
    }

    @PutMapping("/authentication")
    @Transactional
    @VerifyToken
    @ResponseBody
    @ApiOperation(value = "实名认证",notes = "")
    public ResultDTO authentication(
            HttpServletRequest request,
            @ApiParam(value = "姓名",required = true)@RequestParam(value = "name",required = true)String name,
            @ApiParam(value = "证件类型,1身份证，2护照，3澳门香港，4台湾",required = true)@RequestParam(value = "idType",required = true)Integer idType,
            @ApiParam(value = "证件号",required = true)@RequestParam(value = "idNumber",required = true)String idNumber
    ){
        try {
            DecodedJWT decodedJWT = JWTUtil.getInstance().decodedJWT(request);
            User user = userService.selectById(decodedJWT.getClaim("uid").asInt());
            if (user == null){
                throw new SystemException(BaseCode.Null);
            }else if (StringUtils.isNotBlank(user.getName())){
                throw new SystemException(UserExceptionCode.Authentication);
            }else{
                user.setName(name);
                user.setIdType(idType);
                user.setIdNumber(idNumber);
                userService.updateUser(user);
                return ResultUtils.success();
            }
        }catch (SystemException e){
            return ResultUtils.error(e.getCode(), e.getMessage(),null);
        }catch (ErrorException e){
            return ResultUtils.error(e.getCode(),e.getMessage()+'\n'+e.getErrorMessage(),null);
        }
    }

    @PutMapping("/VIPTopUp")
    @Transactional
    @VerifyToken
    @ResponseBody
    @ApiOperation(value = "vip充值")
    public ResultDTO<String> VIPTopUp(
            HttpServletRequest request,
            @ApiParam(value = "充值月份",required = true)@RequestParam(value = "months",required = true)Integer months){
        try {
            DecodedJWT decodedJWT = JWTUtil.getInstance().decodedJWT(request);
            User user = userService.selectById(decodedJWT.getClaim("uid").asInt());
            Date vipTime = user.getVipTime();
            ZoneId zoneId = ZoneId.of("Asia/Shanghai");
            if (vipTime == null){
                vipTime = new Date();
            }
            LocalDateTime localDateTime = vipTime.toInstant().atZone(zoneId).toLocalDateTime();
            localDateTime = localDateTime.plusMonths(months);
            user.setVipTime(Date.from(localDateTime.atZone(zoneId).toInstant()));
            userService.updateUser(user);
            String jobKey =  String.valueOf(System.currentTimeMillis()) + user.getId().toString();
            String jobGroup = QuartzJobCode.Order_Coming_Remind_First.getCode().toString();
            JobDataMap jobDataMap = new JobDataMap();
            jobDataMap.put("type", QuartzJobCode.Vip_Over_Remind);
            jobDataMap.put("uid",user.getId());
            jobDataMap.put("vipTime",localDateTime);
            schedulerManager.add(jobKey,jobGroup,localDateTime.minusDays(10),jobDataMap);
            return ResultUtils.success(localDateTime.format(DateTimeFormatter.ofPattern("yyy-MM-dd HH:mm:ss")));
        }catch (SystemException e){
            return ResultUtils.error(e.getCode(), e.getMessage(),null);
        }catch (ErrorException e){
            return ResultUtils.error(e.getCode(),e.getMessage()+'\n'+e.getErrorMessage(),null);
        } catch (SchedulerException e) {
            return ResultUtils.error(BaseCode.System_Error.getCode(),e.getMessage());
        }
    }
}
