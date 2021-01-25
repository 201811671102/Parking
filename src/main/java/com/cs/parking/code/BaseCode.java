package com.cs.parking.code;

/**
 * @ClassName BaseExceptionCode
 * @Description TODO
 * @Author QQ163
 * @Date 2021/1/17 0:45
 **/
public enum  BaseCode {

    Success(200,"成功"),
    Null(404,"查无数据"),
    System_Error(500,"系统错误"),
    FailOperation(50001,"操作错误"),
    JWTError(50002,"jwt校验失败"),
    FTPError(50003,"ftp错误"),
    RedisError(50004,"redis错误"),
    IOError(50005,"io操作错误");

    private Integer code;
    private String message;
    BaseCode(Integer code,String message){
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
