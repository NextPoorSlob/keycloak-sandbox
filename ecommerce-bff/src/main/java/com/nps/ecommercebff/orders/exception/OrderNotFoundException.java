package com.nps.ecommercebff.orders.exception;

/**
 * Exception thrown when an order is not found in the e-commerce application.
 */
public class OrderNotFoundException extends RuntimeException {
    public OrderNotFoundException(String message) {
        super(message);
    }
}
