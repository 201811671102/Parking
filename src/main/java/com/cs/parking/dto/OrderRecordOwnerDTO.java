package com.cs.parking.dto;

import com.cs.parking.code.TimeOfDay;
import com.cs.parking.pojo.DIYpojo.OrderRecordOwner;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName OrderRecordOwnerDTO
 * @Description TODO
 * @Author QQ163
 * @Date 2021/1/21 20:15
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderRecordOwnerDTO {

    private String name;

    private String carNumber;

    private String carOwnerPhoneNumber;

    private Map<Integer,String> appointmentTime;

    public OrderRecordOwnerDTO(OrderRecordOwner orderRecordOwner) {
        this.name = orderRecordOwner.getName();
        this.carNumber = orderRecordOwner.getCarNumber();
        this.carOwnerPhoneNumber = orderRecordOwner.getCarOwnerPhoneNumber();
        Map<Integer,String> appointmentTimeMap = new HashMap<>();
        Integer appointmentTime = orderRecordOwner.getAppointmentTime();
        String time = TimeOfDay.getTime(appointmentTime);
        appointmentTimeMap.put(appointmentTime,time);
        this.appointmentTime = appointmentTimeMap;
    }
}
