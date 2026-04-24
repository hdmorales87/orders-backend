package com.company.orders.infrastructure.adapter.out.messaging;

import com.company.orders.domain.port.out.MessageQueuePort;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class RabbitMQAdapter implements MessageQueuePort {
    
    private final RabbitTemplate rabbitTemplate;
    
    public RabbitMQAdapter(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }
    
    @Override
    public Mono<Void> sendMessage(String queueName, Object message) {
        return Mono.fromRunnable(() -> {
            rabbitTemplate.convertAndSend(queueName, message);
        });
    }
    
    @Override
    public Mono<Void> sendMessage(String queueName, Object message, String routingKey) {
        return Mono.fromRunnable(() -> {
            rabbitTemplate.convertAndSend(queueName, routingKey, message);
        });
    }
}
