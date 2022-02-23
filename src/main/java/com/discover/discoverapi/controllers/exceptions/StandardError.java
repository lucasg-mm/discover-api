package com.discover.discoverapi.controllers.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class StandardError {
    private int status;
    private long timestamp;
    private String message;
}
