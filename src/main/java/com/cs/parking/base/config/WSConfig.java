package com.cs.parking.base.config;

import lombok.Data;
import org.springframework.stereotype.Component;


/**
 * @ClassName config
 * @Description TODO
 * @Author QQ163
 * @Date 2020/7/21 18:36
 **/
@Data
@Component
public class WSConfig {
    public static final Integer port = 9091;
    public static final String secretKey = "CGParkingProject20200116";
}
