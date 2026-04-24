package com.company.orders.infrastructure.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String ORDER_NOTIFICATIONS_QUEUE = "order.notifications";
    public static final String ORDER_NOTIFICATIONS_REPLY_QUEUE = "order.notifications.reply";
    public static final String ORDER_EXCHANGE = "orders.exchange";

    @Bean
    public TopicExchange ordersExchange() {
        return new TopicExchange(ORDER_EXCHANGE, true, false);
    }

    @Bean
    public Queue orderNotificationsQueue() {
        return QueueBuilder.durable(ORDER_NOTIFICATIONS_QUEUE).build();
    }

    @Bean
    public Queue orderNotificationsReplyQueue() {
        return QueueBuilder.durable(ORDER_NOTIFICATIONS_REPLY_QUEUE).build();
    }

    @Bean
    public Binding orderNotificationsBinding() {
        return BindingBuilder
                .bind(orderNotificationsQueue())
                .to(ordersExchange())
                .with("order.notification");
    }

    @Bean
    public Binding orderNotificationsReplyBinding() {
        return BindingBuilder
                .bind(orderNotificationsReplyQueue())
                .to(ordersExchange())
                .with("order.notification.reply");
    }

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
