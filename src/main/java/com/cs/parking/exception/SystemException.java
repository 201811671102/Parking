package com.cs.parking.exception;

import com.cs.parking.code.*;

/**
 * @ClassName SystemException
 * @Description TODO
 * @Author QQ163
 * @Date 2021/1/18 15:21
 **/
public class SystemException extends ParkingBaseException {
    private static final long serialVersionUID = 4636071869189434216L;
    public SystemException(UserExceptionCode userExceptionCode){
        super(userExceptionCode.getCode(),userExceptionCode.getMessage());
    }
    public SystemException(ReportExceptionCode reportExceptionCode){
        super(reportExceptionCode.getCode(),reportExceptionCode.getMessage());
    }
    public SystemException(OrderRecordExceptionCode orderRecordExceptionCode){
        super(orderRecordExceptionCode.getCode(),orderRecordExceptionCode.getMessage());
    }
    public SystemException(CouponsExceptionCode couponsExceptionCode){
        super(couponsExceptionCode.getCode(),couponsExceptionCode.getMessage());
    }
    public SystemException(BaseCode baseCode){
        super(baseCode.getCode(),baseCode.getMessage());
    }
    public SystemException(Integer code,String message){
        super(code,message);
    }
}
