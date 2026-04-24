package com.company.orders.domain.port.out;

import reactor.core.publisher.Mono;

public interface EmailNotificationPort {
    
    Mono<Void> sendEmail(String to, String subject, String body);
    
    Mono<Void> sendOrderNotification(String to, String orderName, Double total);
}
