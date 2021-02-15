package com.cs.parking.base.ftp;


import com.cs.parking.code.BaseCode;
import com.cs.parking.base.config.FtpConfig;
import com.cs.parking.exception.ErrorException;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;


/**
 * 类说明：文件上传下载工具类
 *
 */
@Component
@Log4j2
public class FtpOperation {

    //默认文件类型为二进制（图片）
    private static int filetype = FTP.BINARY_FILE_TYPE;

    @Autowired
    FtpConfig config;
    @Autowired
    private ResourceLoader resourceLoader;
    @Autowired
    private FtpPool pool;

    /**
     *
     * 功能：上传文件附件到文件服务器
     * @param buffIn:上传文件流
     * @return
     * @throws IOException
     */
    public  boolean uploadToFtp(InputStream buffIn,String filepath){
        FTPClient ftpClient = pool.getFTPClient();
        // 上传文件
        try {
            setFileType(filetype,ftpClient);
            int reply = ftpClient.getReplyCode();
            if(!FTPReply.isPositiveCompletion(reply))
            {
                ftpClient.disconnect();
                return false;
            }
            String fileName = null;
            ftpClient.enterLocalPassiveMode();
            if(!ftpClient.changeWorkingDirectory(filepath)){
                String[] dirs = filepath.split("/");
                fileName = dirs[dirs.length-1];
                String tempPath = "";
                for (int i=0;i<dirs.length-1;i++){
                    if (null == dirs[i] || "".equals(dirs[i]))
                        continue;
                    tempPath += "/"+dirs[i];
                    if (!ftpClient.changeWorkingDirectory(tempPath)){
                        if (!ftpClient.makeDirectory(tempPath)){
                            return false;
                        }else{
                            ftpClient.changeWorkingDirectory(tempPath);
                        }
                    }
                }
            }
            // 上传文件到ftp
            // 输出操作结果信息
            fileName = new String(fileName.getBytes("utf-8"),"iso-8859-1");
            if (ftpClient.storeFile(fileName, buffIn)) {
                return true;
            } else {
                return false;
            }
        }catch (Exception e) {
            throw new ErrorException(BaseCode.System_Error,"ERR : upload file  to ftp : failed! \n"+e.getMessage());
        } finally {
            try {
                if (buffIn != null) {
                    buffIn.close();
                }
                pool.returnFTPClient(ftpClient);
            } catch (Exception e) {
                throw new ErrorException(BaseCode.System_Error,"ftp关闭输入流时失败！ \n"+e.getMessage());
            }
        }
    }


    /**
     *
     * 功能：根据文件名称，下载文件流
     * @return
     * @throws IOException
     */
    public  InputStream  downloadFile(String filepath){
        InputStream in=null;
        FTPClient ftpClient = pool.getFTPClient();
        String filename = null;
        try {
            ftpClient.enterLocalPassiveMode();
            // 设置传输二进制文件
            setFileType(filetype,ftpClient);
            if(!FTPReply.isPositiveCompletion(ftpClient.getReplyCode()))
            {
                return null;
            }
            if (!ftpClient.changeWorkingDirectory(filepath)){
                return null;
            }
            filename = new String(filepath.substring(filepath.lastIndexOf("/")+1).getBytes("utf-8"),"iso-8859-1");
            in=ftpClient.retrieveFileStream(filename);
            return in;
        }catch (Exception e) {
            throw new ErrorException(BaseCode.System_Error,"ERR : upload file "+ filename+ " from ftp : failed! \n"+e.getMessage());
        }finally {
            pool.returnFTPClient(ftpClient);
        }
    }


    public  boolean delectFile(String filename,String filepath){
        FTPClient ftpClient = pool.getFTPClient();
        try {
            filename = new String(filename.getBytes("utf-8"),"iso-8859-1");
            ftpClient.changeWorkingDirectory(filepath);
            if (!ftpClient.deleteFile(filename)){
                return false;
            }
            return true;
        }catch (Exception e) {
            throw new ErrorException(BaseCode.System_Error,"ERR : delete file "+ filename+ " from ftp : failed! \n"+e.getMessage());
        }finally {
            pool.returnFTPClient(ftpClient);
        }
    }
    /**
     * 设置传输文件的类型[文本文件或者二进制文件]
     *
     * @param fileType
     *            --BINARY_FILE_TYPE、ASCII_FILE_TYPE
     */
    public  void setFileType(int fileType,FTPClient ftpClient) {
        try {
            ftpClient.setFileType(fileType);
        } catch (Exception e) {
            throw new ErrorException(BaseCode.System_Error,"ftp设置传输文件的类型时失败！ \n"+e.getMessage());
        }
    }
}
