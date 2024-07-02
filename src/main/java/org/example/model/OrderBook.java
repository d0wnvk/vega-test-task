package org.example.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class OrderBook {

    private Map<String, List<Order>> buyOrders;
    private Map<String, List<Order>> sellOrders;

    public OrderBook() {
        this.buyOrders = new HashMap<>();
        this.sellOrders = new HashMap<>();
    }

    public void addOrder(Order order) {
        List<Order> orders;

        if (order.getOrderType() == OrderType.BUY) {
            orders = buyOrders.computeIfAbsent(order.getTraderId(), k -> new ArrayList<>());
        } else {
            orders = sellOrders.computeIfAbsent(order.getTraderId(), k -> new ArrayList<>());
        }

        orders.add(order);
        matchOrders();
    }

    public void cancelOrder(String traderId, String orderId) {
        List<Order> orders = buyOrders.get(traderId);
        if (orders != null) {
            orders.removeIf(order -> order.getOrderId().equals(orderId));
        }

        orders = sellOrders.get(traderId);
        if (orders != null) {
            orders.removeIf(order -> order.getOrderId().equals(orderId));
        }
    }

    private void matchOrders() {
        for (String traderId : buyOrders.keySet()) {
            List<Order> buys = buyOrders.get(traderId);
            List<Order> sells = sellOrders.get(traderId);

            if (buys != null && sells != null) {
                Iterator<Order> buyIterator = buys.iterator();
                while (buyIterator.hasNext()) {
                    Order buyOrder = buyIterator.next();
                    Iterator<Order> sellIterator = sells.iterator();
                    while (sellIterator.hasNext()) {
                        Order sellOrder = sellIterator.next();
                        if (buyOrder.getPrice() >= sellOrder.getPrice()
                            && buyOrder.getQuantity() == sellOrder.getQuantity()) {
                            executeTrade(buyOrder, sellOrder);
                            buyIterator.remove();
                            sellIterator.remove();
                            break;
                        }
                    }
                }
            }
        }
    }

    private void executeTrade(Order buyOrder, Order sellOrder) {
        System.out.println("Trade executed: " + buyOrder.getOrderId() + " buys from " + sellOrder.getOrderId());
        // have a sell / buy match, do exchange properly here
    }

}
