package com.cs.parking.dto;

import com.cs.parking.pojo.ParkingSpace;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName ParkingSpaceOwner
 * @Description TODO
 * @Author QQ163
 * @Date 2021/1/20 11:52
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ParkingSpaceOwner implements Serializable {
    private static final long serialVersionUID = -2858096875769982624L;

    private Integer pid;

    private String shortName;

    private String address;

    private Double price;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss",
            timezone = "GMT+8"
    )
    private Date startTimeYear;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss",
            timezone = "GMT+8"
    )
    private Date endTimeYear;

    private Boolean share;

    private Boolean cancel;

    public ParkingSpaceOwner(ParkingSpace parkingSpace) {
        this.pid = parkingSpace.getId();
        this.shortName = parkingSpace.getShortName();
        this.price = parkingSpace.getPrice();
        this.startTimeYear = parkingSpace.getStartTimeYear();
        this.endTimeYear = parkingSpace.getEndTimeYear();
        this.share = parkingSpace.getShare();
        this.cancel = parkingSpace.getCancel();
        this.address = parkingSpace.getProvince()+parkingSpace.getCity()+parkingSpace.getCounty()+parkingSpace.getTown()+parkingSpace.getAddress();
    }
}
