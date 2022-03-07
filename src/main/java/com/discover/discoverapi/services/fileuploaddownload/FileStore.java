package com.discover.discoverapi.services.fileuploaddownload;

import java.io.InputStream;
import java.util.Map;

public interface FileStore {
    void save(String path, String fileName, InputStream fileToUpload, Map<String, String> imageMetadata);
    byte[] download(String path, String fileName);
}
