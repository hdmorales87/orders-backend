package com.company.orders.application.reactive.port.in;

import com.company.orders.domain.model.Order;
import reactor.core.publisher.Flux;

public interface ListOrdersReactiveUseCase {
    Flux<Order> execute();
}
