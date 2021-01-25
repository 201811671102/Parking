package com.cs.parking.service.DIYService;

import com.cs.parking.code.TimeOfDay;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Map;

public interface AppointmentTimeService {
    /*
    * 根据车位id查询车位可预约时间
    * */
    Map<LocalDate,Map<Integer,String>> searchAppointmentTime(Integer pid);

    /*
     * 根据车位id,时间日期查询车位可预约时间
     * */
    Map<Integer,String> searchAppointmentTime(Integer pid,Date date);
}
