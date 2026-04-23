package com.company.orders.application;

import com.company.orders.application.port.in.ListOrdersUseCase;
import com.company.orders.domain.model.Order;
import com.company.orders.domain.port.out.OrderRepository;

import java.util.List;

public class ListOrdersService implements ListOrdersUseCase {

    private final OrderRepository orderRepository;

    public ListOrdersService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public List<Order> execute() {
        return orderRepository.findAll().collectList().block();
    }
}
