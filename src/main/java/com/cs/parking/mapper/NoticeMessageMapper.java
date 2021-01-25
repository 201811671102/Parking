package com.cs.parking.mapper;

import com.cs.parking.pojo.NoticeMessage;
import com.cs.parking.pojo.NoticeMessageExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

@Component
public interface NoticeMessageMapper {
    long countByExample(NoticeMessageExample example);

    int deleteByExample(NoticeMessageExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(NoticeMessage record);

    int insertSelective(NoticeMessage record);

    List<NoticeMessage> selectByExample(NoticeMessageExample example);

    NoticeMessage selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") NoticeMessage record, @Param("example") NoticeMessageExample example);

    int updateByExample(@Param("record") NoticeMessage record, @Param("example") NoticeMessageExample example);

    int updateByPrimaryKeySelective(NoticeMessage record);

    int updateByPrimaryKey(NoticeMessage record);
}