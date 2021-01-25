package com.cs.parking.exception;

import com.cs.parking.code.BaseCode;

/**
 * @ClassName ErrorException
 * @Description TODO
 * @Author QQ163
 * @Date 2021/1/17 0:50
 **/
public class ErrorException extends ParkingBaseException {
    private static final long serialVersionUID = -1881229635359698225L;

    private String errorMessage;
    public ErrorException(BaseCode baseCode,String errorMessage){
        super(baseCode.getCode(),baseCode.getMessage());
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
