package com.discover.discoverapi.services.fileupload;

import org.springframework.web.multipart.MultipartFile;

public interface Uploader {
    public void upload(MultipartFile file, String path, String fileName);
}
