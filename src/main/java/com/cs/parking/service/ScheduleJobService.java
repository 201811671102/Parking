package com.cs.parking.service;

import com.cs.parking.pojo.ScheduleJob;

import java.util.List;

/**
 * @ClassName ScheduleJobService
 * @Description TODO
 * @Author QQ163
 * @Date 2021/1/23 23:00
 **/
public interface ScheduleJobService {
    /*
    * 增加任务
    * */
    void insert(ScheduleJob scheduleJob);
    /*
    * 查找任务
    * */
    List<ScheduleJob> selectNotComplete();
    /*
    * 任务完成
    * */
    void updateCompleted(String jobKey);
}
