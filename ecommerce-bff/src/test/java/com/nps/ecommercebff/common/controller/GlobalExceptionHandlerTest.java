package com.nps.ecommercebff.common.controller;

import com.nps.ecommercebff.common.model.ApiResponseBody;
import com.nps.ecommercebff.common.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler exceptionHandler = new GlobalExceptionHandler();

    @Test
    void handleResourceNotFoundReturnsNotFoundResponse() {
        String message = "Resource [missing-resource] not found.";

        ResponseEntity<ApiResponseBody<Void>> response =
                exceptionHandler.handleResourceNotFound(new ResourceNotFoundException(message));

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(new ApiResponseBody<>("error", 404, message, null), response.getBody());
        assertNotNull(response.getBody());
        assertNull(response.getBody().data());
    }
}
