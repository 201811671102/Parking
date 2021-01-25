package com.cs.parking.pojo.DIYpojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName OrderRecordOwnerDTO
 * @Description TODO
 * @Author QQ163
 * @Date 2021/1/21 20:15
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderRecordOwner {

    private String name;

    private String carNumber;

    private String carOwnerPhoneNumber;

    private Integer appointmentTime;

}
