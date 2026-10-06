package com.nps.ecommercebff.orders.controller;

import com.nps.ecommercebff.orders.model.OrderItem;
import com.nps.ecommercebff.orders.model.OrderRequest;
import com.nps.ecommercebff.orders.service.OrderCrudService;
import org.jspecify.annotations.NonNull;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrdersCrudControllerV1.class)
class OrdersCrudControllerV1Test {

    @MockitoBean
    private OrderCrudService orderCrudService;

    @Autowired
    private MockMvc mockMvc;

    private static @NonNull List<OrderRequest> buildOrderRequests(int count) {

        List<OrderRequest> orderRequests = new ArrayList<>();
        BigDecimal price = BigDecimal.valueOf(9.99);
        for (int i = 0; i < count; i++) {
            price = price.add(BigDecimal.valueOf(10.00));
            orderRequests.add(new OrderRequest(
                    "order-" + (i + 1),
                    "customer-" + (i + 1),
                    List.of(new OrderItem("product-" + (i + 1), i + 1, price))
            ));
        }
        return orderRequests;
    }

    private static OrderRequest buildOrderRequest() {
        return new OrderRequest(
                "order-1",
                "customer-1",
                List.of(new OrderItem("product-1", 2, BigDecimal.valueOf(19.99)))
        );
    }

    private static String orderRequestJson() {
        return """
                {
                  "orderId": "order-1",
                  "customerId": "customer-1",
                  "items": [{"productId": "product-1", "quantity": 2, "price": 19.99}]
                }
                """;
    }

    @Test
    void getOrders_returnEmpty() throws Exception {
        when(orderCrudService.getOrders()).thenReturn(List.of());

        mockMvc.perform(get("/api/v1/orders"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void getOrders_returnsTwoOrders() throws Exception {
        when(orderCrudService.getOrders()).thenReturn(buildOrderRequests(2));

        mockMvc.perform(get("/api/v1/orders"))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        [
                          {
                            "orderId": "order-1",
                            "customerId": "customer-1",
                            "items": [{"productId": "product-1", "quantity": 1, "price": 19.99}]
                          },
                          {
                            "orderId": "order-2",
                            "customerId": "customer-2",
                            "items": [{"productId": "product-2", "quantity": 2, "price": 29.99}]
                          }
                        ]
                        """));
    }

    @Test
    void getOrder_returnsOrderWhenFound() throws Exception {
        when(orderCrudService.getOrderById("order-1")).thenReturn(Optional.of(buildOrderRequest()));

        mockMvc.perform(get("/api/v1/orders/order-1"))
                .andExpect(status().isOk())
                .andExpect(content().json(orderRequestJson()));
    }

    @Test
    void getOrder_returnsNotFoundWhenOrderDoesNotExist() throws Exception {
        when(orderCrudService.getOrderById("missing-order")).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/orders/missing-order"))
                .andExpect(status().isNotFound());
    }

    @Test
    void createOrder_returnsCreatedOrder() throws Exception {
        when(orderCrudService.createOrder(buildOrderRequest())).thenReturn(buildOrderRequest());

        mockMvc.perform(post("/api/v1/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(orderRequestJson()))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(content().json(orderRequestJson()));
    }

    @Test
    void updateOrder_returnsUpdatedOrderWhenFound() throws Exception {
        when(orderCrudService.updateOrder("order-1", buildOrderRequest()))
                .thenReturn(Optional.of(buildOrderRequest()));

        mockMvc.perform(put("/api/v1/orders/order-1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(orderRequestJson()))
                .andExpect(status().isOk())
                .andExpect(content().json(orderRequestJson()));
    }

    @Test
    void updateOrder_returnsNotFoundWhenOrderDoesNotExist() throws Exception {
        when(orderCrudService.updateOrder("missing-order", buildOrderRequest()))
                .thenReturn(Optional.empty());

        mockMvc.perform(put("/api/v1/orders/missing-order")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(orderRequestJson()))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteOrder_returnsNoContentWhenDeleted() throws Exception {
        when(orderCrudService.deleteOrder("order-1")).thenReturn(true);

        mockMvc.perform(delete("/api/v1/orders/order-1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteOrder_returnsNotFoundWhenOrderDoesNotExist() throws Exception {
        when(orderCrudService.deleteOrder("missing-order")).thenReturn(false);

        mockMvc.perform(delete("/api/v1/orders/missing-order"))
                .andExpect(status().isNotFound());
    }
}