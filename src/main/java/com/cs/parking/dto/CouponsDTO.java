package com.cs.parking.dto;

import com.cs.parking.pojo.Coupons;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName CouponsDTO
 * @Description TODO
 * @Author QQ163
 * @Date 2021/1/18 17:48
 **/

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CouponsDTO implements Serializable {
    private static final long serialVersionUID = 2121865419379854016L;

    private Integer cid;

    private String name;

    private Double price;

    private Integer number;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss",
            timezone = "GMT+8"
    )
    private Date startTime;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss",
            timezone = "GMT+8"
    )
    private Date endTime;

    private Integer parkingSpaceId;

    private boolean overdue;

    public CouponsDTO(Coupons coupons) {
        this.cid = coupons.getId();
        this.name = coupons.getName();
        this.price = coupons.getPrice();
        this.number = coupons.getNumber();
        this.startTime = coupons.getStartTime();
        this.endTime = coupons.getEndTime();
        this.parkingSpaceId = coupons.getParkingSpaceId();
        if (coupons.getEndTime().before(new Date())){
            overdue = true;
        }else{
            overdue = false;
        }
    }
}
