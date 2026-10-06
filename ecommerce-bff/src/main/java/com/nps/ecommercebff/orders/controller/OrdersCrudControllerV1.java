package com.nps.ecommercebff.orders.controller;

import com.nps.ecommercebff.orders.exception.OrderNotFoundException;
import com.nps.ecommercebff.orders.model.OrderRequest;
import com.nps.ecommercebff.orders.service.OrderCrudService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

/**
 * Provides CRUD operations for managing orders in the e-commerce application.
 */
@RestController
@RequestMapping("/api/v1/orders")
public class OrdersCrudControllerV1 {
    public static final String ORDER_NOT_FOUND = "Order [%s] not found.";
    private final OrderCrudService orderService;

    public OrdersCrudControllerV1(OrderCrudService orderService) {
        this.orderService = orderService;
    }

    /**
     * Returns all the orders in the database.
     *
     * @return the orders in the database.
     */
    @GetMapping
    public List<OrderRequest> getOrders() {
        return orderService.getOrders();
    }

    /**
     * Returns the requested order.
     *
     * @param orderId the ID of order to return.
     * @return the order specified by the ID.
     */
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderRequest> getOrder(@PathVariable String orderId) {
        return orderService.getOrderById(orderId)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new OrderNotFoundException(String.format(ORDER_NOT_FOUND, orderId)));
    }

    /**
     * Creates a new order in the application.
     *
     * @param order the new order to complete.
     * @return the created order.
     */
    @PostMapping
    public ResponseEntity<OrderRequest> createOrder(@RequestBody OrderRequest order) {
        OrderRequest createdOrder = orderService.createOrder(order);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{orderId}")
                .buildAndExpand(createdOrder.orderId())
                .toUri();
        return ResponseEntity.created(location).body(createdOrder);
    }

    /**
     * Updates the existing order.
     *
     * @param orderId the ID of the order to update. This has to match the orderId in the order data.
     * @param order   the complete order data for the order to update.
     * @return the updated order.
     */
    @PutMapping("/{orderId}")
    public ResponseEntity<OrderRequest> updateOrder(
            @PathVariable String orderId,
            @RequestBody OrderRequest order) {
        return orderService.updateOrder(orderId, order)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new OrderNotFoundException(String.format(ORDER_NOT_FOUND, orderId)));
    }

    /**
     * Deletes the specified order.
     *
     * @param orderId the ID of the order to delete.
     * @return the HTTP status code.
     */
    @DeleteMapping("/{orderId}")
    public ResponseEntity<Void> deleteOrder(@PathVariable String orderId) {
        if (!orderService.deleteOrder(orderId)) {
            throw new OrderNotFoundException(String.format(ORDER_NOT_FOUND, orderId));
        }
        return ResponseEntity.noContent().build();
    }
}
