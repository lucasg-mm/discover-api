package com.discover.discoverapi.services.exceptions;

public class FailedToDownloadException extends RuntimeException{
    public FailedToDownloadException(String message){
        super(message);
    }
}
