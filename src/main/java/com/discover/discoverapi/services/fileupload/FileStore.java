package com.discover.discoverapi.services.fileupload;

import java.io.File;
import java.io.InputStream;

public interface FileStore {
    public void save(String path, String fileName, InputStream fileToUpload);
}
