package com.mall.util;

import org.apache.commons.net.ftp.FTPClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

/**
 * Author: jonny
 * Time: 2017-05-27 09:21.
 */
public class FTPUtil {

    private static String ip;
    private static int port = 21;
    private static String user;
    private static String pass;

    private FTPClient ftpClient;

    private static final Logger LOGGER = LoggerFactory.getLogger(FTPUtil.class);

    static {
        InputStream inputStream = FTPUtil.class.getResourceAsStream("ftp.properties");
        if (inputStream != null){
            Properties properties = new Properties();
            try {
                properties.load(inputStream);
                ip = properties.getProperty("ftp.server.ip");
                user = properties.getProperty("ftp.user");
                pass = properties.getProperty("ftp.pass");
            } catch (IOException e) {
                LOGGER.error(e.getMessage(),e);
            }finally {
                try {
                    inputStream.close();
                    inputStream = null;
                } catch (IOException e) {
                    LOGGER.error(e.getMessage(),e);
                }
            }
        }

    }

    private FTPUtil(String ip, int port, String user, String pass) {
        this.ip = ip;
        this.port = port;
        this.user = user;
        this.pass = pass;
    }

    public static boolean uploadFile(List<File> files) throws IOException {
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("start connect ftpclient...");
        }
        FTPUtil ftpUtil = new FTPUtil(ip,port,user,pass);
        boolean success = ftpUtil.uploadFile("img", files);
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("upload file over.result is :{}",success);
        }
        return success;
    }

    private boolean uploadFile(String remotePath, List<File> files) throws IOException {
        FileInputStream fis = null;
        boolean upload = false;
        if (this.connect(this.ip, this.user, this.pass)) {
            try {
                ftpClient.changeWorkingDirectory(remotePath);
                ftpClient.setBufferSize(1024);
                ftpClient.setControlEncoding("UTF-8");
                ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
                ftpClient.enterLocalPassiveMode();
                for (File file : files) {
                    fis = new FileInputStream(file);
                    ftpClient.storeFile(file.getName(), fis);
                }
                upload = true;
            } catch (IOException e) {
                LOGGER.error(e.getMessage(), e);
            }finally {
                if (fis != null) {
                    fis.close();
                }
                if (ftpClient != null) {
                    ftpClient.disconnect();
                }
            }
        }
        return upload;
    }

    private boolean connect(String ip, String user, String pass) {
        ftpClient = new FTPClient();
        boolean success = false;
        try {
            ftpClient.connect(ip);
            success = ftpClient.login(user, pass);
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return success;
    }

}
