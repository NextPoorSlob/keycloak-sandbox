package com.nps.ecommercebff.orders.controller;

import com.nps.ecommercebff.common.exception.ResourceNotFoundException;
import com.nps.ecommercebff.orders.model.OrderRequest;
import com.nps.ecommercebff.orders.service.OrderCrudService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Orders", description = "Operations for managing customer orders")
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
    @Operation(summary = "List orders", description = "Returns all orders.")
    @ApiResponse(responseCode = "200", description = "Orders returned successfully")
    public ResponseEntity<List<OrderRequest>> getOrders() {
        return ResponseEntity.ok(orderService.getOrders());
    }

    /**
     * Returns the requested order.
     *
     * @param orderId the ID of order to return.
     * @return the order specified by the ID.
     */
    @GetMapping("/{orderId}")
    @Operation(summary = "Get an order", description = "Returns an order by its ID.")
    @SuppressWarnings("squid:S1710")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Order returned successfully"),
            @ApiResponse(responseCode = "404", description = "Order not found", content = @Content)
    })
    public ResponseEntity<OrderRequest> getOrder(
            @Parameter(description = "ID of the order to retrieve", required = true)
            @PathVariable String orderId) {
        return orderService.getOrderById(orderId)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(ORDER_NOT_FOUND, orderId)));
    }

    /**
     * Creates a new order in the application.
     *
     * @param order the new order to complete.
     * @return the created order.
     */
    @PostMapping
    @Operation(summary = "Create an order", description = "Creates an order and returns its location.")
    @SuppressWarnings("squid:S1710")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Order created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid order request", content = @Content)
    })
    public ResponseEntity<OrderRequest> createOrder(@RequestBody OrderRequest order) {
        OrderRequest createdOrder = orderService.createOrder(order);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{orderId}")
                .buildAndExpand(
                        createdOrder.orderId())
                .toUri();
        return ResponseEntity.created(location).body(createdOrder);
    }

    /**
     * Updates the existing order.
     *
     * @param orderId the ID of the order to update. This has to match the orderId in the order data.
     * @param order   the complete order data for the order to update.
     * @return a no-content response when the order is updated.
     */
    @PutMapping("/{orderId}")
    @Operation(summary = "Update an order", description = "Replaces an existing order by its ID.")
    @SuppressWarnings("squid:S1710")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Order updated successfully", content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid order request", content = @Content),
            @ApiResponse(responseCode = "404", description = "Order not found", content = @Content)
    })
    public ResponseEntity<Void> updateOrder(
            @Parameter(description = "ID of the order to update", required = true)
            @PathVariable String orderId,
            @RequestBody OrderRequest order) {
        orderService.updateOrder(orderId, order)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(ORDER_NOT_FOUND, orderId)));
        return ResponseEntity.noContent().build();
    }

    /**
     * Deletes the specified order.
     *
     * @param orderId the ID of the order to delete.
     * @return a no-content response when the order is deleted.
     */
    @DeleteMapping("/{orderId}")
    @Operation(summary = "Delete an order", description = "Deletes an order by its ID.")
    @SuppressWarnings("squid:S1710")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Order deleted successfully", content = @Content),
            @ApiResponse(responseCode = "404", description = "Order not found", content = @Content)
    })
    public ResponseEntity<Void> deleteOrder(
            @Parameter(description = "ID of the order to delete", required = true)
            @PathVariable String orderId) {
        if (!orderService.deleteOrder(orderId)) {
            throw new ResourceNotFoundException(String.format(ORDER_NOT_FOUND, orderId));
        }
        return ResponseEntity.noContent().build();
    }
}
