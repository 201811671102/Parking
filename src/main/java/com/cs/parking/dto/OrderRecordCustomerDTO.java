package com.cs.parking.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @ClassName OrderRecordOwnerDTO
 * @Description TODO
 * @Author QQ163
 * @Date 2021/1/21 20:15
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderRecordCustomerDTO {

    private Integer orId;

    private String shortName;

    private String address;

    private String carNumber;

    private String carOwnerPhoneNumber;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss",
            timezone = "GMT+8"
    )
    private Date payTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss",
            timezone = "GMT+8"
    )
    private Date effectTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss",
            timezone = "GMT+8"
    )
    private Date failureTime;

    private Double paymentAmount;

    private Integer orderState;

    private Double longitude;

    private Double latitude;

}
