package com.cs.parking.code;

public enum CouponsExceptionCode {
    NOCoupons(20400,"优惠卷售罄")
    ;


    CouponsExceptionCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    private int code;
    private String message;

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
