package com.company.orders.infrastructure.adapter.out.messaging;

import com.company.orders.domain.port.out.AsyncMessageQueuePort;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@Component
public class AsyncRabbitMQAdapter extends RabbitMQAdapter implements AsyncMessageQueuePort {
    
    private final RabbitTemplate rabbitTemplate;
    
    public AsyncRabbitMQAdapter(RabbitTemplate rabbitTemplate) {
        super(rabbitTemplate);
        this.rabbitTemplate = rabbitTemplate;
    }
    
    @Override
    public Mono<Void> sendMessageAndWaitConfirmation(String queueName, Object message) {
        return Mono.fromCallable(() -> {
            String correlationId = UUID.randomUUID().toString();
            String replyQueueName = queueName + ".reply";
            
            // Configurar reply queue
            rabbitTemplate.convertAndSend(queueName, message, msg -> {
                MessageProperties props = msg.getMessageProperties();
                props.setCorrelationId(correlationId);
                props.setReplyTo(replyQueueName);
                return msg;
            });
            
            // Esperar confirmación con timeout
            return waitForConfirmation(replyQueueName, correlationId);
        }).then();
    }
    
    @Override
    public Mono<Void> sendConfirmation(String replyQueueName, String correlationId) {
        return Mono.fromRunnable(() -> {
            rabbitTemplate.convertAndSend(replyQueueName, "CONFIRMED", msg -> {
                MessageProperties props = msg.getMessageProperties();
                props.setCorrelationId(correlationId);
                return msg;
            });
        });
    }
    
    private CompletableFuture<Void> waitForConfirmation(String replyQueueName, String correlationId) {
        CompletableFuture<Void> future = new CompletableFuture<>();
        
        // Listener temporal para esperar confirmación
        rabbitTemplate.execute(channel -> {
            String consumerTag = channel.basicConsume(replyQueueName, false, (consumerTag1, delivery) -> {
                String receivedCorrelationId = delivery.getProperties().getCorrelationId();
                if (correlationId.equals(receivedCorrelationId)) {
                    channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
                    future.complete(null);
                } else {
                    channel.basicNack(delivery.getEnvelope().getDeliveryTag(), false, true);
                }
            }, consumerTag1 -> {});
            
            // Timeout de 30 segundos
            CompletableFuture.delayedExecutor(30, TimeUnit.SECONDS).execute(() -> {
                if (!future.isDone()) {
                    future.completeExceptionally(new RuntimeException("Timeout waiting for confirmation"));
                }
            });
            
            return consumerTag;
        });
        
        return future;
    }
}
