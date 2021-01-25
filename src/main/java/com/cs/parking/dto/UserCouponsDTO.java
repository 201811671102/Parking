package com.cs.parking.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName UserCouponsDTO
 * @Description TODO
 * @Author QQ163
 * @Date 2021/1/19 15:14
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserCouponsDTO implements Serializable {
    private static final long serialVersionUID = 9213761386151861120L;

    private Integer cid;

    private String name;

    private Double price;

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
    //优惠卷是否取消
    private boolean cancel;
}
