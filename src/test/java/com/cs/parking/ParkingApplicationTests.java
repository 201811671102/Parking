package com.cs.parking;


import com.cs.parking.base.utils.CornTimeUtil;
import com.cs.parking.code.QuartzJobCode;
import com.cs.parking.dto.OrderRecordOwnerDTO;
import com.cs.parking.manager.SchedulerManager;
import com.cs.parking.mapper.DIYMapper.DIYAppointmentTimeMapper;
import com.cs.parking.service.DIYService.AppointmentTimeService;
import com.cs.parking.service.DIYService.DIYOrderRecordService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.quartz.JobDataMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;


@SpringBootTest(classes = {ParkingApplication.class},webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringJUnit4ClassRunner.class)
class ParkingApplicationTests {
    @Autowired
    SchedulerManager schedulerManager;



    @Test
    void contextLoads() {
        try {

        }catch (Exception e){
            e.printStackTrace();
        }

    }

}
