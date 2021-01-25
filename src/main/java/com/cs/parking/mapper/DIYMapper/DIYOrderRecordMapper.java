package com.cs.parking.mapper.DIYMapper;

import com.cs.parking.dto.OrderRecordCustomerDTO;
import com.cs.parking.pojo.DIYpojo.OrderRecordOwner;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

/**
 * @ClassName DIYOrderRecordMapper
 * @Description TODO
 * @Author QQ163
 * @Date 2021/1/22 15:15
 **/
@Component
public interface DIYOrderRecordMapper {

    /*
    * 用户预约订单
    * */
    @Select("SELECT o.id AS orId,p.short_name AS shortName, CONCAT(p.province,p.city,p.county,p.town,p.address) AS address,\n" +
            "\t\t\to.car_number AS carNumber,o.car_owner_phone_number AS carOwnerPhoneNumber,\n" +
            "\t\t\to.pay_time AS payTime,o.effect_time AS effectTime,o.failure_time AS failureTime,\n" +
            "\t\t\to.payment_amount AS paymentAmount,o.order_state AS orderState,\n" +
            "\t\t\tp.longitude AS longitude,p.latitude AS latitude\n" +
            "\tFROM order_record as o \n" +
            "\tJOIN parking_space as p\n" +
            "\t\tON o.parking_space_id = p.id\n" +
            "\t\tWHERE o.order_state != -1 AND o.user_id = #{uid}\n" +
            "\t\tORDER BY o.order_state ASC")
    List<OrderRecordCustomerDTO> selectRecordUser(Integer uid);

    /*
    *车位出租者某日车位订单
    * */
    @Select("SELECT u.`name`,o.car_number AS carNumber,o.car_owner_phone_number AS carOwnerPhoneNumber,\n" +
            "\t\t\tCAST( SUBSTRING_INDEX( SUBSTRING_INDEX(o.appointment_time,';',mht.help_topic_id+1 ),';',-1) AS signed )  AS appointmentTime\n" +
            "\tFROM order_record AS o\n" +
            "\tJOIN parking_space AS p \n" +
            "\t\tON o.parking_space_id = p.id\n" +
            "  JOIN `user` as u\n" +
            "\t\tON  o.user_id = u.id\n" +
            "\tJOIN mysql.help_topic as mht\n" +
            "\t\tON mht.help_topic_id < ( LENGTH(o.appointment_time) - LENGTH( REPLACE(o.appointment_time,';','') ) + 1)\n" +
            "\t\tWHERE o.parking_space_id = #{pid} \n" +
            "\t\t\t\tAND UNIX_TIMESTAMP( DATE_FORMAT(o.effect_time,'%Y-%m-%d') ) = UNIX_TIMESTAMP( #{localDate} )\n" +
            "\t\tGROUP BY appointmentTime ASC;")
    List<OrderRecordOwner> selectRecordRent(Integer pid, LocalDate localDate);
}
