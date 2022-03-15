package com.discover.discoverapi.controllers.exceptions;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

@Schema(description = "Represents a standard error.")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class StandardError {
    @Schema(description = "Error's http status code.")
    private int statusCode;

    @Schema(description = "Error's timestamp (when the error occurred).")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;

    @Schema(description = "Error's message.")
    private String message;

    @Schema(description = "Error's stack trace.")
    private String stackTrace;

    @Schema(description = "When field-based validation happens, this property holds the field name on the key, " +
            "and the error associated with it that caused the validation exception.")
    private Map<String, String> fieldErrors;

    public StandardError(int statusCode, String message, String stackTrace, Map<String, String> fieldErrors) {
        this.statusCode = statusCode;
        this.message = message;
        this.stackTrace = stackTrace;
        this.fieldErrors = fieldErrors;
        this.timestamp = LocalDateTime.now();
    }

    public StandardError(int statusCode, String message, String stackTrace) {
        this.statusCode = statusCode;
        this.message = message;
        this.stackTrace = stackTrace;
        this.timestamp = LocalDateTime.now();
    }
}
