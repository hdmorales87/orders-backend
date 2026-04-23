package com.company.orders.domain.port.out;

import com.company.orders.domain.model.Order;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface OrderRepository {
    Mono<Void> save(Order order);
    Flux<Order> findAll();
    Mono<Void> update(Long id, Order order);
    Mono<Void> delete(Long id);
}
