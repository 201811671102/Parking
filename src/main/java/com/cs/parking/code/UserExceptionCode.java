package com.cs.parking.code;



/**
 * @ClassName ExceptionCode
 * @Description TODO
 * @Author QQ163
 * @Date 2021/1/16 18:39
 **/
public enum  UserExceptionCode {

    NoToken(10300,"请求没有token或token已经过期"),
    Authentication(10401,"该用户已经实名认证");

    UserExceptionCode(Integer code,String message){
        this.code = code;
        this.message = message;
    }
    private Integer code;
    private String message;

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
