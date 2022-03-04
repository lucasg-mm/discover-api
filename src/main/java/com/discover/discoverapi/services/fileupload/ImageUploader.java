package com.discover.discoverapi.services.fileupload;

import com.discover.discoverapi.services.exceptions.FailToUploadException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

import static org.apache.http.entity.ContentType.IMAGE_JPEG;
import static org.apache.http.entity.ContentType.IMAGE_PNG;

@AllArgsConstructor
@Service
public class ImageUploader implements Uploader {
    // use to actually saving the file
    private FileStore awsFileStore;

    // used to verify if a file is supported or no
    private final List supportedMimeTypes = Arrays.asList(IMAGE_PNG.getMimeType(), IMAGE_JPEG.getMimeType());

    // tells if a file is an image or not
    public boolean isFileImage(MultipartFile file) {
        return supportedMimeTypes.contains(file.getContentType());
    }

    public Map<String, String> getsImageMetadata(MultipartFile file){
        //get file metadata
        Map<String, String> metadata = new HashMap<>();
        metadata.put("Content-Type", file.getContentType());
        metadata.put("Content-Length", String.valueOf(file.getSize()));

        return metadata;
    }

    // verifies file, then upload it
    @Override
    public void upload(MultipartFile file, String path, String fileName) {
        // check if file is empty
        if (file.isEmpty()) {
            throw new FailToUploadException("Cannot upload empty file!");
        }

        // check if file is image
        if (!isFileImage(file)) {
            throw new FailToUploadException("It's only possible to upload files of type PNG or JPEG.");
        }

        // gets file metadata
        Map<String, String> imageMetadata = getsImageMetadata(file);

        // saves the file
        try {
            awsFileStore.save(path, fileName, file.getInputStream(), imageMetadata);
        } catch (IOException e) {
            throw new FailToUploadException("Failed to upload the file.");
        }
    }
}
