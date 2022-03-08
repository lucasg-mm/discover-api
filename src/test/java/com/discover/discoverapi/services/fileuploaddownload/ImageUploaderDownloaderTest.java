package com.discover.discoverapi.services.fileuploaddownload;

import com.discover.discoverapi.services.exceptions.FailedToUploadException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class ImageUploaderDownloaderTest {
    @InjectMocks
    ImageUploaderDownloader imageUploaderDownloader;

    @Mock
    FileStore awsFileStore;

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("The method imageUploaderDownloader.upload() throws FailedToUploadException " +
            "when the provided file is empty.")
    public void uploadMethodThrowsFailedToUploadExceptionWhenTheFileIsEmpty(){
        // --- GIVEN ---

        byte[] emptyContent = null;
        MultipartFile file = new MockMultipartFile("file_name.png", emptyContent);

        // --- WHEN THEN ---

        assertThrows(FailedToUploadException.class,
                () -> imageUploaderDownloader.upload(file, "", "file_name"), "The method " +
                        "imageUploaderDownloader.upload() should throw FailedToUploadException when the provided" +
                        " file is empty.");

    }

    @Test
    @DisplayName("The method imageUploaderDownloader.upload() throws FailedToUploadException " +
            "when the provided file is not an image.")
    public void uploadMethodThrowsFailedToUploadWhenTheFileIsNotAnImage(){
        // --- GIVEN ---

        byte[] fileContent = "For testing".getBytes();
        MultipartFile file = new MockMultipartFile("file_name.pdf", "file_name.pdf",
                "application/pdf", fileContent);

        // --- WHEN THEN ---
        assertThrows(FailedToUploadException.class,
                () -> imageUploaderDownloader.upload(file, "", "file_name"), "The method " +
                        "imageUploaderDownloader.upload() should throw FailedToUploadException when the provided" +
                        " file is not an image.");
    }
}
