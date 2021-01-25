package com.cs.parking.service;

import com.cs.parking.code.Protocol;
import com.cs.parking.controller.NoticeMessageDTO;
import com.cs.parking.pojo.NoticeMessage;
import org.quartz.JobDataMap;

import java.util.List;

public interface NoticeMessageService {
    /*
    * 添加信息记录
    * */
    void insert(NoticeMessage noticeMessage);
    /*
    * 查找用户信息
    * */
    List<NoticeMessageDTO> selectUser(Integer uid);
    /*
    * 修改信息状态
    * */
    void update(Integer nid);
}
