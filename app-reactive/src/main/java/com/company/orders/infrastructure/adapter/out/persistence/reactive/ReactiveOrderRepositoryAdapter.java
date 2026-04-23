package com.company.orders.infrastructure.adapter.out.persistence.reactive;

import com.company.orders.domain.model.Order;
import com.company.orders.domain.port.out.OrderRepository;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class ReactiveOrderRepositoryAdapter implements OrderRepository {

    private final SpringDataReactiveOrderRepository springDataReactiveOrderRepository;

    public ReactiveOrderRepositoryAdapter(SpringDataReactiveOrderRepository springDataReactiveOrderRepository) {
        this.springDataReactiveOrderRepository = springDataReactiveOrderRepository;
    }

    @Override
    public Mono<Void> save(Order order) {
        OrderRow orderRow = new OrderRow();
        orderRow.setId(order.getId());
        orderRow.setName(order.getName());
        orderRow.setTotal(order.getTotal());
        return springDataReactiveOrderRepository.save(orderRow).then();
    }

    @Override
    public Mono<Void> update(Long id, Order order) {
        return springDataReactiveOrderRepository.findById(id)
                .flatMap(orderRow -> {
                    orderRow.setName(order.getName());
                    orderRow.setTotal(order.getTotal());
                    return springDataReactiveOrderRepository.save(orderRow);
                })
                .then();
    }

    @Override
    public Mono<Void> delete(Long id) {
        return springDataReactiveOrderRepository.deleteById(id);
    }

    @Override
    public Flux<Order> findAll() {
        return springDataReactiveOrderRepository.findAll()
                .map(orderRow -> new Order(orderRow.getId(), orderRow.getName(), orderRow.getTotal()));
    }
}
