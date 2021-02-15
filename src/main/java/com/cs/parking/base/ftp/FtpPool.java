package com.cs.parking.base.ftp;


import com.cs.parking.code.BaseCode;
import com.cs.parking.base.config.FtpConfig;
import com.cs.parking.exception.ErrorException;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * @ClassName FTPClientPool
 * @Description TODO
 * @Author QQ163
 * @Date 2020/5/8 14:39
 **/
@Component
public class FtpPool {
    FtpClientFactory factory;
    private final GenericObjectPool<FTPClient> internalPool;
    //初始化连接池
    public FtpPool(@Autowired FtpClientFactory factory){
        this.factory=factory;
        FtpConfig config = factory.getConfig();
        GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
        poolConfig.setMaxTotal(config.getMaxTotal());
        poolConfig.setMinIdle(config.getMinIdel());
        poolConfig.setMaxIdle(config.getMaxIdle());
        poolConfig.setMaxWaitMillis(config.getMaxWaitMillis());
        this.internalPool = new GenericObjectPool<FTPClient>(factory,poolConfig);
    }
    //从连接池中取连接
    public  FTPClient getFTPClient() {
        try {
            return internalPool.borrowObject();
        } catch (Exception e) {
            throw new ErrorException(BaseCode.System_Error,"从连接池获取链接失败 \n"+e.getMessage());
        }
    }
    //将链接归还到连接池
    public  void returnFTPClient(FTPClient ftpClient) {
        try {
            internalPool.returnObject(ftpClient);
        } catch (Exception e) {
            throw new ErrorException(BaseCode.System_Error,"链接归还到连接池失败 \n"+e.getMessage());
        }
    }
    /**
     * 销毁池子
     */
    public  void destroy() {
        try {
            internalPool.close();
        } catch (Exception e) {
            throw new ErrorException(BaseCode.System_Error,"销毁池子失败 \n"+e.getMessage());
        }
    }
}

