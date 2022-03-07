package com.discover.discoverapi.services.exceptions;

public class FailedToUploadException extends RuntimeException{
    public FailedToUploadException(String message){
        super(message);
    }
}
