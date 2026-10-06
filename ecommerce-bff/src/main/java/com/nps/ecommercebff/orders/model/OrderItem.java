package com.nps.ecommercebff.orders.model;

/**
 * Represents an item in an order in the e-commerce application.
 */
public record OrderItem(
        String productId,
        int quantity,
        double price
)
{}
