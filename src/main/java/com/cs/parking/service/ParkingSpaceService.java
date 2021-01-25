package com.cs.parking.service;

import com.cs.parking.dto.ParkingSpaceCustomer;
import com.cs.parking.dto.ParkingSpaceOwner;
import com.cs.parking.dto.ParkingSpaceRent;
import com.cs.parking.pojo.ParkingSpace;

import java.util.List;

/**
 * @ClassName ParkingSpaceService
 * @Description TODO
 * @Author QQ163
 * @Date 2021/1/20 11:42
 **/
public interface ParkingSpaceService {
    /*
    * 添加车位
    * */
    void insert(ParkingSpace parkingSpace);
    /*
    * 查找用户车位
    * */
    List<ParkingSpaceOwner> selectParkingSpaceUser(Integer uid,boolean share);
    /*
    * 查找附件车位
    * */
    List<ParkingSpaceCustomer> selectParkingSpaceCustomer(double TLat,double BLat,double LLong,double RLong);
    /*
    * 删除车位
    * */
    void delete(Integer id);
    /*
     * 取消共享车位
     * */
    void unShare(Integer id);
    /*
     * 共享车位
     * */
    void share(Integer id);
    /*
    * 修改车位
    * */
    void update(ParkingSpace parkingSpace);
    /*
    * id查找车位
    * */
    ParkingSpace selectId(Integer pid);
    /*
    * id查找车位
    * */
    ParkingSpaceRent selectIdNotice(Integer pid);
}
