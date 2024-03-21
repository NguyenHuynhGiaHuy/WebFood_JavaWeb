package com.spring.oshaneat.payload.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CartRequest {
    @NotNull(message = "Food id is required")
    private int foodId;

    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Minimum quantity is 1")
    private int quantity;
}
