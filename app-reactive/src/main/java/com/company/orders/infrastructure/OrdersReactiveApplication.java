package com.company.orders.infrastructure;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.company.orders")
public class OrdersReactiveApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrdersReactiveApplication.class, args);
    }
}
