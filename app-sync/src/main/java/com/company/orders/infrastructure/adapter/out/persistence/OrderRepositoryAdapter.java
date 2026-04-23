package com.company.orders.infrastructure.adapter.out.persistence;

import com.company.orders.domain.model.Order;
import com.company.orders.domain.port.out.OrderRepository;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class OrderRepositoryAdapter implements OrderRepository {

    private final SpringDataOrderRepository springDataOrderRepository;

    public OrderRepositoryAdapter(SpringDataOrderRepository springDataOrderRepository) {
        this.springDataOrderRepository = springDataOrderRepository;
    }

    @Override
    public Mono<Void> save(Order order) {
        return Mono.fromRunnable(() -> {
            OrderEntity orderEntity = new OrderEntity();
            orderEntity.setId(order.getId());
            orderEntity.setName(order.getName());
            orderEntity.setTotal(order.getTotal());
            springDataOrderRepository.save(orderEntity);
        });
    }

    @Override
    public Flux<Order> findAll() {
        List<Order> orders = springDataOrderRepository.findAll().stream()
                .map(orderEntity -> new Order(orderEntity.getId(), orderEntity.getName(), orderEntity.getTotal()))
                .toList();
        return Flux.fromIterable(orders);
    }

    @Override
    public Mono<Void> update(Long id, Order order) {
        return Mono.fromRunnable(() -> {
            OrderEntity orderEntity = springDataOrderRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Order not found"));
            orderEntity.setName(order.getName());
            orderEntity.setTotal(order.getTotal());
            springDataOrderRepository.save(orderEntity);
        });
    }

    @Override
    public Mono<Void> delete(Long id) {
        return Mono.fromRunnable(() -> springDataOrderRepository.deleteById(id));
    }
}
