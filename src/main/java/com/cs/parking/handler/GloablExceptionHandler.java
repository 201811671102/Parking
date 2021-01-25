package com.cs.parking.handler;

import com.cs.parking.base.DTO.ResultDTO;
import com.cs.parking.base.utils.ResultUtils;
import com.cs.parking.code.BaseCode;
import com.cs.parking.exception.ErrorException;
import com.cs.parking.exception.ParkingBaseException;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @ClassName GloablExceptionHandler
 * @Description TODO
 * @Author QQ163
 * @Date 2021/1/17 1:54
 **/
@ControllerAdvice
@Log4j2
public class GloablExceptionHandler {
    @ResponseBody
    @ExceptionHandler(ParkingBaseException.class)
    public ResultDTO<String> handleException(ParkingBaseException e){
        return ResultUtils.error(e.getCode(),e.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(ErrorException.class)
    public ResultDTO<String> errorException(ErrorException e){
        return ResultUtils.error(e.getCode(),e.getMessage(),e.getErrorMessage());
    }

    @ResponseBody
    @ExceptionHandler(Exception.class)
    public ResultDTO<String> exception(Exception e){
        log.info(e.getMessage());
        return ResultUtils.error(BaseCode.System_Error.getCode(),e.getMessage(),null);
    }
}
