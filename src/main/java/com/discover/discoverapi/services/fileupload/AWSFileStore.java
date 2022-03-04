package com.discover.discoverapi.services.fileupload;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.discover.discoverapi.services.exceptions.FailToUploadException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

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
            throw new FailToUploadException("Failed to upload the file.");
        }
    }
}
