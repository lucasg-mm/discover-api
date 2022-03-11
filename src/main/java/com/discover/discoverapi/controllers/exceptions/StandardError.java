package com.discover.discoverapi.controllers.exceptions;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Schema(description = "Represents a standard error.")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class StandardError {
    @Schema(description = "Error's http status code.")
    private int status;

    @Schema(description = "Error's timestamp (when the error occurred).")
    private long timestamp;

    @Schema(description = "Error's message.")
    private String message;
}
