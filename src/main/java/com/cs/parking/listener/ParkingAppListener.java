package com.cs.parking.listener;

import com.alibaba.fastjson.JSONObject;
import com.cs.parking.base.utils.CornTimeUtil;
import com.cs.parking.code.QuartzJobCode;
import com.cs.parking.manager.SchedulerManager;
import com.cs.parking.pojo.ScheduleJob;
import com.cs.parking.quzrtzJob.NoticeJob;
import com.cs.parking.service.ScheduleJobService;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.quartz.JobDataMap;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.List;

/**
 * @ClassName ParkingAppListener
 * @Description TODO
 * @Author QQ163
 * @Date 2021/1/24 18:29
 **/
@Component
@Log4j2
public class ParkingAppListener implements ApplicationListener<ContextRefreshedEvent> {

    public static ParkingAppListener parkingAppListener;
    @PostConstruct
    public void init(){
        parkingAppListener = this;
    }

    @Autowired
    SchedulerManager schedulerManager;

    @SneakyThrows
    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        if (contextRefreshedEvent.getApplicationContext().getParent() == null){
            try {
                parkingAppListener.schedulerManager.resumeJob();
            }catch (Exception e){
                log.error(e.getMessage());
            }
        }
    }
}
