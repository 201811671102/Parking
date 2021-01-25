package com.cs.parking.service.DIYService;

import com.cs.parking.dto.OrderRecordCustomerDTO;
import com.cs.parking.dto.OrderRecordOwnerDTO;

import java.util.Date;
import java.util.List;

/**
 * @ClassName DIYOrderRecordService
 * @Description TODO
 * @Author QQ163
 * @Date 2021/1/22 15:32
 **/
public interface DIYOrderRecordService {
    /*
     * 查询用户订单
     * */
    List<OrderRecordCustomerDTO> selectOrderRecordUser(Integer uid);
    /*
     * 获取某车位某日的订单记录
     * */
    List<OrderRecordOwnerDTO> searchOrderRecordOwner(Integer pid, Date date);
}
