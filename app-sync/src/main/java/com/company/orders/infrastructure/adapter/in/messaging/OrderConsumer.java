package com.company.orders.infrastructure.adapter.in.messaging;

import com.company.orders.domain.port.out.AsyncMessageQueuePort;
import com.company.orders.domain.model.Order;
import com.company.orders.infrastructure.config.RabbitMQConfig;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.core.Message;
import org.springframework.stereotype.Component;

@Component
public class OrderConsumer {
    
    private final AsyncMessageQueuePort messageQueue;
    
    public OrderConsumer(AsyncMessageQueuePort messageQueue) {
        this.messageQueue = messageQueue;
    }
    
    @RabbitListener(queues = RabbitMQConfig.ORDER_NOTIFICATIONS_QUEUE)
    public void processOrder(Order order, Message message) {
        try {
            // Procesar la orden (aquí iría la lógica de negocio)
            System.out.println("Processing order: " + order.getName());
            
            // Simular procesamiento
            Thread.sleep(1000);
            
            // Enviar confirmación
            String correlationId = message.getMessageProperties().getCorrelationId();
            String replyQueue = message.getMessageProperties().getReplyTo();
            
            if (correlationId != null && replyQueue != null) {
                messageQueue.sendConfirmation(replyQueue, correlationId)
                    .subscribe(
                        result -> System.out.println("Confirmation sent for order: " + order.getName()),
                        error -> System.err.println("Error sending confirmation: " + error.getMessage())
                    );
            }
            
        } catch (Exception e) {
            System.err.println("Error processing order: " + e.getMessage());
        }
    }
}
