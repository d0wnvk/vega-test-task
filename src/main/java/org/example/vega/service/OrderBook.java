package org.example.vega.service;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import org.example.vega.exception.NotFoundStockException;
import org.example.vega.model.Account;
import org.example.vega.model.FinInstr;
import org.example.vega.model.Market;
import org.example.vega.model.Order;

public class OrderBook {

    private List<Order> compositeOrder = null; // not thread safe

    private Market market;

    public void cancelOrder(Order orderToCancel) {
        // for that operation to be supported the compositeOrder object should support a thread-safe implementation
        compositeOrder.removeIf(order -> order.getOrderId().equals(orderToCancel.getOrderId()));
    }

    public List<Order> getCompositeOrder() {
        return compositeOrder;
    }

    public void addOCompositeOrder(List<Order> compositeOrder) {
        this.compositeOrder = compositeOrder;

    }

    public Market getMarket() {
        return market;
    }

    public void addMarket(Market market) {
        this.market = market;
    }

    public void processOrders() {
        orderValidation();
        executeOrders();
        updateAccountWithNewValues();
    }

    /**
     * Invocation of this method will change account's and market's states.
     */
    private void updateAccountWithNewValues() {
        Order order = compositeOrder.stream().findAny()
            .orElseThrow(() -> new NoSuchElementException("No composite orders found."));

        Account account = order.getAccount();
        double actualBalance = account.getBalance();
        double temporaryBalance = account.getTemporaryBalance();
        Map<FinInstr, Integer> actualPortfolio = account.getPortfolio();
        Map<FinInstr, Integer> temporaryPortfolio = account.getTemporaryPortfolio();

        // these kinds of operations should be processed in a transactional manner
        try {
            account.setBalance(temporaryBalance);
            account.setPortfolio(new HashMap<>(temporaryPortfolio));
        } catch (Exception e) {
            rollBackOrders(account, actualBalance, actualPortfolio, temporaryPortfolio);
            e.printStackTrace(); // todo should think about more robust logging
        }
    }

    private static void rollBackOrders(Account account, double actualBalance, Map<FinInstr, Integer> actualPortfolio,
                                       Map<FinInstr, Integer> temporaryPortfolio) {
        account.setBalance(actualBalance);
        HashMap<FinInstr, Integer> portfolio1 = new HashMap<>(actualPortfolio);
        account.setPortfolio(portfolio1);
        account.setTemporaryPortfolio(temporaryPortfolio);
    }

    private void executeOrders() {
        for (Order order : compositeOrder) {
            order.executeOrder(order);
        }
    }

    private void orderValidation() {
        validateMarketHasStock();
        validateMarketStockQuantity();
        // todo add check if there is a match between given orders to sell/buy and client's portfolio/balance content
    }

    private void validateMarketStockQuantity() {
        for (Order order : compositeOrder) {
            order.validate(market);
        }
    }

    private void validateMarketHasStock() {
        for (Order order : compositeOrder) {
            if (getMarket().hasNoStock(order.getFinInstr())) {
                throw new NotFoundStockException("This financial instrument in not presents on Market "
                                                     + "for any operations: " + order.getFinInstr());
            }
        }
    }

}
