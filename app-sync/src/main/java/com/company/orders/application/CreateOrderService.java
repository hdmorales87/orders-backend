package com.company.orders.application;

import com.company.orders.application.port.in.CreateOrderUseCase;
import com.company.orders.domain.model.Order;
import com.company.orders.domain.config.NotificationsConfig;
import com.company.orders.domain.port.out.OrderRepository;
import com.company.orders.domain.port.out.AsyncMessageQueuePort;
import com.company.orders.domain.port.out.EmailNotificationPort;
import com.company.orders.infrastructure.config.RabbitMQConfig;

public class CreateOrderService implements CreateOrderUseCase {

    private final OrderRepository orderRepository;
    private final AsyncMessageQueuePort messageQueue;
    private final EmailNotificationPort emailNotification;

    public CreateOrderService(OrderRepository orderRepository, 
                             AsyncMessageQueuePort messageQueue,
                             EmailNotificationPort emailNotification) {
        this.orderRepository = orderRepository;
        this.messageQueue = messageQueue;
        this.emailNotification = emailNotification;
    }

    @Override
    public void execute(Order order) {
        try {
            // Guardar orden
            orderRepository.save(order).block();
            
            // Enviar mensaje a cola y esperar confirmación
            messageQueue.sendMessageAndWaitConfirmation(RabbitMQConfig.ORDER_NOTIFICATIONS_QUEUE, order)
                .then(emailNotification.sendOrderNotification(NotificationsConfig.DEFAULT_CUSTOMER_EMAIL, order.getName(), order.getTotal()))
                .block();
                
        } catch (Exception e) {
            System.err.println("Error in order processing: " + e.getMessage());
            throw new RuntimeException("Failed to process order", e);
        }
    }
}
