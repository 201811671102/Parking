package com.cs.parking.service;

import com.cs.parking.pojo.OrderRecord;

import java.util.Date;
import java.util.Map;

public interface OrderRecordService {
   /*
   * 添加订单
   * */
   void insert(OrderRecord orderRecord);
   /*
   * 取消订单
   * */
   void delete(Integer orId);
   /*
   * 获取某车位某日的所有订单,计算其可预约时间
   * */
   Map<Integer,String> searchAppointmentTime(Integer pid, Date date,Map<Integer, String> timeOfDayMap);
   /*
   * 修改订单状态
   * */
   void update(Integer orId,Integer state);
}
