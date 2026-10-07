package com.nps.ecommercebff.products.controller;

import com.nps.ecommercebff.products.model.ProductRequest;
import com.nps.ecommercebff.products.service.ProductCrudService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

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

@WebMvcTest(ProductsCrudControllerV1.class)
class ProductsCrudControllerV1Test {
    @MockitoBean
    private ProductCrudService productCrudService;

    @Autowired
    private MockMvc mockMvc;

    private static ProductRequest sampleProduct() {
        return new ProductRequest("product-1", "Product 1", 19.99, 10);
    }

    private static String productRequestJson() {
        return """
                {
                  "name": "product-1",
                  "description": "Product 1",
                  "price": 19.99,
                  "quantity": 10
                }
                """;
    }

    @Test
    void getProducts_returnsProducts() throws Exception {
        when(productCrudService.getProducts()).thenReturn(List.of(sampleProduct()));

        mockMvc.perform(get("/api/v1/products"))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        [{
                          "name": "product-1",
                          "description": "Product 1",
                          "price": 19.99,
                          "quantity": 10
                        }]
                        """));
    }

    @Test
    void getProduct_returnsProductWhenFound() throws Exception {
        when(productCrudService.getProductByName("product-1")).thenReturn(Optional.of(sampleProduct()));

        mockMvc.perform(get("/api/v1/products/product-1"))
                .andExpect(status().isOk())
                .andExpect(content().json(productRequestJson()));
    }

    @Test
    void getProduct_returnsNotFoundWhenMissing() throws Exception {
        when(productCrudService.getProductByName("missing-product")).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/products/missing-product"))
                .andExpect(status().isNotFound());
    }

    @Test
    void createProduct_returnsCreatedProduct() throws Exception {
        ProductRequest product = sampleProduct();
        when(productCrudService.createProduct(product)).thenReturn(product);

        mockMvc.perform(post("/api/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(productRequestJson()))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "http://localhost/api/v1/products/product-1"))
                .andExpect(content().json(productRequestJson()));
    }

    @Test
    void updateProduct_returnsNoContentWhenUpdated() throws Exception {
        ProductRequest product = sampleProduct();
        when(productCrudService.updateProduct("product-1", product)).thenReturn(Optional.of(product));

        mockMvc.perform(put("/api/v1/products/product-1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(productRequestJson()))
                .andExpect(status().isNoContent())
                .andExpect(content().string(""));
    }

    @Test
    void updateProduct_returnsNotFoundWhenMissing() throws Exception {
        when(productCrudService.updateProduct("missing-product", sampleProduct())).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/v1/products/missing-product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(productRequestJson()))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteProduct_returnsNoContentWhenDeleted() throws Exception {
        when(productCrudService.deleteProduct("product-1")).thenReturn(true);

        mockMvc.perform(delete("/api/v1/products/product-1"))
                .andExpect(status().isNoContent())
                .andExpect(content().string(""));
    }

    @Test
    void deleteProduct_returnsNotFoundWhenMissing() throws Exception {
        when(productCrudService.deleteProduct("missing-product")).thenReturn(false);

        mockMvc.perform(delete("/api/v1/products/missing-product"))
                .andExpect(status().isNotFound());
    }
}
