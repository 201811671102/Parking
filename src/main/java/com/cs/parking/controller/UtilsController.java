package com.cs.parking.controller;

import com.cs.parking.base.DTO.ResultDTO;
import com.cs.parking.base.baseinterface.VerifyToken;
import com.cs.parking.code.BaseCode;
import com.cs.parking.base.ftp.FtpOperation;
import com.cs.parking.base.utils.EncodeUtil;
import com.cs.parking.base.utils.RedisUtil;
import com.cs.parking.base.utils.ResultUtils;
import com.cs.parking.exception.ErrorException;
import com.cs.parking.mapper.UserCouponsMapper;
import com.cs.parking.mapper.UserMapper;
import com.cs.parking.pojo.User;
import com.cs.parking.pojo.UserCoupons;
import com.cs.parking.pojo.UserCouponsExample;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @ClassName UtilsController
 * @Description TODO
 * @Author QQ163
 * @Date 2021/1/17 13:14
 **/
@Controller
@RequestMapping("/utils")
@Api(tags = {"工具接口"})
public class UtilsController {

    @Value("${parking.uri}")
    String uri;
    @Value("${parking.nginx.port}")
    String nginxPort;

    @Autowired
    RedisUtil redisUtil;
    @Autowired
    FtpOperation ftpOperation;

    @PostMapping("/cachePhoto")
    @ResponseBody
    @VerifyToken
    @ApiOperation(value = "缓存图片",notes = "图片缓存接口，返回图片url")
    public ResultDTO<String> cachePhoto(@ApiParam(value = "图片",required = true)@RequestParam(value = "photo",required = true)MultipartFile photo){
        try {
            if (!photo.isEmpty()){
                DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd/yyyyMMddHHmmss");
                String originalFilename = photo.getOriginalFilename();
                String type = originalFilename.substring(originalFilename.indexOf(".")+1);
                String url = "/photo/parking/"+dateTimeFormatter.format(LocalDateTime.now())+"."+type;
                try {
                    String photoString = EncodeUtil.getInstance().encodeImage(photo.getInputStream(),type );
                    redisUtil.set(url,photoString,1000*60*3);
                    return ResultUtils.success("http://"+uri+":"+nginxPort+url);
                }catch (Exception e){
                    e.printStackTrace();
                    throw new ErrorException(BaseCode.System_Error,"redis缓存图片错误"+ '\n'+e.getMessage());
                }
            }else{
                throw new ErrorException(BaseCode.FailOperation,"上传内容为空");
            }
        }catch (ErrorException e){
            return ResultUtils.error(e.getCode(),e.getMessage(),e.getErrorMessage());
        }
    }

    @PostMapping("/uploadPhoto")
    @ResponseBody
    @VerifyToken
    @ApiOperation(value = "上传图片",notes = "上传图片接口")
    public ResultDTO uploadPhoto(@ApiParam(value = "url数组",required = true)@RequestParam(value = "urlArray",required = true)String[] urlArray){
        try {
            if (urlArray.length == 0){
                throw new ErrorException(BaseCode.FailOperation,"数组为空");
            }
            Arrays.stream(urlArray)
                    .map((x)->{
                        return x.substring(x.indexOf("70")+2);
                    })
                    .forEach((x)->{
                        String photo = null;
                        try {
                            photo = redisUtil.get(x).toString();
                        }catch (Exception e){
                            throw new ErrorException(BaseCode.System_Error,"获取键为："+x+" 的redis缓存出错！"+'\n'+e.getMessage());
                        }
                        try {
                            ftpOperation.uploadToFtp(EncodeUtil.getInstance().decodeImage(photo),x);
                        }catch (Exception e){
                            throw new ErrorException(BaseCode.System_Error,e.getMessage());
                        }

                    });
            return ResultUtils.success();
        }catch (ErrorException e){
            return ResultUtils.error(e.getCode(),e.getMessage(),e.getErrorMessage());
        }
    }

    @GetMapping("/getDate")
    @ResponseBody
    @ApiOperation(value = "获取当前时间测试")
    public ResultDTO getDate(){
        LocalDateTime localDateTime = LocalDateTime.now();
        Date from = Date.from(localDateTime.atZone(ZoneId.of("Asia/Shanghai")).toInstant());
        return ResultUtils.success(from.toString());
    }
}
