package com.cs.parking.mapper;

import com.cs.parking.pojo.ParkingSpace;
import com.cs.parking.pojo.ParkingSpaceExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

@Component
public interface ParkingSpaceMapper {
    long countByExample(ParkingSpaceExample example);

    int deleteByExample(ParkingSpaceExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(ParkingSpace record);

    int insertSelective(ParkingSpace record);

    List<ParkingSpace> selectByExample(ParkingSpaceExample example);

    ParkingSpace selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") ParkingSpace record, @Param("example") ParkingSpaceExample example);

    int updateByExample(@Param("record") ParkingSpace record, @Param("example") ParkingSpaceExample example);

    int updateByPrimaryKeySelective(ParkingSpace record);

    int updateByPrimaryKey(ParkingSpace record);
}