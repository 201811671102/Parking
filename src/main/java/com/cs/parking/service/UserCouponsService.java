package com.cs.parking.service;

import com.cs.parking.dto.UserCouponsDTO;

import java.util.List;

public interface UserCouponsService {
    /*
    * 用户新增优惠卷
    * */
    void insert(Integer uid,Integer cid);
    /*
    * 用户使用优惠卷
    * */
    void delete(Integer ucId);
    /*
    * 用户查询优惠卷
    * */
    List<UserCouponsDTO> selectAll(Integer uid, boolean overdue, boolean used);
}
