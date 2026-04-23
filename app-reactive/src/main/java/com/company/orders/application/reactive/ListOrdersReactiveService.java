package com.company.orders.application.reactive;

import com.company.orders.application.reactive.port.in.ListOrdersReactiveUseCase;
import com.company.orders.domain.model.Order;
import com.company.orders.domain.port.out.OrderRepository;
import reactor.core.publisher.Flux;

public class ListOrdersReactiveService implements ListOrdersReactiveUseCase {

    private final OrderRepository orderRepository;

    public ListOrdersReactiveService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public Flux<Order> execute() {
        return orderRepository.findAll();
    }
}
