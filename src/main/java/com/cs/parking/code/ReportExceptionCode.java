package com.cs.parking.code;

/**
 * @ClassName ReportExceptionCode
 * @Description TODO
 * @Author QQ163
 * @Date 2021/1/20 1:58
 **/
public enum  ReportExceptionCode {
    UnCancelDeal(30400,"违规记录已经处理，无法撤销"),
    UnCancelOverdue(30401,"违规记录撤销时间已超时，无法撤销")
    ;

    ReportExceptionCode(Integer code,String message){
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
