package com.cs.parking.code;

public enum OrderRecordExceptionCode {
    Order_Cancel_Overdue(40401,"订单已生效，无法撤销")
    ;

    OrderRecordExceptionCode(Integer code,String message){
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
