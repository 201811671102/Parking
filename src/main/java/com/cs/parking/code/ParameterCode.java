package com.cs.parking.code;

/**
 * @ClassName ParameterCode
 * @Description TODO
 * @Author QQ163
 * @Date 2021/1/19 2:58
 **/
public enum  ParameterCode {
    UserParameter(1),
    CouponsParameter(2),
    OrderRecordParameter(3),
    ParkingSpaceParameter(4),
    ReportParameter(5),
    UserCouponsParameter(6),
    ;
    private int code;

    ParameterCode(int code) {
        this.code = code;
    }
}
