package com.nps.ecommercebff.products.model;

public record ProductRequest(
    String name,
    String description,
    double price,
    int quantity
) { }