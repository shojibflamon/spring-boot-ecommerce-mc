package com.flamontech.ecommerce.customer;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record CustomerRequest(

        String id,

        @NotNull(message = "Customer firstname is required.")
        String firstName,

        @NotNull(message = "Customer lastname is required.")
        String lastName,

        @NotNull(message = "Customer email is required.")
        @Email(message = "Customer Email is not a valid email address.")
        String email,

        @NotNull(message = "Customer phone is required.")
        String phone,

        Address address
) {
}
