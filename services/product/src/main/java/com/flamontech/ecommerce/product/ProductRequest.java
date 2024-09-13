package com.flamontech.ecommerce.product;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record ProductRequest(
        Integer id,

        @NotNull(message = "Product name is required.")
        String name,

        @NotNull(message = "Product description is required.")
        String description,

        @NotNull(message = "Product quantity is required.")
        @Positive(message = "Product quantity should be positive.")
        double quantity,

        @NotNull(message = "Product price is required.")
        @Positive(message = "Product price should be positive.")
        BigDecimal price,

        @NotNull(message = "Product Category is required.")
        Integer categoryId
) {

}
