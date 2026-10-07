package com.nps.ecommercebff.products.controller;

import com.nps.ecommercebff.common.exception.ResourceNotFoundException;
import com.nps.ecommercebff.products.model.ProductRequest;
import com.nps.ecommercebff.products.service.ProductCrudService;
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
 * Provides CRUD operations for managing products in the e-commerce application.
 */
@RestController
@RequestMapping("/api/v1/products")
@Tag(name = "Products", description = "Operations for managing products")
public class ProductsCrudControllerV1 {
    public static final String PRODUCT_NOT_FOUND = "Product [%s] not found.";
    private final ProductCrudService productService;

    public ProductsCrudControllerV1(ProductCrudService productService) {
        this.productService = productService;
    }

    @GetMapping
    @Operation(summary = "List products", description = "Returns all products.")
    @ApiResponse(responseCode = "200", description = "Products returned successfully")
    public ResponseEntity<List<ProductRequest>> getProducts() {
        return ResponseEntity.ok(productService.getProducts());
    }

    @GetMapping("/{productName}")
    @Operation(summary = "Get a product", description = "Returns a product by its name.")
    @SuppressWarnings("squid:S1710")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Product returned successfully"),
            @ApiResponse(responseCode = "404", description = "Product not found", content = @Content)
    })
    public ResponseEntity<ProductRequest> getProduct(
            @Parameter(description = "Name of the product to retrieve", required = true)
            @PathVariable String productName) {
        return productService.getProductByName(productName)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(PRODUCT_NOT_FOUND, productName)));
    }

    @PostMapping
    @Operation(summary = "Create a product", description = "Creates a product and returns its location.")
    @SuppressWarnings("squid:S1710")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Product created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid product request", content = @Content)
    })
    public ResponseEntity<ProductRequest> createProduct(@RequestBody ProductRequest product) {
        ProductRequest createdProduct = productService.createProduct(product);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{productName}")
                .buildAndExpand(createdProduct.name())
                .toUri();
        return ResponseEntity.created(location).body(createdProduct);
    }

    @PutMapping("/{productName}")
    @Operation(summary = "Update a product", description = "Replaces an existing product by its name.")
    @SuppressWarnings("squid:S1710")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Product updated successfully", content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid product request", content = @Content),
            @ApiResponse(responseCode = "404", description = "Product not found", content = @Content)
    })
    public ResponseEntity<Void> updateProduct(
            @Parameter(description = "Name of the product to update", required = true)
            @PathVariable String productName,
            @RequestBody ProductRequest product) {
        productService.updateProduct(productName, product)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(PRODUCT_NOT_FOUND, productName)));
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{productName}")
    @Operation(summary = "Delete a product", description = "Deletes a product by its name.")
    @SuppressWarnings("squid:S1710")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Product deleted successfully", content = @Content),
            @ApiResponse(responseCode = "404", description = "Product not found", content = @Content)
    })
    public ResponseEntity<Void> deleteProduct(
            @Parameter(description = "Name of the product to delete", required = true)
            @PathVariable String productName) {
        if (!productService.deleteProduct(productName)) {
            throw new ResourceNotFoundException(String.format(PRODUCT_NOT_FOUND, productName));
        }
        return ResponseEntity.noContent().build();
    }
}
