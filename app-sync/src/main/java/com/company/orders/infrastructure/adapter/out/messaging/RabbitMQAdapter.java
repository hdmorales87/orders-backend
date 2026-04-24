package com.company.orders.infrastructure.adapter.out.messaging;

import com.company.orders.domain.port.out.SyncMessageQueuePort;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class RabbitMQAdapter implements SyncMessageQueuePort {
    
    private final RabbitTemplate rabbitTemplate;
    
    public RabbitMQAdapter(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }
    
    @Override
    public void sendMessage(String queueName, Object message) {
        rabbitTemplate.convertAndSend(queueName, message);
    }
    
    @Override
    public void sendMessage(String queueName, Object message, String routingKey) {
        rabbitTemplate.convertAndSend(queueName, routingKey, message);
    }
}
