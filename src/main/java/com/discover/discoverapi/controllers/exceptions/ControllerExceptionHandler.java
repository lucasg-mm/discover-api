package com.discover.discoverapi.controllers.exceptions;

import com.discover.discoverapi.services.exceptions.FailedToDownloadException;
import com.discover.discoverapi.services.exceptions.FailedToUploadException;
import com.discover.discoverapi.services.exceptions.InvalidInputException;
import com.discover.discoverapi.services.exceptions.ObjectNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.validation.ConstraintViolationException;

@ControllerAdvice
public class ControllerExceptionHandler {
    // returns a response with a basic error
    public ResponseEntity<StandardError> getBasicExceptionResponse(HttpStatus status, RuntimeException exception){
        // creates a new standard error
        StandardError error = new StandardError(status.value(),
                System.currentTimeMillis(), exception.getMessage());

        // returns the error with the NOT FOUND status code
        return ResponseEntity.status(status).body(error);
    }

    // handle exceptions that cause NOT FOUND
    @ExceptionHandler(ObjectNotFoundException.class)
    public ResponseEntity<StandardError> handleNotFound(RuntimeException exception){
        return getBasicExceptionResponse(HttpStatus.NOT_FOUND, exception);
    }

    // handle exceptions that cause BAD REQUEST
    @ExceptionHandler({
            InvalidInputException.class,
            MethodArgumentTypeMismatchException.class,
            HttpMessageNotReadableException.class,
            ConstraintViolationException.class,
            DataIntegrityViolationException.class
    })
    public ResponseEntity<StandardError> handleBadRequest(RuntimeException exception){
        return getBasicExceptionResponse(HttpStatus.BAD_REQUEST, exception);
    }

    // handle exceptions that cause INTERNAL SERVER ERROR
    @ExceptionHandler({
            FailedToUploadException.class,
            FailedToDownloadException.class
    })
    public ResponseEntity<StandardError> handleInternalServerError(RuntimeException exception){
        return  getBasicExceptionResponse(HttpStatus.INTERNAL_SERVER_ERROR, exception);
    }
}
