package org.example;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;
import org.example.model.CompositeFinancialInstrument;
import org.example.model.FinancialInstrument;
import org.example.model.Order;
import org.example.model.OrderBook;
import org.example.model.OrderType;
import org.junit.jupiter.api.Test;


class VegaApplicationTest {

    @Test
    void test(){

        // Sample financial instruments
        FinancialInstrument stockA = new FinancialInstrument("1", "AAPL", 150.00);
        FinancialInstrument stockB = new FinancialInstrument("2", "GOOGL", 2500.00);

        List<FinancialInstrument> components = Arrays.asList(stockA, stockB);
        CompositeFinancialInstrument compositeInstrument = new CompositeFinancialInstrument("3", "TECH", components);

        OrderBook orderBook = new OrderBook();

        Order buyOrder = new Order("1", "Trader1", OrderType.BUY, 2000.00, 1);
        Order sellOrder = new Order("2", "Trader1", OrderType.SELL, 2000.00, 1);

        orderBook.addOrder(buyOrder);
        orderBook.addOrder(sellOrder);

        // Update composite instrument price and display
        compositeInstrument.updatePrice();
        System.out.println("Composite Instrument Price: " + compositeInstrument.getCurrentMarketPrice());

    }

}
