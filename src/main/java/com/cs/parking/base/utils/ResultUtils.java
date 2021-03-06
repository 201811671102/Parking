package com.cs.parking.base.utils;

import com.cs.parking.base.DTO.ResultDTO;
import com.cs.parking.code.BaseCode;


/**
 * @ClassName ResultUtils
 * @Description TODO
 * @Author QQ163
 * @Date 2020/8/18 23:32
 **/
public class ResultUtils {
    public static  <T> ResultDTO<T> success(T data){
        return new ResultDTO<T>(BaseCode.Success.getCode(), BaseCode.Success.getMessage(),data);
    }
    public static  <T> ResultDTO<T> success(){
        return new ResultDTO<T>(BaseCode.Success.getCode(),BaseCode.Success.getMessage(),null);
    }
    public static  <T> ResultDTO<T> success(String message,T data){
        return new ResultDTO<T>(BaseCode.Success.getCode(),message,data);
    }
    public static  <T> ResultDTO<T> error(Integer code,String message){
        return new ResultDTO<T>(code,message,null);
    }
    public static  <T> ResultDTO<T> error(Integer code,String message,T data){
        return new ResultDTO<T>(code,message,data);
    }
}
