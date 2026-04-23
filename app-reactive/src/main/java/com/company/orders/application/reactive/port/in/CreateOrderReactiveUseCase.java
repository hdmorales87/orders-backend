package com.company.orders.application.reactive.port.in;

import com.company.orders.domain.model.Order;
import reactor.core.publisher.Mono;

public interface CreateOrderReactiveUseCase {
    Mono<Void> execute(Order order);
}
