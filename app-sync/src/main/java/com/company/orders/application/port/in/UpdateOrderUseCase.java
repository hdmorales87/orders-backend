package com.company.orders.application.port.in;

import com.company.orders.domain.model.Order;

public interface UpdateOrderUseCase {
    void execute(Long id, Order order);
}
