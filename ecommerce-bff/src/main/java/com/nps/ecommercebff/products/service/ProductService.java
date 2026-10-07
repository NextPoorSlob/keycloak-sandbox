package com.nps.ecommercebff.products.service;

import com.nps.ecommercebff.products.model.ProductRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService implements ProductCrudService {
    @Override
    public List<ProductRequest> getProducts() {
        return List.of(
                new ProductRequest("product-1", "Product 1", 19.99, 10),
                new ProductRequest("product-2", "Product 2", 29.99, 20),
                new ProductRequest("product-3", "Product 3", 39.99, 30),
                new ProductRequest("product-4", "Product 4", 49.99, 40),
                new ProductRequest("product-5", "Product 5", 59.99, 50)
        );
    }

    @Override
    public Optional<ProductRequest> getProductByName(String productName) {
        return getProducts().stream()
                .filter(product -> product.name().equals(productName))
                .findFirst();
    }

    @Override
    public ProductRequest createProduct(ProductRequest product) {
        return product;
    }

    @Override
    public Optional<ProductRequest> updateProduct(String productName, ProductRequest product) {
        if (getProductByName(productName).isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(product);
    }

    @Override
    public boolean deleteProduct(String productName) {
        return getProductByName(productName).isPresent();
    }
}
