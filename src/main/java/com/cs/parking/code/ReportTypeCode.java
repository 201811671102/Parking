package com.cs.parking.code;

import io.swagger.models.auth.In;

/**
 * @ClassName ReportTypeCode
 * @Description TODO
 * @Author QQ163
 * @Date 2021/1/20 15:24
 **/
public enum  ReportTypeCode {
    DisorderlyParkingPlace(1,"乱停乱放"),
    IllegalOccupancyOfEmergencyLanes(2,"Illegal occupancy of emergency lanes")
    ;
    private int code;
    private String reportType;

    ReportTypeCode(int code, String reportType) {
        this.code = code;
        this.reportType = reportType;
    }

    public int getCode() {
        return code;
    }

    public String getReportType() {
        return reportType;
    }

    public static String getType(int code){
        ReportTypeCode[] values = ReportTypeCode.values();
        for (ReportTypeCode reportTypeCode : values){
            if (reportTypeCode.getCode() == code){
                return reportTypeCode.getReportType();
            }
        }
        return null;
    }
}
