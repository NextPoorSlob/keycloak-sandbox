package com.nps.ecommercebff.orders.controller;

import com.nps.ecommercebff.orders.model.OrderItemRequest;
import com.nps.ecommercebff.orders.model.OrderRequest;
import com.nps.ecommercebff.orders.service.OrderCrudService;
import org.jspecify.annotations.NonNull;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

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

    @SuppressWarnings("SameParameterValue")
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

    private static String orderRequestJson() {
        return """
                {
                  "orderName": "order-1",
                  "customerId": "customer-1",
                  "items": [{"productId": "product-1", "quantity": 1, "price": 19.99}]
                }
                """;
    }

    @Test
    void getOrders_returnEmpty() throws Exception {
        when(orderCrudService.getOrders()).thenReturn(List.of());

        mockMvc.perform(get("/api/v1/orders"))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        []
                        """));
    }

    @Test
    void getOrders_returnsTwoOrders() throws Exception {
        when(orderCrudService.getOrders()).thenReturn(buildOrderRequests(2));

        mockMvc.perform(get("/api/v1/orders"))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        [
                          {
                            "orderName": "order-1",
                            "customerId": "customer-1",
                            "items": [{"productId": "product-1", "quantity": 1, "price": 19.99}]
                          },
                          {
                            "orderName": "order-2",
                            "customerId": "customer-2",
                            "items": [{"productId": "product-2", "quantity": 2, "price": 29.99}]
                          }
                        ]
                        """));
    }

    @Test
    void getOrder_returnsOrderWhenFound() throws Exception {
        when(orderCrudService.getOrderById("order-1")).thenReturn(Optional.of(buildOrderRequests(1).getFirst()));

        mockMvc.perform(get("/api/v1/orders/order-1"))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        {
                          "orderName": "order-1",
                          "customerId": "customer-1",
                          "items": [{"productId": "product-1", "quantity": 1, "price": 19.99}]
                        }
                        """));
    }

    @Test
    void getOrder_returnsNotFoundWhenOrderDoesNotExist() throws Exception {
        when(orderCrudService.getOrderById("missing-order")).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/orders/missing-order"))
                .andExpect(status().isNotFound())
                .andExpect(content().json("""
                        {
                          "status": "error",
                          "httpStatus": 404,
                          "message": "Order [missing-order] not found.",
                          "data": null
                        }
                        """));
    }

    @Test
    void createOrder_returnsCreatedOrder() throws Exception {
        OrderRequest expectedOrder = buildOrderRequests(1).getFirst();
        when(orderCrudService.createOrder(expectedOrder)).thenReturn(expectedOrder);

        mockMvc.perform(post("/api/v1/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(orderRequestJson()))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(content().json("""
                        {
                          "orderName": "order-1",
                          "customerId": "customer-1",
                          "items": [{"productId": "product-1", "quantity": 1, "price": 19.99}]
                        }
                        """));
    }

    @Test
    void updateOrder_returnsUpdatedOrderWhenFound() throws Exception {
        OrderRequest expectedOrder = buildOrderRequests(1).getFirst();
        when(orderCrudService.updateOrder("order-1", expectedOrder))
                .thenReturn(Optional.of(expectedOrder));

        mockMvc.perform(put("/api/v1/orders/order-1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(orderRequestJson()))
                .andExpect(status().isNoContent())
                .andExpect(content().string(""));
    }

    @Test
    void updateOrder_returnsNotFoundWhenOrderDoesNotExist() throws Exception {
        when(orderCrudService.updateOrder("missing-order", buildOrderRequests(1).getFirst()))
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
                .andExpect(status().isNoContent())
                .andExpect(content().string(""));
    }

    @Test
    void deleteOrder_returnsNotFoundWhenOrderDoesNotExist() throws Exception {
        when(orderCrudService.deleteOrder("missing-order")).thenReturn(false);

        mockMvc.perform(delete("/api/v1/orders/missing-order"))
                .andExpect(status().isNotFound());
    }
}