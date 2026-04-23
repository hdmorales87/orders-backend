package com.company.orders.application;

import com.company.orders.application.port.in.DeleteOrderUseCase;
import com.company.orders.domain.port.out.OrderRepository;

public class DeleteOrderService implements DeleteOrderUseCase {

    private final OrderRepository orderRepository;

    public DeleteOrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public void execute(Long id) {
        orderRepository.delete(id).block();
    }
}