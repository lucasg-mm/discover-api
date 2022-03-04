package com.discover.discoverapi.services.fileupload;

import java.io.InputStream;
import java.util.Map;

public interface FileStore {
    void save(String path, String fileName, InputStream fileToUpload, Map<String, String> imageMetadata);
}
