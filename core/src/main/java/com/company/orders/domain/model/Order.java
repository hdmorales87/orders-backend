package com.company.orders.domain.model;

public class Order {
    private Long id;
    private String name;
    private double total;
    private int statusId;

    // Default constructor for Jackson deserialization
    public Order() {
    }

    public Order(String name, double total) {
        this(null, name, total, 0);
    }

    public Order(Long id, String name, double total, int statusId) {
        if (total <= 0) throw new IllegalArgumentException();
        this.setId(id);
        this.setName(name);
        this.setTotal(total);
        this.setStatusId(statusId);
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public double getTotal() { return total; }
    public int getStatusId() { return statusId; }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTotal(double total) {
        this.total = total;
    }
    
    public void setStatusId(int statusId) {
        this.statusId = statusId;
    }
}
