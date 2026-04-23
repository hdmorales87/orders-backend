package com.company.orders.application;

import com.company.orders.application.port.in.CreateOrderUseCase;
import com.company.orders.domain.model.Order;
import com.company.orders.domain.port.out.OrderRepository;

public class CreateOrderService implements CreateOrderUseCase {

    private final OrderRepository orderRepository;

    public CreateOrderService(OrderRepository repo) {
        this.orderRepository = repo;
    }

    @Override
    public void execute(Order order) {
        orderRepository.save(order).block();
    }
}
