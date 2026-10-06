package com.nps.ecommercebff.orders.model;

import java.util.List;

/**
 * Represents a request to create or update an order in the e-commerce application.
 */
public record OrderRequest(

        String orderId,
        String customerId,
        List<OrderItem> items
) {}
