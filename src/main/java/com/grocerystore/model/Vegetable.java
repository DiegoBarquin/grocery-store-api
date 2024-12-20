package com.grocerystore.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record Vegetable(
        String name,
        @NotNull(message = "Vegetables quantity cannot be null")
        @Positive(message = "Vegetables quantity must be positive")
        double weightInGrams
) {}