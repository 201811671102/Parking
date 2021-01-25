package com.cs.parking.base.utils;

import com.cs.parking.code.BaseCode;
import com.cs.parking.code.ParameterCode;
import com.cs.parking.exception.ErrorException;
import com.cs.parking.mapper.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.Map;

/**
 * @ClassName ParameterVerifyUtil
 * @Description TODO
 * @Author QQ163
 * @Date 2021/1/19 2:29
 **/
@Configuration
public class ParameterVerifyUtil {
    private static class ParameterVerifyHolder{
        private static final ParameterVerifyUtil instance = new ParameterVerifyUtil();
    }
    public ParameterVerifyUtil(){}
    public static ParameterVerifyUtil getInstance(){return ParameterVerifyHolder.instance;}

    public static ParameterVerifyUtil parameterVerifyUtil;
    @PostConstruct
    public void init(){parameterVerifyUtil = this;}

    @Autowired
    UserMapper userMapper;
    @Autowired
    CouponsMapper couponsMapper;
    @Autowired
    OrderRecordMapper orderRecordMapper;
    @Autowired
    ParkingSpaceMapper parkingSpaceMapper;
    @Autowired
    UserCouponsMapper userCouponsMapper;
    @Autowired
    ReportMapper reportMapper;

    public void verifyParameterVer(String[] parameterKeys, String[] parameterNames, ParameterCode parameterCode, Map<String, String[]> parameterMap){
        for (int i=0;i<parameterKeys.length;i++){
            String[] values = parameterMap.get(parameterKeys[i]);
            Object result = null;
            for (String value : values){
                switch (parameterCode){
                    case UserParameter:
                        result = parameterVerifyUtil.userMapper.selectByPrimaryKey(Integer.parseInt(value));
                        break;
                    case CouponsParameter:
                        result = parameterVerifyUtil.couponsMapper.selectByPrimaryKey(Integer.parseInt(value));
                        break;
                    case OrderRecordParameter:
                        result = parameterVerifyUtil.orderRecordMapper.selectByPrimaryKey(Integer.parseInt(value));
                        break;
                    case ParkingSpaceParameter:
                        result = parameterVerifyUtil.parkingSpaceMapper.selectByPrimaryKey(Integer.parseInt(value));
                        break;
                    case ReportParameter:
                        result = parameterVerifyUtil.reportMapper.selectByPrimaryKey(Integer.parseInt(value));
                        break;
                    case UserCouponsParameter:
                        result = parameterVerifyUtil.userCouponsMapper.selectByPrimaryKey(Integer.parseInt(value));
                        break;
                }
                if (result == null){
                    throw new ErrorException(BaseCode.Null,"查无数据："+parameterNames[i]+" = "+value);
                }
            }
        }
    }
}
