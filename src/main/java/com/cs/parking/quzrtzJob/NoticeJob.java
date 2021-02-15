package com.cs.parking.quzrtzJob;

import com.alibaba.fastjson.JSONObject;
import com.cs.parking.base.utils.MessageUtil;
import com.cs.parking.code.Protocol;
import com.cs.parking.code.QuartzJobCode;
import com.cs.parking.dto.NoticeMessageDTO;
import com.cs.parking.manager.ConnManager;
import com.cs.parking.manager.SchedulerManager;
import com.cs.parking.pojo.NoticeMessage;
import com.cs.parking.service.NoticeMessageService;
import com.cs.parking.service.OrderRecordService;
import com.cs.parking.service.ScheduleJobService;
import lombok.SneakyThrows;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;

/**
 * @ClassName NoticeJob
 * @Description TODO
 * @Author QQ163
 * @Date 2021/1/23 14:59
 **/
@Component
public class NoticeJob implements Job {

    public static NoticeJob noticeJob;
    @PostConstruct
    public void init(){
        noticeJob = this;
    }
    @Autowired
    ScheduleJobService scheduleJobService;
    @Autowired
    NoticeMessageService noticeMessageService;
    @Autowired
    SchedulerManager schedulerManager;
    @Autowired
    OrderRecordService orderRecordService;

    @SneakyThrows
    @Override
    @Transactional
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        try {
            JobDataMap jobDataMap = jobExecutionContext.getJobDetail().getJobDataMap();
            JobDetail jobDetail = jobExecutionContext.getJobDetail();
            String jobKey = jobDetail.getKey().getName();
            String jobGroup = jobDetail.getKey().getGroup();
            QuartzJobCode quartzJobCode = (QuartzJobCode) jobDataMap.get("type");
            int uid = jobDataMap.getInt("uid");
            NoticeMessage noticeMessage = new NoticeMessage();
            noticeMessage.setUserId(uid);
            //是否在线
            if (ConnManager.getInstance().isAlive(uid)){
                noticeMessage.setState(true);
            }else{
                noticeMessage.setState(false);
            }
            Protocol protocol = null;
            NoticeMessageDTO noticeMessageDTO = new NoticeMessageDTO();
            switch (quartzJobCode) {
                case Order_Coming_Remind_First:
                    protocol = Protocol.Order_Coming_Remind_First_Notice;
                    protocol.setData((LocalDateTime) jobDataMap.get("effectTime"));
                    noticeMessageDTO.setValue(protocol.getValue());
                    noticeMessageDTO.setData(protocol.getData());
                    if(noticeMessage.getState()){
                        MessageUtil.unicast(uid, protocol);
                    }
                    jobDataMap.replace("type",QuartzJobCode.Order_Coming_Remind_Last);
                    noticeJob.schedulerManager.add(jobKey,jobGroup, ((LocalDateTime) jobDataMap.get("effectTime")).minusMinutes(15),jobDataMap);
                    break;
                case Order_Coming_Remind_Last:
                    protocol = Protocol.Order_Coming_Remind_Last_Notice;
                    protocol.setData((LocalDateTime) jobDataMap.get("effectTime"));
                    noticeMessageDTO.setValue(protocol.getValue());
                    noticeMessageDTO.setData(protocol.getData());
                    if(noticeMessage.getState()){
                        MessageUtil.unicast(uid, protocol);
                    }
                    jobDataMap.replace("type",QuartzJobCode.Order_OnGoing_Remind);
                    noticeJob.schedulerManager.add(jobKey,jobGroup, (LocalDateTime) jobDataMap.get("effectTime"),jobDataMap);
                    break;
                case Order_OnGoing_Remind:
                    protocol = Protocol.Order_OnGoing_Remind_Notice;
                    protocol.setData((LocalDateTime) jobDataMap.get("effectTime"));
                    noticeMessageDTO.setValue(protocol.getValue());
                    noticeMessageDTO.setData(protocol.getData());
                    if(noticeMessage.getState()){
                        MessageUtil.unicast(uid, protocol);
                    }
                    noticeJob.orderRecordService.update(jobDataMap.getInt("orId"),1);
                    jobDataMap.replace("type",QuartzJobCode.Order_Complete_Remind);
                    noticeJob.schedulerManager.add(jobKey,jobGroup, (((LocalDateTime) jobDataMap.get("effectTime")).plusHours(1)),jobDataMap);
                    break;
                case Order_Complete_Remind:
                    noticeJob.orderRecordService.update(jobDataMap.getInt("orId"),2);
                    protocol = Protocol.Order_Complete_Remind_Notice;
                    protocol.setData(((LocalDateTime) jobDataMap.get("effectTime")).plusHours(1));
                    noticeMessageDTO.setValue(protocol.getValue());
                    noticeMessageDTO.setData(protocol.getData());
                    if(noticeMessage.getState()){
                        MessageUtil.unicast(uid, protocol);
                    }
                    break;
                case Vip_Over_Remind:
                    protocol = Protocol.Vip_Over_Notice;
                    protocol.setData((LocalDateTime) jobDataMap.get("vipTime"));
                    noticeMessageDTO.setValue(protocol.getValue());
                    noticeMessageDTO.setData(protocol.getData());
                    if(noticeMessage.getState()){
                        MessageUtil.unicast(uid,protocol);
                    }
                    break;
                case Coupons_Over:
                    protocol = Protocol.Coupons_Over_Notice;
                    protocol.setData((LocalDateTime) jobDataMap.get("CouponsTime"));
                    noticeMessageDTO.setValue(protocol.getValue());
                    noticeMessageDTO.setData(protocol.getData());
                    if(noticeMessage.getState()){
                        MessageUtil.unicast(uid, protocol);
                    }
                    break;
            }
            noticeMessage.setMessage(JSONObject.toJSONString(noticeMessageDTO));
            noticeJob.noticeMessageService.insert(noticeMessage);
            noticeJob.scheduleJobService.updateCompleted(jobKey);
            noticeJob.schedulerManager.remove(jobKey,jobGroup);
        }catch (Exception e){
            throw new Exception("执行任务失败 \n "+e.getMessage());
        }
    }




}
