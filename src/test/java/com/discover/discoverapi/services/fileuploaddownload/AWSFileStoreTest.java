package com.discover.discoverapi.services.fileuploaddownload;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.discover.discoverapi.services.exceptions.FailedToDownloadException;
import com.discover.discoverapi.services.exceptions.FailedToUploadException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;


public class AWSFileStoreTest {
    @InjectMocks
    AWSFileStore awsFileStore;

    @Mock
    private AmazonS3 amazonS3;

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    public Map<String, String> getValidFileMetadata(){
        Map<String, String> theMetadata = new HashMap<>();
        theMetadata.put("Content-type", "image/png");
        return theMetadata;
    }

    @Test
    @DisplayName("Checks if awsFileStore.save() throws FailedToUploadException when amazonS3.putObject " +
            "throws AmazonServiceException .")
    public void saveThrowsFailedToUploadExceptionWhenPutObjectThrowsAmazonServiceException (){
        // --- GIVEN ---

        Map<String, String> passedMetadata = getValidFileMetadata();
        doThrow(AmazonServiceException.class).when(amazonS3).putObject(any(), any(), any(), any());

        // --- WHEN THEN---

        assertThrows(FailedToUploadException.class, () -> awsFileStore.save("", "file.png",
                null, passedMetadata), "The awsFileStore.save() method should throw" +
                " a FailedToUploadException when amazonS3.putObject throws AmazonServiceException.");

    }

    @Test
    @DisplayName("Checks if awsFileStore.save() throws FailedToUploadException when amazonS3.putObject " +
            "throws SdkClientException.")
    public void saveThrowsFailedToUploadExceptionWhenPutObjectThrowsSdkClientException(){
        // --- GIVEN ---

        Map<String, String> passedMetadata = getValidFileMetadata();
        doThrow(SdkClientException.class).when(amazonS3).putObject(any(), any(), any(), any());

        // --- WHEN THEN---

        assertThrows(FailedToUploadException.class, () -> awsFileStore.save("", "file.png",
                null, passedMetadata), "");

    }

    @Test
    @DisplayName("Tests if giving a null fileMetadata do not throw an exception")
    public void saveDoesNotThrowExceptionWhenProvidedWithNullFileMetadata(){
        assertDoesNotThrow(() -> awsFileStore.save("", "", null, null),
                "awsFileStore.save() should not throw an exception when provided with a null fileMetadata.");
    }
}
