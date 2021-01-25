package com.cs.parking.manager;

import com.alibaba.fastjson.JSONObject;
import com.cs.parking.base.utils.CornTimeUtil;
import com.cs.parking.code.BaseCode;
import com.cs.parking.code.QuartzJobCode;
import com.cs.parking.exception.ErrorException;
import com.cs.parking.pojo.ScheduleJob;
import com.cs.parking.quzrtzJob.NoticeJob;
import com.cs.parking.service.ScheduleJobService;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Map;

/**
 * @ClassName ScedulerManager
 * @Description TODO
 * @Author QQ163
 * @Date 2021/1/23 14:56
 **/
@Component
public class SchedulerManager {

    @Autowired
    private Scheduler scheduler;

    public static SchedulerManager schedulerManager;
    @PostConstruct
    public void init(){
        schedulerManager = this;
    }
    @Autowired
    ScheduleJobService scheduleJobService;

    //清除任务
    public void clear() throws SchedulerException {
        scheduler.clear();
    }

    //添加任务
    public void add(String jobName,String jobGroup, Date cornDate, JobDataMap jobDataMap) throws SchedulerException {
        try {
            ScheduleJob scheduleJob = new ScheduleJob(jobName,jobGroup, JSONObject.toJSONString(jobDataMap),cornDate,false);
            schedulerManager.scheduleJobService.insert(scheduleJob);
            scheduler.start();
            JobDetail jobDetail = JobBuilder.newJob(NoticeJob.class)
                    .withIdentity(jobName, jobGroup)
                    .usingJobData(jobDataMap)
                    .build();
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(CornTimeUtil.getInstance().dateToCorn(cornDate));
            CronTrigger cronTrigger = TriggerBuilder.newTrigger()
                    .withIdentity(jobName, jobGroup)
                    .withSchedule(scheduleBuilder)
                    .build();
            scheduler.scheduleJob(jobDetail,cronTrigger);
        } catch (Exception e) {
            throw new ErrorException(BaseCode.System_Error,"定时任务出错！\n "+e.getMessage());
        }
    }

    //添加任务
    public void add(String jobName, String jobGroup, LocalDateTime localDateTime, JobDataMap jobDataMap) throws SchedulerException {
        try {
            Date cornDate = Date.from(localDateTime.atZone(ZoneId.of("Asia/Shanghai")).toInstant());
            ScheduleJob scheduleJob = new ScheduleJob(jobName,jobGroup, JSONObject.toJSONString(jobDataMap),cornDate,false);
            schedulerManager.scheduleJobService.insert(scheduleJob);
            scheduler.start();
            JobDetail jobDetail = JobBuilder.newJob(NoticeJob.class)
                    .withIdentity(jobName, jobGroup)
                    .usingJobData(jobDataMap)
                    .build();
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(CornTimeUtil.getInstance().dateToCorn(localDateTime));
            CronTrigger cronTrigger = TriggerBuilder.newTrigger()
                    .withIdentity(jobName, jobGroup)
                    .withSchedule(scheduleBuilder)
                    .build();
            scheduler.scheduleJob(jobDetail,cronTrigger);
        } catch (Exception e) {
            throw new ErrorException(BaseCode.System_Error,"定时任务出错！\n "+e.getMessage());
        }
    }

    //停止任务
    public boolean pause(String jobName,String jobGroup){
        JobKey jobKey = new JobKey(jobName,jobGroup);
        //暂停触发器
        try {
            scheduler.pauseJob(jobKey);
            return true;
        } catch (SchedulerException e) {
            return false;
        }
    }

    //移除任务
    public boolean remove(String jobName,String jobGroup){
        TriggerKey triggerKey = TriggerKey.triggerKey(jobName,jobGroup);
        try {
            //停止出发前
            scheduler.pauseTrigger(triggerKey);
            //移除触发器
            scheduler.unscheduleJob(triggerKey);
            //删除任务
            scheduler.deleteJob(JobKey.jobKey(jobName,jobGroup));
            return true;
        }catch (Exception e){
            return false;
        }
    }

    //恢复停止的任务
    public boolean resume(String jobName,String jobGroup){
        JobKey jobKey = new JobKey(jobName,jobGroup);
        try {
            scheduler.resumeJob(jobKey);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    //修改任务时间
    public boolean modify(String jobName,String jobGroup,String time){
        Date date = null;
        try {
            TriggerKey triggerKey = new TriggerKey(jobName,jobGroup);
            CronTrigger cronTrigger = (CronTrigger) scheduler.getTrigger(triggerKey);
            String oldTime = cronTrigger.getCronExpression();
            if (!oldTime.equalsIgnoreCase(time)){
                CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(time);
                CronTrigger trigger = TriggerBuilder.newTrigger()
                        .withIdentity(jobName,jobGroup)
                        .withSchedule(cronScheduleBuilder).build();
                date = scheduler.rescheduleJob(triggerKey,trigger);
            }
            return date != null;
        }catch (Exception e){
            return false;
        }
    }

}
