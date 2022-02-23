package com.discover.discoverapi.controllers.exceptions;

import com.discover.discoverapi.services.exceptions.ObjectNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ControllerExceptionHandler {
    @ExceptionHandler(ObjectNotFoundException.class)
    // handling ObjectNotFoundException exceptions
    public ResponseEntity<StandardError> handleObjectNotFound(ObjectNotFoundException exception){
        // creates a new standard error
        StandardError error = new StandardError(HttpStatus.NOT_FOUND.value(),
                System.currentTimeMillis(), exception.getMessage());

        // returns the error with the NOT FOUND status code
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }
}
