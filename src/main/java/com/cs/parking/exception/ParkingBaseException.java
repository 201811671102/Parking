package com.cs.parking.exception;

import java.io.Serializable;

/**
 * @ClassName ParkingBaseException
 * @Description TODO
 * @Author QQ163
 * @Date 2021/1/16 18:37
 **/
public class ParkingBaseException extends RuntimeException implements Serializable {
    private static final long serialVersionUID = 2351137404448635713L;
    private String message;
    private Integer code;
    public ParkingBaseException(){super();}
    public ParkingBaseException(String message){ super(message);this.message = message;}
    public ParkingBaseException(Integer code,String message){ super(message);this.code = code;this.message = message; }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }
}
