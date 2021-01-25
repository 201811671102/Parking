package com.cs.parking.quzrtzJob;

import com.cs.parking.base.utils.CornTimeUtil;
import com.cs.parking.manager.SchedulerManager;
import com.cs.parking.pojo.ScheduleJob;
import com.cs.parking.service.NoticeMessageService;
import com.cs.parking.service.ScheduleJobService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.List;

/**
 * @ClassName SystemNoticeJob
 * @Description TODO
 * @Author QQ163
 * @Date 2021/1/25 23:45
 **/
public class SystemNoticeJob implements Job {
    public static SystemNoticeJob systemNoticeJob;
    @PostConstruct
    public void init(){
        systemNoticeJob = this;
    }

    @Autowired
    ScheduleJobService scheduleJobService;
    @Autowired
    NoticeMessageService noticeMessageService;
    @Autowired
    SchedulerManager schedulerManager;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        List<ScheduleJob> scheduleJobs = systemNoticeJob.scheduleJobService.selectNotComplete();
        if (scheduleJobs != null && scheduleJobs.size() > 0){
            scheduleJobs.forEach(scheduleJob -> {
                systemNoticeJob.schedulerManager.modify(scheduleJob.getJobKey(),scheduleJob.getJobGroup(), CornTimeUtil.getInstance().dateToCorn(new Date()));
            });
        }
    }
}
