package com.grocerystore.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record Beer(
        @NotNull(message = "Beer type cannot be null")
        String type,
        @NotNull(message = "Beer quantity cannot be null")
        @Min(value = 1, message = "Beer quantity must be greater than zero")
        int quantity
){}