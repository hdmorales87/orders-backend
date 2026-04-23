package com.company.orders.application;

import com.company.orders.application.port.in.UpdateOrderUseCase;
import com.company.orders.domain.model.Order;
import com.company.orders.domain.port.out.OrderRepository;

public class UpdateOrderService implements UpdateOrderUseCase {

    private final OrderRepository orderRepository;

    public UpdateOrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public void execute(Long id, Order order) {
        orderRepository.update(id, order).block();
    }
}
