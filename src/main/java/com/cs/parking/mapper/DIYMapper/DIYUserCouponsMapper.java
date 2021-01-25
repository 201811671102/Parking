package com.cs.parking.mapper.DIYMapper;

import com.cs.parking.dto.UserCouponsDTO;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface DIYUserCouponsMapper {

    //依据用户id查询其拥有的优惠卷(包含过期，包含使用过)
    @Select("select c.id as cid,c.name,c.price,c.start_time as startTime,c.end_time as endTime,c.parking_space_id as parkingSpaceId,c.cancel  from coupons  as c LEFT JOIN user_coupons as uc ON c.id = uc.coupons_id  where uc.user_id = #{uid}")
    List<UserCouponsDTO> selectUserCouponsByUidOverdueUsed(Integer uid);

    //依据用户id查询其拥有的优惠卷(包含过期，不包含使用过)
    @Select("select c.id as cid,c.name,c.price,c.start_time as startTime,c.end_time as endTime,c.parking_space_id as parkingSpaceId,c.cancel  from coupons  as c LEFT JOIN user_coupons as uc ON c.id = uc.coupons_id  where uc.user_id = #{uid} AND uc.used = false")
    List<UserCouponsDTO> selectUserCouponsByUidUsed(Integer uid);

    //依据用户id查询其拥有的优惠卷(不包含过期，包含使用过)
    @Select("select c.id as cid,c.name,c.price,c.start_time as startTime,c.end_time as endTime,c.parking_space_id as parkingSpaceId,c.cancel  from coupons  as c LEFT JOIN user_coupons as uc ON c.id = uc.coupons_id  where uc.user_id = #{uid} AND UNIX_TIMESTAMP( c.end_time ) > UNIX_TIMESTAMP(NOW()) ")
    List<UserCouponsDTO> selectUserCouponsByUidOverdue(Integer uid);

    //依据用户id查询其拥有的优惠卷(不包含过期，不包含使用过)
    @Select("select c.id as cid,c.name,c.price,,c.start_time as startTime,c.end_time as endTime,c.parking_space_id as parkingSpaceId,c.cancel  from coupons  as c LEFT JOIN user_coupons as uc ON c.id = uc.coupons_id  where uc.user_id = #{uid} AND UNIX_TIMESTAMP( c.end_time ) > UNIX_TIMESTAMP(NOW()) AND uc.used = FALSE")
    List<UserCouponsDTO> selectUserCouponsByUid(Integer uid);
}
