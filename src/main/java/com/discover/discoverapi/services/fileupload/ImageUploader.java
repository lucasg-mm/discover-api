package com.discover.discoverapi.services.fileupload;

import com.discover.discoverapi.services.exceptions.FailToUploadException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

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

    // verifies file, then upload it
    @Override
    public void upload(MultipartFile file, String path) {
        // check if file is empty
        if (file.isEmpty()) {
            throw new FailToUploadException("Cannot upload empty file!");
        }

        // check if file is image
        if (!isFileImage(file)) {
            throw new FailToUploadException("It's only possible to upload files of type PNG or JPEG.");
        }

        // builds image name
        String fileName = UUID.randomUUID() + file.getOriginalFilename();

        // saves the file
        try {
            awsFileStore.save(path, fileName, file.getInputStream());
        } catch (IOException e) {
            throw new FailToUploadException("Failed to upload the file.");
        }
    }
}
