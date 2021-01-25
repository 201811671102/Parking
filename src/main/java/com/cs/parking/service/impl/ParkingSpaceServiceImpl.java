package com.cs.parking.service.impl;

import com.cs.parking.base.utils.RedisUtil;
import com.cs.parking.code.BaseCode;
import com.cs.parking.dto.ParkingSpaceCustomer;
import com.cs.parking.dto.ParkingSpaceOwner;
import com.cs.parking.dto.ParkingSpaceRent;
import com.cs.parking.exception.ErrorException;
import com.cs.parking.exception.SystemException;
import com.cs.parking.mapper.ParkingSpaceMapper;
import com.cs.parking.pojo.ParkingSpace;
import com.cs.parking.pojo.ParkingSpaceExample;
import com.cs.parking.service.ParkingSpaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName ParkingSpaceServiceImpl
 * @Description TODO
 * @Author QQ163
 * @Date 2021/1/20 17:53
 **/
@Service
public class ParkingSpaceServiceImpl implements ParkingSpaceService {

    @Autowired
    ParkingSpaceMapper parkingSpaceMapper;
    @Autowired
    ParkingSpaceExample parkingSpaceExample;
    @Autowired
    RedisUtil redisUtil;

    @Override
    public void insert(ParkingSpace parkingSpace) {
        int i = parkingSpaceMapper.insertSelective(parkingSpace);
        if (i == -1){
            throw new ErrorException(BaseCode.System_Error,"新增共享车位失败");
        }
    }

    @Override
    public List<ParkingSpaceOwner> selectParkingSpaceUser(Integer uid, boolean share) {
        parkingSpaceExample.clear();
        parkingSpaceExample.createCriteria()
                .andUserIdEqualTo(uid)
                .andShareEqualTo(share)
                .andCancelEqualTo(false);
        List<ParkingSpace> parkingSpaces = parkingSpaceMapper.selectByExample(parkingSpaceExample);
        if (parkingSpaces == null || parkingSpaces.size() <= 0){
            throw new SystemException(BaseCode.Null);
        }
        List<ParkingSpaceOwner> parkingSpaceOwnerList = new ArrayList<>();
        parkingSpaces.stream()
                .forEach((x)->{
                    parkingSpaceOwnerList.add(new ParkingSpaceOwner(x));
                });
        return parkingSpaceOwnerList;
    }

    @Override
    public List<ParkingSpaceCustomer> selectParkingSpaceCustomer(double TLat, double BLat, double LLong, double RLong) {
        parkingSpaceExample.clear();
        LocalDateTime localDateTime = LocalDateTime.now();
        parkingSpaceExample.createCriteria()
                .andCancelEqualTo(false)
                .andShareEqualTo(true)
                .andStartTimeDayLessThanOrEqualTo(localDateTime.getHour())
                .andEndTimeDayGreaterThan(localDateTime.getHour())
                .andLatitudeLessThanOrEqualTo(TLat)
                .andLatitudeGreaterThanOrEqualTo(BLat)
                .andLongitudeLessThanOrEqualTo(RLong)
                .andLongitudeGreaterThanOrEqualTo(LLong);

        List<ParkingSpace> parkingSpaces = parkingSpaceMapper.selectByExample(parkingSpaceExample);
        if (parkingSpaces == null || parkingSpaces.size() <= 0){
            throw new SystemException(BaseCode.Null);
        }
        List<ParkingSpaceCustomer> parkingSpaceCustomerList = new ArrayList<>();
        parkingSpaces.stream()
                .forEach((x)->{
                    long watchedNumber;
                    if (redisUtil.hasKey(String.valueOf(x.getId()))){
                        watchedNumber = (long) redisUtil.get(String.valueOf(x.getId()));
                    }else{
                        watchedNumber = 1;
                    }
                    parkingSpaceCustomerList.add(new ParkingSpaceCustomer(x,watchedNumber));
                });
        return parkingSpaceCustomerList;
    }

    @Override
    public void delete(Integer id) {
        ParkingSpace parkingSpace = new ParkingSpace();
        parkingSpace.setId(id);
        parkingSpace.setCancel(true);
        int i = parkingSpaceMapper.updateByPrimaryKeySelective(parkingSpace);
        if (i == -1){
            throw new ErrorException(BaseCode.System_Error,"删除共享车位失败");
        }
    }

    @Override
    public void unShare(Integer id) {
        ParkingSpace parkingSpace = new ParkingSpace();
        parkingSpace.setId(id);
        parkingSpace.setShare(false);
        int i = parkingSpaceMapper.updateByPrimaryKeySelective(parkingSpace);
        if (i == -1){
            throw new ErrorException(BaseCode.System_Error,"车位取消共享失败");
        }
    }

    @Override
    public void share(Integer id) {
        ParkingSpace parkingSpace = new ParkingSpace();
        parkingSpace.setId(id);
        parkingSpace.setShare(true);
        int i = parkingSpaceMapper.updateByPrimaryKeySelective(parkingSpace);
        if (i == -1){
            throw new ErrorException(BaseCode.System_Error,"车位共享失败");
        }
    }

    @Override
    public void update(ParkingSpace parkingSpace) {
        int i = parkingSpaceMapper.updateByPrimaryKeySelective(parkingSpace);
        if (i == -1){
            throw new ErrorException(BaseCode.System_Error,"修改共享车位失败");
        }
    }

    @Override
    public ParkingSpace selectId(Integer pid) {
        return parkingSpaceMapper.selectByPrimaryKey(pid);
    }

    @Override
    public ParkingSpaceRent selectIdNotice(Integer pid) {
        ParkingSpace parkingSpace = parkingSpaceMapper.selectByPrimaryKey(pid);
        if (parkingSpace == null){
            throw new SystemException(BaseCode.Null);
        }
        return new ParkingSpaceRent(parkingSpace);
    }
}
