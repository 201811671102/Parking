package com.cs.parking.dto;

import com.cs.parking.pojo.ParkingSpace;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @ClassName ParkingSpaceOwner
 * @Description TODO
 * @Author QQ163
 * @Date 2021/1/20 11:52
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ParkingSpaceCustomer implements Serializable {
    private static final long serialVersionUID = -2858096875769982624L;

    private Integer pid;

    private String shortName;

    private String address;

    private Double price;

    private String rentPhoneNumber;

    private Double longitude;

    private Double latitude;

    private long watchedNumber;


    public ParkingSpaceCustomer(ParkingSpace parkingSpace,long watchedNumber) {
        this.pid = parkingSpace.getId();
        this.shortName = parkingSpace.getShortName();
        this.price = parkingSpace.getPrice();
        this.address = parkingSpace.getProvince()+parkingSpace.getCity()+parkingSpace.getCounty()+parkingSpace.getTown()+parkingSpace.getAddress();
        this.rentPhoneNumber = parkingSpace.getRentPhoneNumber();
        this.longitude = parkingSpace.getLongitude();
        this.latitude = parkingSpace.getLatitude();
        this.watchedNumber = watchedNumber;
    }
}
