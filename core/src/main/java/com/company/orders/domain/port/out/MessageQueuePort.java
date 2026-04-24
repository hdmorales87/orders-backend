package com.company.orders.domain.port.out;

import reactor.core.publisher.Mono;

public interface MessageQueuePort {
    
    Mono<Void> sendMessage(String queueName, Object message);
    
    Mono<Void> sendMessage(String queueName, Object message, String routingKey);
}
