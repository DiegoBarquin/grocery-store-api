package com.grocerystore.model;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record Bread(
        @NotNull(message = "Bread quantity cannot be null")
        @Min(value = 1, message = "Bread quantity must be greater than zero")
        int quantity,
        @NotNull(message = "Bread age in days cannot be null")
        @Min(value = 0, message = "Bread age in days cannot be negative")
        @Max(value = 6, message = "Bread too old to sell")
        int ageInDays
){}