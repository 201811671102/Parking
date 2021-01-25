package com.cs.parking.dto;

import com.cs.parking.code.ReportTypeCode;
import com.cs.parking.pojo.Report;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName ReportDTO
 * @Description TODO
 * @Author QQ163
 * @Date 2021/1/19 21:40
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportDTO  implements Serializable {
    private static final long serialVersionUID = -4905413849638260769L;

    private Integer id;

    private String carNumber;

    private String reportType;

    private String city;

    private String county;

    private String town;

    private String address;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss",
            timezone = "GMT+8"
    )
    private Date reportTime;

    private Double longitude;

    private Double latitude;

    private String[] reportPhoto;

    private Boolean deal;

    public ReportDTO(Report report) {
        this.id = report.getId();
        this.carNumber = report.getCarNumber();
        this.reportType = ReportTypeCode.getType(report.getReportType());
        this.city = report.getCity();
        this.county = report.getCounty();
        this.town = report.getTown();
        this.address = report.getAddress();
        this.reportTime = report.getReportTime();
        this.longitude = report.getLongitude();
        this.latitude = report.getLatitude();
        this.deal = report.getDeal();
        String reportPhoto = report.getReportPhoto();
        String[] split = reportPhoto.split(";");
        this.reportPhoto = split;
    }
}
