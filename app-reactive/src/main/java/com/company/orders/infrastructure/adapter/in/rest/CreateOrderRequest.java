package com.company.orders.infrastructure.adapter.in.rest;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public class CreateOrderRequest {
    @NotBlank(message = "Name cannot be blank")
    private String name;

    @Positive(message = "Total must be positive")
    private double total;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }
}