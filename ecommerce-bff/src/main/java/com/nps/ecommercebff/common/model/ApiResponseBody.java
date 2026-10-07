package com.nps.ecommercebff.common.model;

public record ApiResponseBody<T>(
        String status,
        int httpStatus,
        String message,
        T data
) {
}
