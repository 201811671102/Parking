package com.cs.parking.base.config;

import lombok.Data;
import org.apache.commons.net.ftp.FTP;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @ClassName FTPProperties
 * @Description TODO
 * @Author QQ163
 * @Date 2020/5/8 14:37
 **/
@Data
@Component
public class FtpConfig {
    @Value("${parking.ftp.userName}")
    private String userName;
    @Value("${parking.ftp.passWord}")
    private String passWord;
    @Value("${parking.ftp.host}")
    private String host;
    @Value("${parking.ftp.port}")
    private Integer port;
    private Integer passiveMode = FTP.BINARY_FILE_TYPE;
    private String encoding="UTF-8";
    private int clientTimeout=120000;
    private int transferFileType=FTP.BINARY_FILE_TYPE;
    @Value("${parking.ftp.root}")
    private String root;
    @Value("${parking.ftp.MaxTotal}")
    private int MaxTotal;
    @Value("${parking.ftp.MinIdel}")
    private int MinIdel;
    @Value("${parking.ftp.MaxIdle}")
    private int MaxIdle;
    @Value("${parking.ftp.MaxWaitMillis}")
    private int MaxWaitMillis;

}
