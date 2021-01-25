package com.cs.parking.service.DIYService.impl;

import com.cs.parking.code.BaseCode;
import com.cs.parking.code.TimeOfDay;
import com.cs.parking.dto.OrderRecordCustomerDTO;
import com.cs.parking.dto.OrderRecordOwnerDTO;
import com.cs.parking.exception.SystemException;
import com.cs.parking.mapper.DIYMapper.DIYOrderRecordMapper;
import com.cs.parking.pojo.DIYpojo.OrderRecordOwner;
import com.cs.parking.service.DIYService.DIYOrderRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

/**
 * @ClassName DIYOrderRecordServiceImpl
 * @Description TODO
 * @Author QQ163
 * @Date 2021/1/22 15:33
 **/
@Service
public class DIYOrderRecordServiceImpl implements DIYOrderRecordService {

    @Autowired
    DIYOrderRecordMapper diyOrderRecordMapper;

    @Override
    public List<OrderRecordCustomerDTO> selectOrderRecordUser(Integer uid) {
        List<OrderRecordCustomerDTO> orderRecordCustomerDTOList = diyOrderRecordMapper.selectRecordUser(uid);
        if (orderRecordCustomerDTOList == null || orderRecordCustomerDTOList.size() <= 0){
            throw new SystemException(BaseCode.Null);
        }
        return orderRecordCustomerDTOList;
    }

    @Override
    public List<OrderRecordOwnerDTO> searchOrderRecordOwner(Integer pid, Date date) {
        LocalDate localDate = date.toInstant().atZone(ZoneId.of("Asia/Shanghai")).toLocalDate();
        List<OrderRecordOwner> orderRecordOwners = diyOrderRecordMapper.selectRecordRent(pid, localDate);
        if (orderRecordOwners == null || orderRecordOwners.size() <=0){
            throw new SystemException(BaseCode.Null);
        }
        List<OrderRecordOwnerDTO> orderRecordOwnerDTOList = new ArrayList<>();
        orderRecordOwners.stream()
                .forEach(orderRecordOwner -> {
                    orderRecordOwnerDTOList.add(new OrderRecordOwnerDTO(orderRecordOwner));
                });
        return orderRecordOwnerDTOList;
    }
}
