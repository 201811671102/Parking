package com.cs.parking.service.impl;

import com.cs.parking.code.BaseCode;
import com.cs.parking.code.CouponsExceptionCode;
import com.cs.parking.dto.CouponsDTO;
import com.cs.parking.exception.ErrorException;
import com.cs.parking.exception.SystemException;
import com.cs.parking.mapper.CouponsMapper;
import com.cs.parking.pojo.Coupons;
import com.cs.parking.pojo.CouponsExample;
import com.cs.parking.service.CouponsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @ClassName CouponsServiceImpl
 * @Description TODO
 * @Author QQ163
 * @Date 2021/1/18 15:33
 **/
@Service
public class CouponsServiceImpl implements CouponsService {

    @Autowired
    CouponsMapper couponsMapper;
    @Autowired
    CouponsExample couponsExample;

    @Override
    public void insert(Coupons coupons) {
        int i = couponsMapper.insertSelective(coupons);
        if (i == -1){
            throw new ErrorException(BaseCode.System_Error,"新增优惠卷出错！");
        }
    }

    @Override
    public void update(Coupons coupons) {
        int i = couponsMapper.updateByPrimaryKeySelective(coupons);
        if (i == -1){
            throw new ErrorException(BaseCode.System_Error,"修改优惠卷出错！");
        }
    }

    @Override
    public void delete(Integer id) {
        Coupons coupons = couponsMapper.selectByPrimaryKey(id);
        if (coupons.getStartTime().before(new Date())){
            throw new ErrorException(BaseCode.FailOperation,"优惠卷已经起效");
        }
        coupons.setCancel(true);
        int i = couponsMapper.updateByPrimaryKeySelective(coupons);
        if (i == -1){
            throw new ErrorException(BaseCode.System_Error,"修改优惠卷出错！");
        }
    }

    @Override
    public void adminDelete(Integer id) {
        Coupons coupons = new Coupons();
        coupons.setId(id);
        coupons.setCancel(true);
        int i = couponsMapper.updateByPrimaryKeySelective(coupons);
        if (i == -1){
            throw new ErrorException(BaseCode.System_Error,"修改优惠卷出错！");
        }
    }

    @Override
    public Coupons selectById(Integer id) {
        couponsExample.clear();
        couponsExample.createCriteria()
                .andCancelEqualTo(false)
                .andIdEqualTo(id);
        List<Coupons> couponsList = couponsMapper.selectByExample(couponsExample);
        if (couponsList == null || couponsList.isEmpty()){
            throw new SystemException(BaseCode.Null);
        }
        return couponsList.get(0);
    }

    @Override
    public List<CouponsDTO> selectByParkingSpaceId(Integer parkingSpaceId,boolean overdue) {
        couponsExample.clear();
        CouponsExample.Criteria criteria = couponsExample.createCriteria()
                .andCancelEqualTo(false)
                .andParkingSpaceIdEqualTo(parkingSpaceId)
                .andEndTimeGreaterThan(new Date());
        if (!overdue){
            criteria.andEndTimeLessThan(new Date());
        }
        List<Coupons> coupons = couponsMapper.selectByExample(couponsExample);
        if (coupons == null || coupons.isEmpty()){
            throw new ErrorException(BaseCode.Null,"车位id为: "+parkingSpaceId+"没有优惠卷");
        }
        try {
            Map<String, List<Coupons>> collect = coupons.stream()
                    .collect(Collectors.groupingBy(Coupons::getName));
            List<CouponsDTO> result = new ArrayList<>();
            for (String key :collect.keySet()){
                List<Coupons> sort = collect.get(key).stream()
                        .sorted((a, b) -> {
                            return a.getPrice().compareTo(b.getPrice());
                        }).collect(Collectors.toList());
                for (Coupons item : sort){
                    result.add(new CouponsDTO(item));
                }
            }
            return result;
        }catch (Exception e){
            throw new ErrorException(BaseCode.System_Error,"据车位查询优惠卷分组排序出错!"+'\n'+e.getMessage());
        }
    }

    @Override
    public void reduce(Integer cid) {
        couponsExample.clear();
        Coupons coupons = couponsMapper.selectByPrimaryKey(cid);
        if (coupons.getNumber() == 0){
            throw new SystemException(CouponsExceptionCode.NOCoupons);
        }
        coupons.setNumber(coupons.getNumber()-1);
        int i = couponsMapper.updateByPrimaryKeySelective(coupons);
        if (i == -1){
            throw new ErrorException(BaseCode.System_Error,"优惠卷出纳出错！");
        }
    }

}
