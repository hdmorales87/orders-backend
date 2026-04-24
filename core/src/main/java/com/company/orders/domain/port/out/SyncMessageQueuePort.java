package com.company.orders.domain.port.out;

public interface SyncMessageQueuePort {
    
    void sendMessage(String queueName, Object message);
    
    void sendMessage(String queueName, Object message, String routingKey);
}
