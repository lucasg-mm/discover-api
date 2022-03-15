package com.discover.discoverapi.controllers.exceptions;

import com.discover.discoverapi.services.exceptions.ObjectNotFoundException;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class ControllerExceptionHandler {
    // returns an error response with a custom error message (with field errors)
    public ResponseEntity<StandardError> getExceptionResponse(HttpStatus statusCode, String errorMessage,
                                                              Map<String, String> fieldErrors, Exception exception){
        // creates a new standard error
        StandardError error = new StandardError(statusCode.value(), errorMessage,
                ExceptionUtils.getStackTrace(exception), fieldErrors);

        // returns the error with the appropriate status code
        return ResponseEntity.status(statusCode).body(error);
    }

    // returns an error response with a custom error message (without field errors)
    public ResponseEntity<StandardError> getExceptionResponse(HttpStatus statusCode, String errorMessage,
                                                              Exception exception){
        // creates a new standard error
        StandardError error = new StandardError(statusCode.value(), errorMessage,
                ExceptionUtils.getStackTrace(exception));

        // returns the error with the appropriate status code
        return ResponseEntity.status(statusCode).body(error);
    }

    // handle custom exception thrown when an object (a track, artist, album or genre) was not found
    @ExceptionHandler(ObjectNotFoundException.class)
    public ResponseEntity<StandardError> handleObjectNotFoundException(ObjectNotFoundException exception){
        return getExceptionResponse(HttpStatus.NOT_FOUND, exception.getMessage(), null, exception);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public  ResponseEntity<StandardError> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex){
        Map<String, String> fieldErrors = new HashMap<>();

        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName =  ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            fieldErrors.put(fieldName, errorMessage);
        });

        return getExceptionResponse(HttpStatus.BAD_REQUEST, "Validation on some field(s) failed.",
                fieldErrors, ex);
    }

    // handle exception thrown when a method receives a wrong parameter
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<StandardError> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex){
        // gets the variable name
        String variableName = ex.getName();

        // gets the expected type of the variable
        String expectedType = ex.getRequiredType().getSimpleName();

        // gets the actual received value
        Object variableValue = ex.getValue();
        String message = String.format("'%s' should be a valid '%s' and '%s' isn't.",
                variableName, expectedType, variableValue);

        // returns the exception response
        return getExceptionResponse(HttpStatus.BAD_REQUEST, message, ex);
    }


//    // handle custom exception thrown
//    @ExceptionHandler(InvalidInputException.class)
//    public ResponseEntity<StandardError> handleInvalidInputException(InvalidInputException exception){
//        return getExceptionResponse(HttpStatus.BAD_REQUEST, exception);
//    }
//
//
//    // handle exception thrown when
//    @ExceptionHandler(HttpMessageNotReadableException.class)
//    public ResponseEntity<StandardError> handleHttpMessageNotReadableException(){
//
//    }
//
//    // handle exception thrown when
//    @ExceptionHandler(ConstraintViolationException.class)
//    public ResponseEntity<StandardError> handleConstraintViolationException(){
//
//    }
//
//    // handle exception thrown when
//    @ExceptionHandler(DataIntegrityViolationException.class)
//    public ResponseEntity<StandardError> handleDataIntegrityViolationException(){
//
//    }
//
//    // handle custom exceptions thrown
//    @ExceptionHandler(FailedToUploadException.class)
//    public ResponseEntity<StandardError> handleFailedToUploadException(){
//
//    }
//
//    // handle custom exceptions thrown
//    @ExceptionHandler(FailedToDownloadException.class)
//    public ResponseEntity<StandardError> handleFailedToDownloadException(){
//
//    }
}
