package com.company.orders.infrastructure.config;

import com.company.orders.application.reactive.CreateOrderReactiveService;
import com.company.orders.application.reactive.ListOrdersReactiveService;
import com.company.orders.application.reactive.UpdateOrderReactiveService;
import com.company.orders.application.reactive.DeleteOrderReactiveService;
import com.company.orders.application.reactive.port.in.CreateOrderReactiveUseCase;
import com.company.orders.application.reactive.port.in.ListOrdersReactiveUseCase;
import com.company.orders.application.reactive.port.in.UpdateOrderReactiveUseCase;
import com.company.orders.application.reactive.port.in.DeleteOrderReactiveUseCase;
import com.company.orders.domain.port.out.OrderRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfig {

    @Bean
    public CreateOrderReactiveUseCase createOrderReactiveUseCase(OrderRepository orderRepository) {
        return new CreateOrderReactiveService(orderRepository);
    }

    @Bean
    public ListOrdersReactiveUseCase listOrdersReactiveUseCase(OrderRepository orderRepository) {
        return new ListOrdersReactiveService(orderRepository);
    }

    @Bean
    public UpdateOrderReactiveUseCase updateOrderReactiveUseCase(OrderRepository orderRepository) {
        return new UpdateOrderReactiveService(orderRepository);
    }

    @Bean
    public DeleteOrderReactiveUseCase deleteOrderReactiveUseCase(OrderRepository orderRepository) {
        return new DeleteOrderReactiveService(orderRepository);
    }
}
