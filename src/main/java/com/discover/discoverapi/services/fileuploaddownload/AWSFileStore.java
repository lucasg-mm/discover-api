package com.discover.discoverapi.services.fileuploaddownload;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import com.discover.discoverapi.services.exceptions.FailedToUploadException;
import com.discover.discoverapi.services.exceptions.FailedToDownloadException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

@Service
@AllArgsConstructor
public class AWSFileStore implements FileStore{
    private final AmazonS3 amazonS3;
    private static final String BUCKET_NAME = "discover-api";

    // parses the image metadata as a Map to ObjectMetadata
    private ObjectMetadata createObjectMetadataFromMap(Map<String, String> imageMetadata){
        // instantiates the ObjectMetadata
        ObjectMetadata objectMetadata = new ObjectMetadata();

        // iterates through maps, adding a new metadata for each iteration
        for (var entry : imageMetadata.entrySet()){
            objectMetadata.addUserMetadata(entry.getKey(), entry.getValue());
        }

        return objectMetadata;
    }

    // upload file to Amazon S3
    public void save(String path, String fileName, InputStream fileToUpload, Map<String, String> imageMetadata){
        // hold the object metadata
        ObjectMetadata objectMetadata = createObjectMetadataFromMap(imageMetadata);

        try{
            // saves the file
            amazonS3.putObject(BUCKET_NAME + "/" + path, fileName, fileToUpload, objectMetadata);
        }
        catch(AmazonServiceException e){
            throw new FailedToUploadException("Failed to upload the file.");
        }
    }

    // download file from Amazon S3
    public byte[] download(String path, String fileName){
        try{
            S3Object object = amazonS3.getObject(BUCKET_NAME + "/" + path, fileName);
            S3ObjectInputStream objectContent = object.getObjectContent();
            return IOUtils.toByteArray(objectContent);
        }
        catch(AmazonServiceException | IOException e){
            throw new FailedToDownloadException("Failed to download the image.");
        }
    }
}
