package com.cs.parking.mapper;

import com.cs.parking.pojo.UserCoupons;
import com.cs.parking.pojo.UserCouponsExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

@Component
public interface UserCouponsMapper {
    long countByExample(UserCouponsExample example);

    int deleteByExample(UserCouponsExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(UserCoupons record);

    int insertSelective(UserCoupons record);

    List<UserCoupons> selectByExample(UserCouponsExample example);

    UserCoupons selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") UserCoupons record, @Param("example") UserCouponsExample example);

    int updateByExample(@Param("record") UserCoupons record, @Param("example") UserCouponsExample example);

    int updateByPrimaryKeySelective(UserCoupons record);

    int updateByPrimaryKey(UserCoupons record);
}