package com.cs.parking.service.impl;

import com.cs.parking.code.BaseCode;
import com.cs.parking.code.OrderRecordExceptionCode;
import com.cs.parking.exception.ErrorException;
import com.cs.parking.exception.SystemException;
import com.cs.parking.mapper.OrderRecordMapper;
import com.cs.parking.pojo.OrderRecord;
import com.cs.parking.pojo.OrderRecordExample;
import com.cs.parking.service.OrderRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @ClassName OrderRecordServiceImpl
 * @Description TODO
 * @Author QQ163
 * @Date 2021/1/21 23:40
 **/
@Component
public class OrderRecordServiceImpl implements OrderRecordService {

    @Autowired
    OrderRecordMapper orderRecordMapper;
    @Autowired
    OrderRecordExample orderRecordExample;

    @Override
    public void insert(OrderRecord orderRecord) {
        int i = orderRecordMapper.insertSelective(orderRecord);
        if (i == -1){
            throw new ErrorException(BaseCode.System_Error,"添加订单失败");
        }
    }

    @Override
    public void delete(Integer orId) {
        OrderRecord orderRecord = orderRecordMapper.selectByPrimaryKey(orId);
        if (orderRecord == null){
            throw new ErrorException(BaseCode.Null,"没有id为："+orId+" 的订单");
        }
        if (orderRecord.getEffectTime().before(new Date())){
            throw new SystemException(OrderRecordExceptionCode.Order_Cancel_Overdue);
        }
        orderRecord.setId(orId);
        orderRecord.setOrderState(-1);
        int i = orderRecordMapper.updateByPrimaryKeySelective(orderRecord);
        if (i == -1){
            throw new ErrorException(BaseCode.System_Error,"取消订单失败");
        }
    }

    @Override
    public Map<Integer, String> searchAppointmentTime(Integer pid, Date date,Map<Integer, String> timeOfDayMap) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String format = simpleDateFormat.format(date);
        try {
            date = simpleDateFormat.parse(format);
        } catch (ParseException e) {
            throw new SystemException(BaseCode.System_Error);
        }

        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(calendar.DATE,1);
        Date lastDate = calendar.getTime();
        orderRecordExample.clear();
        orderRecordExample.createCriteria()
                .andParkingSpaceIdEqualTo(pid)
                .andEffectTimeGreaterThanOrEqualTo(date)
                .andEffectTimeLessThan(lastDate);
        List<OrderRecord> orderRecords = orderRecordMapper.selectByExample(orderRecordExample);
        if (orderRecords != null && orderRecords.size() >0){
            StringBuilder stringBuilder = new StringBuilder();
            for (OrderRecord orderRecord : orderRecords){
                stringBuilder.append(orderRecord.getAppointmentTime());
                stringBuilder.append(";");
            }
            String appointmentTime = stringBuilder.toString();
            Arrays.stream(appointmentTime.split(";"))
                    .forEach((x)->{
                        int i = Integer.parseInt(x);
                        timeOfDayMap.remove(i);
                    });
        }
        return timeOfDayMap;
    }

}
