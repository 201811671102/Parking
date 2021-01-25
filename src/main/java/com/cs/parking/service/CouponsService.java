package com.cs.parking.service;

import com.cs.parking.dto.CouponsDTO;
import com.cs.parking.pojo.Coupons;

import java.util.List;

public interface CouponsService {
    /*
    * 新增优惠卷
    * */
    void insert(Coupons coupons);
    /*
    *修改优惠卷
    * */
    void update(Coupons coupons);
    /*
    * 撤销优惠卷
    * */
    void delete(Integer id);
    /*
     * 撤销优惠卷
     * */
    void adminDelete(Integer id);
    /*
    * 主键查找优惠卷
    * */
    Coupons selectById(Integer id);
    /*
    * 车位id查找优惠卷
    * */
    List<CouponsDTO> selectByParkingSpaceId(Integer parkingSpaceId,boolean overdue);
    /*
    * 优惠卷减少
    * */
    void reduce(Integer cid);
}
