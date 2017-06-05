package com.mall.service.impl;

import com.google.common.collect.Lists;
import com.mall.service.FileService;
import com.mall.util.FTPUtil;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * Author: jonny
 * Time: 2017-05-26 23:53.
 */
@Service
public class FileServiceImpl implements FileService {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileServiceImpl.class);

    @Override
    public String upload(MultipartFile multipartFile, String path){
        String originalFilename = multipartFile.getOriginalFilename();
        String extension = FilenameUtils.getExtension(originalFilename);
        String uploadFileName = UUID.randomUUID().toString() + FilenameUtils.EXTENSION_SEPARATOR_STR + extension;
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("upload file name is :{},upload path is :{}, new file name is :{}", originalFilename, path, uploadFileName);
        }
        File file = new File(path);
        if (!file.exists()) {
            file.setWritable(true);
            if (!file.mkdirs()) {
                LOGGER.warn("failed to make directory:{}",file.getPath());
                throw new RuntimeException("failed to make directory:" + file.getPath());
            }
        }
        File targetFile = new File(path,uploadFileName);
        try {
            multipartFile.transferTo(targetFile);
            FTPUtil.uploadFile(Lists.newArrayList(targetFile));
            if (!targetFile.delete()) {
                LOGGER.warn("failed to delete file:{} ",targetFile.getPath());
            }
        } catch (IOException e) {
            LOGGER.error(e.getMessage(),e);
        }
        return targetFile.getName();
    }
}
