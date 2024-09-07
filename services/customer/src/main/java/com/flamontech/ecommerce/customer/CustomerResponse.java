package com.flamontech.ecommerce.customer;

public record CustomerResponse(
        String id,
        String firstName,
        String lastName,
        String email,
        String phone,
        Address address
) {
}
