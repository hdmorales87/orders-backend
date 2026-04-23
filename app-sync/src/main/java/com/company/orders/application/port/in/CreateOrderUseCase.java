package com.company.orders.application.port.in;

import com.company.orders.domain.model.Order;

public interface CreateOrderUseCase {
    void execute(Order order);
}
