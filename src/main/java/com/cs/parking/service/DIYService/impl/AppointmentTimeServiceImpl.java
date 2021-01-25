package com.cs.parking.service.DIYService.impl;

import com.cs.parking.code.BaseCode;
import com.cs.parking.code.TimeOfDay;
import com.cs.parking.exception.ErrorException;
import com.cs.parking.mapper.DIYMapper.DIYAppointmentTimeMapper;
import com.cs.parking.mapper.ParkingSpaceMapper;
import com.cs.parking.pojo.DIYpojo.Appointment;
import com.cs.parking.pojo.ParkingSpace;
import com.cs.parking.service.DIYService.AppointmentTimeService;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.*;

/**
 * @ClassName AppointmentTimeServiceImpl
 * @Description TODO
 * @Author QQ163
 * @Date 2021/1/21 1:44
 **/
@Service
public class AppointmentTimeServiceImpl implements AppointmentTimeService {
    @Autowired
    DIYAppointmentTimeMapper diyAppointmentTimeMapper;
    @Autowired
    ParkingSpaceMapper parkingSpaceMapper;

    @Override
    public Map<LocalDate, Map<Integer, String>> searchAppointmentTime(Integer pid) {
        ParkingSpace parkingSpace = parkingSpaceMapper.selectByPrimaryKey(pid);
        if (parkingSpace == null){
            throw new ErrorException(BaseCode.Null,"没有id为："+pid+" 的车位信息");
        }
        List<Appointment> appointmentList = diyAppointmentTimeMapper.searchAppointmentTimeOld(pid);
        Map<LocalDate,Map<Integer,String>> appointmentMap = new HashMap<>();
        LocalDate startAppointmentDate = new Date().toInstant().atZone(ZoneId.of("Asia/Shanghai")).toLocalDate();
        if (appointmentList != null && appointmentList.size()>0){
            for (Appointment appointment : appointmentList){
                Map<Integer, String> timeOfDayMap = TimeOfDay.getAppointment(parkingSpace.getStartTimeDay(), parkingSpace.getEndTimeDay());
                Arrays.stream(appointment.getAppointmentTime().split(";"))
                        .forEach((x)->{
                            int i = Integer.parseInt(x);
                            timeOfDayMap.remove(i);
                        });
                appointmentMap.put(appointment.getAppointmentDate(),timeOfDayMap);
            }
        }
        LocalDate endAppointmentDate = parkingSpace.getEndTimeYear().toInstant().atZone(ZoneId.of("Asia/Shanghai")).toLocalDate();
        long until = startAppointmentDate.until(endAppointmentDate, ChronoUnit.DAYS);
        Map<Integer, String> timeOfDayMap = TimeOfDay.getAppointment(parkingSpace.getStartTimeDay(), parkingSpace.getEndTimeDay());
        LocalDate nextLocalDate = startAppointmentDate;
        for (long i=1;i<=until+1;i++){
            if (!appointmentMap.containsKey(nextLocalDate)){
                appointmentMap.put(nextLocalDate,timeOfDayMap);
            }
            nextLocalDate = startAppointmentDate.plusDays(i);
        }
        Map<LocalDate,Map<Integer,String>> appointmentMapResult = Maps.newLinkedHashMap();
        appointmentMap.entrySet().stream().sorted(Map.Entry.<LocalDate, Map<Integer, String>>comparingByKey())
                .forEachOrdered(e -> appointmentMapResult.put(e.getKey(), e.getValue()));
        return appointmentMapResult;
    }

    @Override
    public Map<Integer, String> searchAppointmentTime(Integer pid, Date date) {
        ParkingSpace parkingSpace = parkingSpaceMapper.selectByPrimaryKey(pid);
        if (parkingSpace == null){
            throw new ErrorException(BaseCode.Null,"没有id为："+pid+" 的车位信息");
        }
        LocalDate localDate = date.toInstant().atZone(ZoneId.of("Asia/Shanghai")).toLocalDate();
        LocalDate endTime = parkingSpace.getEndTimeYear().toInstant().atZone(ZoneId.of("Asia/Shanghai")).toLocalDate();
        LocalDate startTime = parkingSpace.getStartTimeYear().toInstant().atZone(ZoneId.of("Asia/Shanghai")).toLocalDate();
        if (localDate.isAfter(endTime)){
            throw new ErrorException(BaseCode.FailOperation,"预约时间不能在车位共享截至日期后");
        }
        if (localDate.isBefore(startTime)){
            throw new ErrorException(BaseCode.FailOperation,"预约时间不能在车位共享开始日期前");
        }
        String appointmentTime = diyAppointmentTimeMapper.searchAppointmentTime(pid,localDate);
        Map<Integer, String> timeOfDayMap = TimeOfDay.getAppointment(parkingSpace.getStartTimeDay(), parkingSpace.getEndTimeDay());
        if (StringUtils.isNotBlank(appointmentTime)){
            Arrays.stream(appointmentTime.split(";"))
                    .forEach((x)->{
                        int i = Integer.parseInt(x);
                        timeOfDayMap.remove(i);
                    });
        }
        return timeOfDayMap;
    }
}
