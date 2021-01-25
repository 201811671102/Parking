package com.cs.parking.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.cs.parking.code.BaseCode;
import com.cs.parking.code.Protocol;
import com.cs.parking.controller.NoticeMessageDTO;
import com.cs.parking.exception.ErrorException;
import com.cs.parking.exception.SystemException;
import com.cs.parking.mapper.NoticeMessageMapper;
import com.cs.parking.pojo.NoticeMessage;
import com.cs.parking.pojo.NoticeMessageExample;
import com.cs.parking.service.NoticeMessageService;
import org.quartz.JobDataMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @ClassName NoticeMessageServiceImpl
 * @Description TODO
 * @Author QQ163
 * @Date 2021/1/24 0:23
 **/
@Service
public class NoticeMessageServiceImpl implements NoticeMessageService {

    @Autowired
    NoticeMessageMapper noticeMessageMapper;
    @Autowired
    NoticeMessageExample noticeMessageExample;

    @Override
    public void insert(NoticeMessage noticeMessage) {
        int i = noticeMessageMapper.insertSelective(noticeMessage);
        if (i == -1){
            throw new ErrorException(BaseCode.System_Error,"添加信息记录出错");
        }
    }

    @Override
    public List<NoticeMessageDTO> selectUser(Integer uid) {
        noticeMessageExample.clear();
        noticeMessageExample.createCriteria()
                .andUserIdEqualTo(uid)
                .andStateEqualTo(false);
        List<NoticeMessage> noticeMessages = noticeMessageMapper.selectByExample(noticeMessageExample);
        if (noticeMessages == null || noticeMessages.size() <= 0 ){
            throw new SystemException(BaseCode.Null);
        }
        List<NoticeMessageDTO> noticeMessageDTOArrayList = new ArrayList<>();
        noticeMessages.forEach(noticeMessage -> {
            NoticeMessageDTO noticeMessageDTO = JSONObject.parseObject(noticeMessage.getMessage(), NoticeMessageDTO.class);
            noticeMessageDTO.setId(noticeMessage.getId());
            noticeMessageDTOArrayList.add(noticeMessageDTO);
                });
        return noticeMessageDTOArrayList;
    }

    @Override
    public void update(Integer nid) {
        NoticeMessage noticeMessage = new NoticeMessage();
        noticeMessage.setId(nid);
        noticeMessage.setState(true);
        int i = noticeMessageMapper.updateByPrimaryKeySelective(noticeMessage);
        if (i == -1){
            throw new ErrorException(BaseCode.System_Error,"修改通知信息状态出错");
        }
    }
}
