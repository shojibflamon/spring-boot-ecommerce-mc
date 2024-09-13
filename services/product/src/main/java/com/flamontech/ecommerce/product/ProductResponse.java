package com.flamontech.ecommerce.product;

import java.math.BigDecimal;

public record ProductResponse(
        Integer Id,
        String name,
        String description,
        double quantity,
        BigDecimal price,
        Integer categoryId,
        String CategoryName,
        String CategoryDescription
) {
}
