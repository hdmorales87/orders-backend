package com.company.orders.domain.port.out;

import reactor.core.publisher.Mono;

public interface AsyncMessageQueuePort extends MessageQueuePort {
    
    Mono<Void> sendMessageAndWaitConfirmation(String queueName, Object message);
    
    Mono<Void> sendConfirmation(String replyQueueName, String correlationId);
}
