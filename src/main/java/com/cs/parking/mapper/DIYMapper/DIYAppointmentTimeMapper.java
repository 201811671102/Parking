package com.cs.parking.mapper.DIYMapper;

import com.cs.parking.pojo.DIYpojo.Appointment;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Component
public interface DIYAppointmentTimeMapper {
    /*
    * 获取车位今日往后所有的预约订单
    * */
    @Select("SELECT  GROUP_CONCAT(o.appointment_time SEPARATOR ';') as appointmentTime,DATE_FORMAT(o.effect_time,'%Y-%m-%d') as appointmentDate\n" +
            "\tfrom order_record as o LEFT JOIN parking_space as p \n" +
            "\t\ton o.parking_space_id = p.id \n" +
            "\t\t\tWHERE o.parking_space_id = #{pid} \n" +
            "\t\t\t\tand UNIX_TIMESTAMP( o.effect_time ) > UNIX_TIMESTAMP( DATE_SUB(CURDATE(),INTERVAL -(p.start_time_day+1) HOUR) )\n" +
            "\t\t\tGROUP BY appointmentDate\n" +
            "\t\t\tORDER BY appointmentDate ASC")
    List<Appointment> searchAppointmentTimeOld(Integer pid);

    /*
    * 获取车位某日的所有订单
    * */
    @Select("SELECT  GROUP_CONCAT(o.appointment_time SEPARATOR ';') as appointmentTime\n" +
            "\tfrom order_record as o LEFT JOIN parking_space as p \n" +
            "\t\ton o.parking_space_id = p.id \n" +
            "\t\t\tWHERE o.parking_space_id = #{pid} \n" +
            "\t\t\t\tand UNIX_TIMESTAMP( DATE_FORMAT(o.effect_time,'%Y-%m-%d') ) = UNIX_TIMESTAMP( #{localDate} )\n" +
            "\t\t\tGROUP BY DATE_FORMAT(o.effect_time,'%Y-%m-%d')")
    String searchAppointmentTime(Integer pid, LocalDate localDate);
}
