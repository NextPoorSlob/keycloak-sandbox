package com.nps.ecommercebff.common.controller;

import com.nps.ecommercebff.common.model.ApiResponseBody;
import com.nps.ecommercebff.common.exception.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponseBody<Void>> handleResourceNotFound(ResourceNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiResponseBody<>(
                        "error",
                        HttpStatus.NOT_FOUND.value(),
                        exception.getMessage(),
                        null
                ));
    }
}
