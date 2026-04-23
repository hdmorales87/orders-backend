package com.company.orders.application.reactive;

import com.company.orders.application.reactive.port.in.CreateOrderReactiveUseCase;
import com.company.orders.domain.model.Order;
import com.company.orders.domain.port.out.OrderRepository;
import reactor.core.publisher.Mono;

public class CreateOrderReactiveService implements CreateOrderReactiveUseCase {

    private final OrderRepository orderRepository;

    public CreateOrderReactiveService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public Mono<Void> execute(Order order) {
        return orderRepository.save(order);
    }
}
