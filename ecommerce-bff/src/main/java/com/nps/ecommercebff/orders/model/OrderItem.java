package com.nps.ecommercebff.orders.model;

import java.math.BigDecimal;

/**
 * Represents an item in an order in the e-commerce application.
 */
public record OrderItem(
        String productId,
        int quantity,
        BigDecimal price
)
{}
