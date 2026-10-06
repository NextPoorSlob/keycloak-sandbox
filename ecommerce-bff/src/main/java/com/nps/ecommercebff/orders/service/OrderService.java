package com.nps.ecommercebff.orders.service;

import com.nps.ecommercebff.orders.model.OrderRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Implements services for Order-related processing.
 */
@Service
public class OrderService implements OrderCrudService{
    @Override
    public List<OrderRequest> getOrders() {
        return List.of();
    }

    @Override
    public Optional<OrderRequest> getOrderById(String orderId) {
        return Optional.empty();
    }

    @Override
    public OrderRequest createOrder(OrderRequest order) {
        return null;
    }

    @Override
    public Optional<OrderRequest> updateOrder(String orderId, OrderRequest order) {
        return Optional.empty();
    }

    @Override
    public boolean deleteOrder(String orderId) {
        return false;
    }
}
