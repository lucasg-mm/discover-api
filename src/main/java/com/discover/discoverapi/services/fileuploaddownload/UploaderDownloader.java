package com.discover.discoverapi.services.fileuploaddownload;

import org.springframework.web.multipart.MultipartFile;

public interface UploaderDownloader {
    public void upload(MultipartFile file, String path, String fileName);
}
