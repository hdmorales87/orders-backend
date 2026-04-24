package com.company.orders.application.reactive;

import com.company.orders.application.reactive.port.in.CreateOrderReactiveUseCase;
import com.company.orders.domain.model.Order;
import com.company.orders.domain.config.NotificationsConfig;
import com.company.orders.domain.port.out.OrderRepository;
import com.company.orders.domain.port.out.AsyncMessageQueuePort;
import com.company.orders.domain.port.out.EmailNotificationPort;
import com.company.orders.infrastructure.config.RabbitMQConfig;
import reactor.core.publisher.Mono;

public class CreateOrderReactiveService implements CreateOrderReactiveUseCase {

    private final OrderRepository orderRepository;
    private final AsyncMessageQueuePort messageQueue;
    private final EmailNotificationPort emailNotification;

    public CreateOrderReactiveService(OrderRepository orderRepository, 
                                    AsyncMessageQueuePort messageQueue,
                                    EmailNotificationPort emailNotification) {
        this.orderRepository = orderRepository;
        this.messageQueue = messageQueue;
        this.emailNotification = emailNotification;
    }

    @Override
    public Mono<Void> execute(Order order) {
        return orderRepository.save(order)
            .then(messageQueue.sendMessageAndWaitConfirmation(RabbitMQConfig.ORDER_NOTIFICATIONS_QUEUE, order))
            .then(emailNotification.sendOrderNotification(NotificationsConfig.DEFAULT_CUSTOMER_EMAIL, order.getName(), order.getTotal()))
            .onErrorContinue((error, throwable) -> {
                System.err.println("Error in order processing: " + error.getMessage());
            });
    }
}
