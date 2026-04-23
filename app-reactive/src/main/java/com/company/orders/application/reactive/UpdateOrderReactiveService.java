package com.company.orders.application.reactive;

import com.company.orders.application.reactive.port.in.UpdateOrderReactiveUseCase;
import com.company.orders.domain.model.Order;
import com.company.orders.domain.port.out.OrderRepository;
import reactor.core.publisher.Mono;

public class UpdateOrderReactiveService implements UpdateOrderReactiveUseCase {

    private final OrderRepository orderRepository;

    public UpdateOrderReactiveService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public Mono<Void> execute(Long id, Order order) {
        return orderRepository.update(id, order);
    }
}
