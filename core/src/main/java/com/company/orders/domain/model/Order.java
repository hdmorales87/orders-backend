package com.company.orders.domain.model;

public class Order {
    private Long id;
    private String name;
    private double total;

    public Order(String name, double total) {
        this(null, name, total);
    }

    public Order(Long id, String name, double total) {
        if (total <= 0) throw new IllegalArgumentException();
        this.setId(id);
        this.setName(name);
        this.setTotal(total);
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public double getTotal() { return total; }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTotal(double total) {
        this.total = total;
    }
}
