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
 * @ClassName ParkingSpaceRent
 * @Description TODO
 * @Author QQ163
 * @Date 2021/1/25 15:56
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ParkingSpaceRent implements Serializable {
    private static final long serialVersionUID = -4857175304120686751L;

    private int uid;

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

    public ParkingSpaceRent(ParkingSpace parkingSpace) {
        this.uid = parkingSpace.getUserId();
        this.shortName = parkingSpace.getShortName();
        this.price = parkingSpace.getPrice();
        this.startTimeYear = parkingSpace.getStartTimeYear();
        this.endTimeYear = parkingSpace.getEndTimeYear();
        this.address = parkingSpace.getProvince()+parkingSpace.getCity()+parkingSpace.getCounty()+parkingSpace.getTown()+parkingSpace.getAddress();
    }
}
