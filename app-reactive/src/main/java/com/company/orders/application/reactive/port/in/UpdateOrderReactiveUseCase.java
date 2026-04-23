package com.company.orders.application.reactive.port.in;

import com.company.orders.domain.model.Order;
import reactor.core.publisher.Mono;

public interface UpdateOrderReactiveUseCase {
    Mono<Void> execute(Long id, Order order);
}
