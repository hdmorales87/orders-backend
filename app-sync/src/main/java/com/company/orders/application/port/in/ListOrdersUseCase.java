package com.company.orders.application.port.in;

import com.company.orders.domain.model.Order;

import java.util.List;

public interface ListOrdersUseCase {
    List<Order> execute();
}
