package org.example.ver01;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.example.vega.exception.InsufficientFundsException;
import org.example.vega.exception.InsufficientStockException;
import org.example.vega.exception.NotFoundStockException;
import org.example.vega.model.Account;
import org.example.vega.model.FinInstr;
import org.example.vega.model.Market;
import org.example.vega.model.Order;
import org.example.vega.model.OrderBook;
import org.example.vega.model.OrderBuy;
import org.example.vega.model.OrderSell;
import org.junit.jupiter.api.Test;

// todo delete User

class VegaTradingSystemTest {

    @Test
    void givenScenario01WhenTradeThenDoAssertion() {
        // this scenario is intended to
        // set up financial instruments that are available to a client and for a market to buy/sell
        // put some of them to a market to be able to buy / sell them
        // put some of them to a clients portfolio to enable a client to sell them
        // put some order to buy / sell stocks to an order book
        // a client is represented by an account

        // In this particular scenario_01 a client is going place a composite order
        // which consists of one operation.
        // The first operation is to sell one stockD.
        // As a result there will happen two actions
        // 1 - the client will have one stock less
        // 2 - it's account's balance will gain a revenue by this stock market's price

        // create financial instruments to operate with
        FinInstr stockA = new FinInstr("A", "A", 1000.00);
        FinInstr stockB = new FinInstr("B", "B", 2000.00);
        FinInstr stockC = new FinInstr("C", "C", 3000.00);
        FinInstr stockD = new FinInstr("D", "D", 4000.00);
        FinInstr stockE = new FinInstr("E", "E", 5000.00);

        // market's snapshot -> name of a stock and its quantity available to buy from market
        Market market1 = new Market();
        market1.addStock(stockA, 10);
        market1.addStock(stockB, 20);
        market1.addStock(stockC, 30);
        market1.addStock(stockD, 40);


        // set up account's portfolio and balance
        double initialBalance = 10000.00;
        Account account = new Account("accoundId_01", initialBalance);
        Map<FinInstr, Integer> portfolio = new HashMap<>();
        int stockDportfolioQuantity = 1;
        portfolio.put(stockD, stockDportfolioQuantity);
        account.setPortfolio(portfolio);

        // set up an orders for stock's buy / sell
        int stockDquantityToSell = 1;
        Order order01 = new OrderSell("orderId_01", account,
                                      stockD, stockDquantityToSell, "traderId_01");

        // construct composite order
        List<Order> compositeOrder = List.of(order01);

        // set an order book up
        OrderBook orderBook = new OrderBook();
        orderBook.addOCompositeOrder(compositeOrder);
        orderBook.addMarket(market1);

        // when
        orderBook.processOrders();

        // then
        Map<FinInstr, Integer> updatedPortfolio = account.getPortfolio();
        Integer stockCount = updatedPortfolio.get(stockD);
        assertEquals(stockCount, stockDportfolioQuantity - stockDquantityToSell); // '-' because it's a sale

        double updateBalance = account.getBalance();
        assertEquals(updateBalance, initialBalance + stockD.getCurrentMarketPrice() * stockDquantityToSell);
    }

    @Test
    void givenScenario02WhenTradeThenDoAssertion() {
        // this scenario is intended to
        // set up financial instruments that are available to a client and for a market to buy/sell
        // put some of them to a market to be able to buy / sell them
        // put some of them to a clients portfolio to enable a client to sell them
        // put some order to buy / sell stocks to an order book
        // a client is represented by an account

        // In this particular scenario_02 a client is going place a composite order
        // which consists of two operations.
        // The first operation is to sell one stockD.
        // As a result there will happen two actions
        // 1 - the client will have one stock less
        // 2 - it's account's balance will gain a revenue by this stock market's price
        // The second operation is to buy 4 stockC
        // As a result there will happen two actions
        // 1 - the client will have four stock more
        // 2 - it's account's balance will decrease

        // create financial instruments to operate with
        FinInstr stockA = new FinInstr("A", "A", 1000.00);
        FinInstr stockB = new FinInstr("B", "B", 2000.00);
        FinInstr stockC = new FinInstr("C", "C", 3000.00);
        FinInstr stockD = new FinInstr("D", "D", 4000.00);
        FinInstr stockE = new FinInstr("E", "E", 5000.00);

        // market's snapshot -> name of a stock and its quantity available to buy from market
        Market market1 = new Market();
        market1.addStock(stockA, 10);
        market1.addStock(stockB, 20);
        market1.addStock(stockC, 30);
        market1.addStock(stockD, 40);

        // set up account's portfolio and balance
        double initialBalance = 10000.00;
        Account account = new Account("accoundId_01", initialBalance);
        Map<FinInstr, Integer> portfolio = new HashMap<>();
        int stockCportfolioQuantity = 5;
        portfolio.put(stockC, stockCportfolioQuantity);
        int stockDportfolioQuantity = 1;
        portfolio.put(stockD, stockDportfolioQuantity);
        account.setPortfolio(portfolio);

        // set up an orders for stock's buy / sell
        int stockDquantityToSell = 1;
        Order order01 = new OrderSell("orderId_01", account,
                                         stockD, stockDquantityToSell, "traderId_01");
        int stockCquantityToBuy = 4;
        Order order02 = new OrderBuy("orderId_02", account,
                                     stockC, stockCquantityToBuy, "traderId_01");

        // construct composite order
        List<Order> compositeOrder = List.of(order01, order02);

        // set an order book up
        OrderBook orderBook = new OrderBook();
        orderBook.addOCompositeOrder(compositeOrder);
        orderBook.addMarket(market1);

        // when
        orderBook.processOrders();

        // then
        Map<FinInstr, Integer> updatedPortfolio = account.getPortfolio();
        Integer stockCcount = updatedPortfolio.get(stockC);
        Integer stockDcount = updatedPortfolio.get(stockD);
        assertEquals(stockCcount, stockCportfolioQuantity + stockCquantityToBuy); // '+' because it's a purchase
        assertEquals(stockDcount, stockDportfolioQuantity - stockDquantityToSell); // '-' because it's a sale

        double updatedBalance = account.getBalance();
        double stockDsellPrice = stockDquantityToSell * stockD.getCurrentMarketPrice();
        double buyPrice = stockCquantityToBuy * stockC.getCurrentMarketPrice();
        assertEquals(updatedBalance, initialBalance + stockDsellPrice - buyPrice);
    }

    @Test
    void givenOrderToBuyTooManyAssetsWhenExecuteOrderThenThrowInsufficientStockException() {
        // this scenario is intended to
        // try to buy / sell too much stocks

        // create financial instruments to operate with
        FinInstr stockA = new FinInstr("A", "A", 1000.00);
        FinInstr stockB = new FinInstr("B", "B", 2000.00);
        FinInstr stockC = new FinInstr("C", "C", 3000.00);
        FinInstr stockD = new FinInstr("D", "D", 4000.00);
        FinInstr stockE = new FinInstr("E", "E", 5000.00);

        // market's snapshot -> name of a stock and its quantity available to buy from market
        Market market1 = new Market();
        market1.addStock(stockA, 10);
        market1.addStock(stockB, 20);
        market1.addStock(stockC, 30);
        market1.addStock(stockD, 40);

        // set up account's portfolio and balance
        double initialBalance = 10000.00;
        Account account = new Account("accoundId_01", initialBalance);
        Map<FinInstr, Integer> portfolio = new HashMap<>();
        int stockCportfolioQuantity = 5;
        portfolio.put(stockC, stockCportfolioQuantity);
        int stockDportfolioQuantity = 1;
        portfolio.put(stockD, stockDportfolioQuantity);
        account.setPortfolio(portfolio);

        // set up an orders for stock's buy / sell
        int stockDquantityToSell = 1;
        Order order01 = new OrderSell("orderId_01", account,
                                         stockD, stockDquantityToSell, "traderId_01");
        int stockCquantityToBuy = 40;
        Order order02 = new OrderBuy("orderId_02", account,
                                         stockC, stockCquantityToBuy, "traderId_01");

        // construct composite order
        List<Order> compositeOrder = List.of(order01, order02);

        // set an order book up
        OrderBook orderBook = new OrderBook();
        orderBook.addOCompositeOrder(compositeOrder);
        orderBook.addMarket(market1);

        // when and then
        assertThrows(InsufficientStockException.class, orderBook::processOrders);

    }

    @Test
    void givenOrderToBuyTooManyAssetsWhenExecuteOrderThenThrowInsufficientFundsException() {
        // this scenario is intended to
        // try to buy stocks with not enough account's balance value

        // create financial instruments to operate with
        FinInstr stockA = new FinInstr("A", "A", 1000.00);
        FinInstr stockB = new FinInstr("B", "B", 2000.00);
        FinInstr stockC = new FinInstr("C", "C", 3000.00);
        FinInstr stockD = new FinInstr("D", "D", 4000.00);
        FinInstr stockE = new FinInstr("E", "E", 5000.00);

        // market's snapshot -> name of a stock and its quantity available to buy from market
        Market market1 = new Market();
        market1.addStock(stockA, 10);
        market1.addStock(stockB, 20);
        market1.addStock(stockC, 30);
        market1.addStock(stockD, 40);

        // set up account's portfolio and balance
        double initialBalance = 10.00;
        Account account = new Account("accoundId_01", initialBalance);
        Map<FinInstr, Integer> portfolio = new HashMap<>();
        int stockCportfolioQuantity = 5;
        portfolio.put(stockC, stockCportfolioQuantity);
        int stockDportfolioQuantity = 1;
        portfolio.put(stockD, stockDportfolioQuantity);
        account.setPortfolio(portfolio);

        // set up an orders for stock's buy / sell
        int stockDquantityToSell = 1;
        Order order01 = new OrderSell("orderId_01", account,
                                         stockD, stockDquantityToSell, "traderId_01");
        int stockCquantityToBuy = 4;
        Order order02 = new OrderBuy("orderId_02", account,
                                         stockC, stockCquantityToBuy, "traderId_01");

        // construct composite order
        List<Order> compositeOrder = List.of(order01, order02);

        // set an order book up
        OrderBook orderBook = new OrderBook();
        orderBook.addOCompositeOrder(compositeOrder);
        orderBook.addMarket(market1);

        // when and then
        assertThrows(InsufficientFundsException.class, orderBook::processOrders);
    }

    @Test
    void givenNotExistingFinInstrWhenExecuteOrderThenThrowNotFoundStockException() {
        // this scenario is intended to
        // try to sell / buy stocks that are not available on the market

        // create financial instruments to operate with
        FinInstr stockA = new FinInstr("A", "A", 1000.00);
        FinInstr stockB = new FinInstr("B", "B", 2000.00);
        FinInstr stockC = new FinInstr("C", "C", 3000.00);
        FinInstr stockD = new FinInstr("D", "D", 4000.00);
        FinInstr stockE = new FinInstr("E", "E", 5000.00);

        // market's snapshot -> name of a stock and its quantity available to buy from market
        Market market1 = new Market();
        market1.addStock(stockA, 10);
        market1.addStock(stockB, 20);
        market1.addStock(stockC, 30);
        market1.addStock(stockD, 40);

        // set up account's portfolio and balance
        double initialBalance = 10.00;
        Account account = new Account("accoundId_01", initialBalance);
        Map<FinInstr, Integer> portfolio = new HashMap<>();
        int stockCportfolioQuantity = 5;
        portfolio.put(stockC, stockCportfolioQuantity);
        int stockDportfolioQuantity = 1;
        portfolio.put(stockD, stockDportfolioQuantity);
        account.setPortfolio(portfolio);

        // set up an orders for stock's buy / sell
        int stockDquantityToSell = 1;
        Order order01 = new OrderSell("orderId_01", account,
                                         stockE, stockDquantityToSell, "traderId_01");
        int stockCquantityToBuy = 4;
        Order order02 = new OrderBuy("orderId_02", account,
                                         stockC, stockCquantityToBuy, "traderId_01");

        // construct composite order
        List<Order> compositeOrder = List.of(order01, order02);

        // set an order book up
        OrderBook orderBook = new OrderBook();
        orderBook.addOCompositeOrder(compositeOrder);
        orderBook.addMarket(market1);

        // when and then
        assertThrows(NotFoundStockException.class, orderBook::processOrders);
    }

    @Test
    void givenTwoOrdersInOrderBookWhenCancelOrderThenRemoveOrderFromOrderBook() {
        // this scenario is intended to
        // provide a thread-safe implementation of List that synchronizes each individual operation
        // to be able to delete Orders from Order's List

        // create financial instruments to operate with
        FinInstr stockC = new FinInstr("C", "C", 3000.00);
        FinInstr stockD = new FinInstr("D", "D", 4000.00);

        // set up account's portfolio and balance
        double initialBalance = 10.00;
        Account account = new Account("accoundId_01", initialBalance);

        // set up an orders for stock's buy / sell
        int stockDquantityToSell = 1;
        Order order01 = new OrderSell("orderId_01", account,
                                         stockD, stockDquantityToSell, "traderId_01");
        int stockCquantityToBuy = 4;
        Order order02 = new OrderBuy("orderId_02", account,
                                         stockC, stockCquantityToBuy, "traderId_01");

        // construct composite order
        List<Order> compositeOrder = Collections.synchronizedList(new ArrayList<>());
        compositeOrder.add(order01);
        compositeOrder.add(order02);

        // set an order book up
        OrderBook orderBook = new OrderBook();
        orderBook.addOCompositeOrder(compositeOrder);

        // when and then
        orderBook.cancelOrder(order02);
        assertEquals(orderBook.getCompositeOrder().size(), 1);
    }

}
