package com.discover.discoverapi.services.fileuploaddownload;

import org.springframework.web.multipart.MultipartFile;

public interface UploaderDownloader {
    void upload(MultipartFile file, String path, String fileName);
    byte[] download(String path, String fileName);
}
