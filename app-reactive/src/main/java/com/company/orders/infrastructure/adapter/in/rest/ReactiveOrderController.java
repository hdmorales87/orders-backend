package com.company.orders.infrastructure.adapter.in.rest;

import com.company.orders.application.reactive.port.in.CreateOrderReactiveUseCase;
import com.company.orders.application.reactive.port.in.ListOrdersReactiveUseCase;
import com.company.orders.application.reactive.port.in.UpdateOrderReactiveUseCase;
import com.company.orders.application.reactive.port.in.DeleteOrderReactiveUseCase;
import com.company.orders.domain.model.Order;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;

import jakarta.validation.Valid;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/orders")
@CrossOrigin
public class ReactiveOrderController {

    private final CreateOrderReactiveUseCase createOrderReactiveUseCase;
    private final ListOrdersReactiveUseCase listOrdersReactiveUseCase;
    private final UpdateOrderReactiveUseCase updateOrderReactiveUseCase;
    private final DeleteOrderReactiveUseCase deleteOrderReactiveUseCase;

    public ReactiveOrderController(CreateOrderReactiveUseCase createOrderReactiveUseCase,
                                    ListOrdersReactiveUseCase listOrdersReactiveUseCase,
                                    UpdateOrderReactiveUseCase updateOrderReactiveUseCase,
                                    DeleteOrderReactiveUseCase deleteOrderReactiveUseCase) {
        this.createOrderReactiveUseCase = createOrderReactiveUseCase;
        this.listOrdersReactiveUseCase = listOrdersReactiveUseCase;
        this.updateOrderReactiveUseCase = updateOrderReactiveUseCase;
        this.deleteOrderReactiveUseCase = deleteOrderReactiveUseCase;
    }

    @PostMapping
    public Mono<Void> create(@Valid @RequestBody CreateOrderRequest request) {
        Order order = new Order(request.getName(), request.getTotal());
        return createOrderReactiveUseCase.execute(order);
    }

    @GetMapping
    public Flux<Order> list() {
        return listOrdersReactiveUseCase.execute();
    }

    @PutMapping("/{id}")
    public Mono<Void> update(@PathVariable Long id, @Valid @RequestBody UpdateOrderRequest request) {
        Order order = new Order(request.getName(), request.getTotal());
        return updateOrderReactiveUseCase.execute(id, order);
    }

    @DeleteMapping("/{id}")
    public Mono<Void> delete(@PathVariable Long id) {
        return deleteOrderReactiveUseCase.execute(id);
    }
}
