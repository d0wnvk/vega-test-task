package org.example.vega.model;

import java.util.Map;
import org.example.vega.exception.InsufficientFundsException;
import org.example.vega.exception.InsufficientStockException;

public class OrderBuy extends Order {

    public OrderBuy(String orderId,
                    Account account,
                    FinInstr cfi,
                    int quantity,
                    String traderId) {
        super(orderId,
              account,
              cfi,
              quantity,
              traderId);
    }

    /**
     * Invocation of this method will not change account's and market's states since affected temporary values only
     */
    @Override
    public void executeOrder(Order order) {
        validateAccountsBalance(order);
        try {
            updateAccount(order);
            updateMarket(order);
        } catch (Exception e) {
            // todo implement the rollback to preserve account's and market's state untouched
        }
    }

    @Override
    public void validate(Market market) {
        validateQuantityOfStock(market);

    }

    /**
     * Checks if FI has the requirement quantity available for the case when a client wants to buy a FI
     * for the sell case a client can provide any quantity, no need to validate quantity
     * */
    private void validateQuantityOfStock(Market market) {
        Integer marketQuantity = market.getStocks().get(getFinInstr());
        Integer quantityToBuy = getQuantity();
        if (marketQuantity - quantityToBuy < 0) {
            throw new InsufficientStockException("Not enough stock quantity. Client wants to buy: "
                                                     + quantityToBuy + ", but Market has: " + marketQuantity);
        }
    }

    private void validateAccountsBalance(Order order) {
        Account account = getAccount();
        double temporaryBalance = account.getTemporaryBalance();
        int quantityToBuy = order.getQuantity();
        double currentMarketPrice = order.getFinInstr().getCurrentMarketPrice();
        double totalCost = currentMarketPrice * quantityToBuy;

        if (temporaryBalance - totalCost <= 0) {
            throw new InsufficientFundsException("Not enough balance. Client has : "
                                                     + temporaryBalance + ", but needs: " + totalCost);
        }
    }

    private void updateAccount(Order order) {
        Account account = getAccount();
        double currentMarketPrice = order.getFinInstr().getCurrentMarketPrice();
        double temporaryBalance = account.getTemporaryBalance();
        int quantityToBuy = order.getQuantity();
        double totalCost = currentMarketPrice * quantityToBuy;

        updateTemporaryPortfolio(order, account, quantityToBuy);
        updateTemporaryBalance(account, temporaryBalance, totalCost);
    }

    private static void updateTemporaryPortfolio(Order order, Account account, int quantityToBuy) {
        FinInstr finInstr = order.getFinInstr();
        Map<FinInstr, Integer> temporaryPortfolio = account.getTemporaryPortfolio();
        temporaryPortfolio.merge(finInstr, quantityToBuy, Integer::sum);
    }

    private static void updateTemporaryBalance(Account account, double temporaryBalance, double totalCost) {
        account.setTemporaryBalance(temporaryBalance - totalCost);
    }

    private void updateMarket(Order order) {
        // to be implemented
    }
}
