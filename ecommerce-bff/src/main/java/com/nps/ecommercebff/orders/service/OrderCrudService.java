package com.nps.ecommercebff.orders.service;

import com.nps.ecommercebff.orders.model.OrderRequest;

import java.util.List;
import java.util.Optional;

/**
 * Defines the contract for performing CRUD operations on orders in the e-commerce application.
 */
public interface OrderCrudService {
    /**
     * Retrieves a list of all orders in the e-commerce application.
     * @return A list of OrderRequest objects representing all orders.
     */
    List<OrderRequest> getOrders();

    /**
     * Retrieves an order by its unique name.
     * @param orderName The unique name of the order.
     * @return An Optional containing the OrderRequest if found, or empty if not found.
     */
    Optional<OrderRequest> getOrderByName(String orderName);

    /**
     * Creates a new order in the e-commerce application.
     * @param order The OrderRequest object representing the order to be created.
     * @return The created OrderRequest object.
     */
    OrderRequest createOrder(OrderRequest order);

    /**
     * Updates an existing order in the e-commerce application.
     * @param orderName The unique name of the order to be updated.
     * @param order The OrderRequest object containing the updated order details.
     * @return An Optional containing the updated OrderRequest if the update was successful, or empty if the order was not found.
     */
    Optional<OrderRequest> updateOrder(String orderName, OrderRequest order);

    /**
     * Deletes an order by its unique name.
     * @param orderName The unique name of the order to be deleted.
     * @return true if the order was successfully deleted, false if the order was not found.
     */
    boolean deleteOrder(String orderName);
}
