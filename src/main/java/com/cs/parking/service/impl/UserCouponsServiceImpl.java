package com.cs.parking.service.impl;

import com.cs.parking.code.BaseCode;
import com.cs.parking.dto.UserCouponsDTO;
import com.cs.parking.exception.ErrorException;
import com.cs.parking.mapper.DIYMapper.DIYUserCouponsMapper;
import com.cs.parking.mapper.UserCouponsMapper;
import com.cs.parking.pojo.UserCoupons;
import com.cs.parking.pojo.UserCouponsExample;
import com.cs.parking.service.UserCouponsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @ClassName UserCouponsServiceImpl
 * @Description TODO
 * @Author QQ163
 * @Date 2021/1/18 21:37
 **/
@Service
public class UserCouponsServiceImpl implements UserCouponsService {

    @Autowired
    UserCouponsMapper userCouponsMapper;
    @Autowired
    UserCouponsExample userCouponsExample;

    @Autowired
    DIYUserCouponsMapper diyUserCouponsMapper;


    @Override
    public void insert(Integer uid, Integer cid) {
        UserCoupons userCoupons = new UserCoupons();
        userCoupons.setUserId(uid);
        userCoupons.setCouponsId(cid);
        userCoupons.setUsed(false);
        int i = userCouponsMapper.insertSelective(userCoupons);
        if (i == -1){
            throw new ErrorException(BaseCode.System_Error,"用户添加优惠卷出错！");
        }
    }

    @Override
    public void delete(Integer ucId) {
        userCouponsExample.clear();
        userCouponsExample.createCriteria()
                .andIdEqualTo(ucId)
                .andUsedEqualTo(false);
        List<UserCoupons> userCouponsList = userCouponsMapper.selectByExample(userCouponsExample);
        if (userCouponsList == null || userCouponsList.isEmpty()){
            throw new ErrorException(BaseCode.Null,"查无数据：优惠卷用户关联表id = "+ucId);
        }
        UserCoupons userCoupons = userCouponsList.get(0);
        userCoupons.setUsed(true);
        int i = userCouponsMapper.updateByPrimaryKeySelective(userCoupons);
        if (i == -1){
            throw new ErrorException(BaseCode.System_Error,"使用优惠卷出错！");
        }
    }

    @Override
    public List<UserCouponsDTO> selectAll(Integer uid,boolean overdue,boolean used) {
        List<UserCouponsDTO> userCouponsDTOS = null;
        if (overdue == true && used == true){
            userCouponsDTOS = diyUserCouponsMapper.selectUserCouponsByUidOverdueUsed(uid);
        }else if (overdue == true && used == false){
            userCouponsDTOS = diyUserCouponsMapper.selectUserCouponsByUidOverdue(uid);
        }else if (overdue == false && used == true){
            userCouponsDTOS = diyUserCouponsMapper.selectUserCouponsByUidUsed(uid);
        }else{
            userCouponsDTOS = diyUserCouponsMapper.selectUserCouponsByUid(uid);
        }
        if (userCouponsDTOS == null){
            throw new ErrorException(BaseCode.Null,"查询用户所拥有：过期："+overdue+" 使用过："+used+" 优惠卷出错");
        }
        try {
            Map<String, List<UserCouponsDTO>> collect = userCouponsDTOS.stream()
                    .collect(Collectors.groupingBy(UserCouponsDTO::getName));
            List<UserCouponsDTO> result = new ArrayList<>();
            for (String key :collect.keySet()){
                List<UserCouponsDTO> sort = collect.get(key).stream()
                        .sorted((a, b) -> {
                            return a.getPrice().compareTo(b.getPrice());
                        }).collect(Collectors.toList());
                result.addAll(sort);
            }
            return result;
        }catch (Exception e){
            throw new ErrorException(BaseCode.System_Error,"据用户id查询优惠卷分组排序出错!"+'\n'+e.getMessage());
        }
    }
}
