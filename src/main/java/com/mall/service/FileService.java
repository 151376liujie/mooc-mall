package com.mall.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * Author: jonny
 * Time: 2017-05-26 23:51.
 */
public interface FileService {

    String upload(MultipartFile file, String path) throws IOException;

}
