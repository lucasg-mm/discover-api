package com.discover.discoverapi.controllers.exceptions;

import com.discover.discoverapi.services.exceptions.ObjectNotFoundException;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Path;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class ControllerExceptionHandler {
    // returns an error response without a custom error message and field errors
    public ResponseEntity<StandardError> getExceptionResponse(HttpStatus statusCode, Exception exception){
        // creates a new standard error
        StandardError error = new StandardError(statusCode.value(), exception.getMessage(),
                ExceptionUtils.getStackTrace(exception));

        // returns the error with the appropriate status code
        return ResponseEntity.status(statusCode).body(error);
    }

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
        return getExceptionResponse(HttpStatus.NOT_FOUND, exception.getMessage(), exception);
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

    // handle exception thrown when there are javax constraint violations
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<StandardError> handleConstraintViolationException(ConstraintViolationException ex){
        final String message = "Validation on some fields failed";
        final HashMap<String, String> fieldErrors = new HashMap<>();

        // gets all constraint failures (field name and its failure message)
        for (ConstraintViolation violation : ex.getConstraintViolations()){
            String fieldMessage = violation.getMessage();
            String fieldName = "";
            for (Path.Node node: violation.getPropertyPath()){
                fieldName = node.getName();
            }
            fieldErrors.put(fieldName, fieldMessage);
        }


        return getExceptionResponse(HttpStatus.BAD_REQUEST, message, fieldErrors, ex);
    }


    // fallback handler method
    @ExceptionHandler(Exception.class)
    public ResponseEntity<StandardError> fallbackHandler(Exception ex){
        return getExceptionResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex);
    }
}
