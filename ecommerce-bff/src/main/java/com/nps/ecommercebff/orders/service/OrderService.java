package com.nps.ecommercebff.orders.service;

import com.nps.ecommercebff.orders.model.OrderItemRequest;
import com.nps.ecommercebff.orders.model.OrderRequest;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implements services for Order-related processing.
 */
@Service
public class OrderService implements OrderCrudService{
    @Override
    public List<OrderRequest> getOrders() {
        return buildOrderRequests(5);
    }

    @Override
    public Optional<OrderRequest> getOrderById(String orderId) {
        if ("order-1".equals(orderId)) {
            return Optional.of(new OrderRequest(
                    "order-1",
                    "customer-1",
                    List.of(new OrderItemRequest("product-1", 1, 19.99))
            ));
        }
        return Optional.empty();
    }

    @Override
    public OrderRequest createOrder(OrderRequest order) {
        return order;
    }

    @Override
    public Optional<OrderRequest> updateOrder(String orderId, OrderRequest order) {
        if (!"order-1".equals(orderId)) {
            return Optional.empty();
        }
        return Optional.of(order);
    }

    @Override
    public boolean deleteOrder(String orderId) {
        return "order-1".equals(orderId);
    }

    private static @NonNull List<OrderRequest> buildOrderRequests(int count) {
        List<OrderRequest> orderRequests = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            double price = (1999 + i * 1000) / 100.0;
            orderRequests.add(new OrderRequest(
                    "order-" + (i + 1),
                    "customer-" + (i + 1),
                    List.of(new OrderItemRequest("product-" + (i + 1), i + 1, price))
            ));
        }
        return orderRequests;
    }

}
