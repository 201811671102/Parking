package com.cs.parking.service.impl;

import com.cs.parking.code.BaseCode;
import com.cs.parking.exception.ErrorException;
import com.cs.parking.mapper.ScheduleJobMapper;
import com.cs.parking.pojo.ScheduleJob;
import com.cs.parking.pojo.ScheduleJobExample;
import com.cs.parking.service.ScheduleJobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @ClassName ScheduleJobServiceImpl
 * @Description TODO
 * @Author QQ163
 * @Date 2021/1/23 23:02
 **/
@Service
public class ScheduleJobServiceImpl implements ScheduleJobService {

    @Autowired
    ScheduleJobMapper scheduleJobMapper;
    @Autowired
    ScheduleJobExample scheduleJobExample;

    @Override
    public void insert(ScheduleJob scheduleJob) {
        int i = scheduleJobMapper.insertSelective(scheduleJob);
        if (i == -1){
            throw new ErrorException(BaseCode.System_Error,"添加任务出错");
        }
    }

    @Override
    public List<ScheduleJob> selectNotComplete() {
        scheduleJobExample.clear();
        scheduleJobExample.createCriteria()
                .andStateEqualTo(false)
                .andJobTimeLessThan(new Date());
        List<ScheduleJob> scheduleJobs = scheduleJobMapper.selectByExample(scheduleJobExample);
        return scheduleJobs;
    }

    @Override
    public void updateCompleted(String jobKey) {
        scheduleJobExample.clear();
        scheduleJobExample.createCriteria()
                .andJobKeyEqualTo(jobKey);
        ScheduleJob scheduleJob = new ScheduleJob();
        scheduleJob.setState(true);
        int i = scheduleJobMapper.updateByExampleSelective(scheduleJob,scheduleJobExample);
        if (i == -1){
            throw new ErrorException(BaseCode.System_Error,"修改任务完成情况出错");
        }
    }
}
