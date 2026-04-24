package com.company.orders.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.company.orders.application.CreateOrderService;
import com.company.orders.application.ListOrdersService;
import com.company.orders.application.UpdateOrderService;
import com.company.orders.application.DeleteOrderService;
import com.company.orders.application.port.in.CreateOrderUseCase;
import com.company.orders.application.port.in.ListOrdersUseCase;
import com.company.orders.application.port.in.UpdateOrderUseCase;
import com.company.orders.application.port.in.DeleteOrderUseCase;
import com.company.orders.domain.port.out.OrderRepository;
import com.company.orders.domain.port.out.AsyncMessageQueuePort;
import com.company.orders.domain.port.out.EmailNotificationPort;

@Configuration
public class BeanConfig {

    @Bean
    public CreateOrderUseCase createOrderUseCase(OrderRepository orderRepository,
                                               AsyncMessageQueuePort messageQueue,
                                               EmailNotificationPort emailNotification) {
        return new CreateOrderService(orderRepository, messageQueue, emailNotification);
    }

    @Bean
    public ListOrdersUseCase listOrdersUseCase(OrderRepository orderRepository) {
        return new ListOrdersService(orderRepository);
    }

    @Bean
    public UpdateOrderUseCase updateOrderUseCase(OrderRepository orderRepository) {
        return new UpdateOrderService(orderRepository);
    }

    @Bean
    public DeleteOrderUseCase deleteOrderUseCase(OrderRepository orderRepository) {
        return new DeleteOrderService(orderRepository);
    }
}
