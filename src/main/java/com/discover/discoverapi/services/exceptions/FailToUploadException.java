package com.discover.discoverapi.services.exceptions;

public class FailToUploadException extends RuntimeException{
    public FailToUploadException(String message){
        super(message);
    }
}
