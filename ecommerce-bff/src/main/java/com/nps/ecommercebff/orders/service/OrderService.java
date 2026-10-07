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
        return buildOrderRequests();
    }

    @Override
    public Optional<OrderRequest> getOrderByName(String orderName) {
        if ("order-1".equals(orderName)) {
            return Optional.of(new OrderRequest(
                    "order-1",
                    1,
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
    public Optional<OrderRequest> updateOrder(String orderName, OrderRequest order) {
        if (!"order-1".equals(orderName)) {
            return Optional.empty();
        }
        return Optional.of(order);
    }

    @Override
    public boolean deleteOrder(String orderName) {
        return "order-1".equals(orderName);
    }

    private static @NonNull List<OrderRequest> buildOrderRequests() {
        List<OrderRequest> orderRequests = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            double price = (1999 + i * 1000) / 100.0;
            orderRequests.add(new OrderRequest(
                    "order-" + (i + 1),
                    i + 1,
                    List.of(new OrderItemRequest("product-" + (i + 1), i + 1, price))
            ));
        }
        return orderRequests;
    }

}
