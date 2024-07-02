package org.example.model;

public class Order {

    private String orderId;
    private String traderId;
    private OrderType orderType;
    private double price;
    private int quantity;

    public Order(String orderId, String traderId, OrderType orderType, double price, int quantity) {
        this.orderId = orderId;
        this.traderId = traderId;
        this.orderType = orderType;
        this.price = price;
        this.quantity = quantity;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getTraderId() {
        return traderId;
    }

    public OrderType getOrderType() {
        return orderType;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

}
