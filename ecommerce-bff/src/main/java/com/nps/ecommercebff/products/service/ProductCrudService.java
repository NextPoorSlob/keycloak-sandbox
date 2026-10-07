package com.nps.ecommercebff.products.service;

import com.nps.ecommercebff.products.model.ProductRequest;

import java.util.List;
import java.util.Optional;

/**
 * Defines the contract for performing CRUD operations on products in the e-commerce application.
 */
public interface ProductCrudService {
    /**
     * Retrieves all products in the e-commerce application.
     * @return a list of products.
     */
    List<ProductRequest> getProducts();

    /**
     * Retrieves a product by its unique name.
     * @param productName the unique name of the product.
     * @return an Optional containing the product if found, or empty if not found.
     */
    Optional<ProductRequest> getProductByName(String productName);

    /**
     * Creates a product in the e-commerce application.
     * @param product the product to create.
     * @return the created product.
     */
    ProductRequest createProduct(ProductRequest product);

    /**
     * Updates an existing product.
     * @param productName the unique name of the product to update.
     * @param product the updated product details.
     * @return an Optional containing the updated product if found, or empty if not found.
     */
    Optional<ProductRequest> updateProduct(String productName, ProductRequest product);

    /**
     * Deletes a product by its unique name.
     * @param productName the unique name of the product to delete.
     * @return true if the product was deleted, false if it was not found.
     */
    boolean deleteProduct(String productName);
}
