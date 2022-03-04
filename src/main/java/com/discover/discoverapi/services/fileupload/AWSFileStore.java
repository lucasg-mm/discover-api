package com.discover.discoverapi.services.fileupload;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.discover.discoverapi.services.exceptions.FailToUploadException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.InputStream;

@Service
@AllArgsConstructor
public class AWSFileStore implements FileStore{
    private final AmazonS3 amazonS3;
    private static final String BUCKET_NAME = "discover-api";

    // upload file to Amazon S3
    public void save(String path, String fileName, InputStream fileToUpload){
        // hold the object metadata (empty for now...)
        ObjectMetadata objectMetadata = new ObjectMetadata();
        try{
            // saves the file
            amazonS3.putObject(BUCKET_NAME + "/" + path, fileName, fileToUpload, objectMetadata);
        }
        catch(AmazonServiceException e){
            throw new FailToUploadException("Failed to upload the file.");
        }
    }
}
