package org.example.vega.model;

import java.util.Map;

public class OrderSell extends Order {

    public OrderSell(String orderId,
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
        try {
            updateAccount(order);
            updateMarket(order);
        } catch (Exception e) {
            // todo implement the rollback to preserve account's and market's state untouched
        }
    }

    @Override
    public void validate(Market order) {
        // not implemented yet
    }

    private void updateAccount(Order order) {
        Account account = getAccount();
        Map<FinInstr, Integer> temporaryPortfolio = account.getTemporaryPortfolio();
        FinInstr finInstr = order.getFinInstr();
        Integer quantityInPortfolio = temporaryPortfolio.get(finInstr);
        int quantityToSell = order.getQuantity();

        updateTemporaryPortfolio(temporaryPortfolio, finInstr, quantityInPortfolio, quantityToSell);
        updateTemporaryBalance(account, finInstr, quantityToSell);
    }

    private static void updateTemporaryPortfolio(Map<FinInstr, Integer> temporaryPortfolio, FinInstr finInstr,
                                                 Integer quantityInPortfolio, int quantityToSell) {
        temporaryPortfolio.put(finInstr, quantityInPortfolio - quantityToSell);
    }

    private static void updateTemporaryBalance(Account account, FinInstr finInstr, int quantityToSell) {
        double temporaryBalance = account.getTemporaryBalance();
        double updatedTemporaryBalance = temporaryBalance + finInstr.getCurrentMarketPrice() * quantityToSell;
        account.setTemporaryBalance(updatedTemporaryBalance);
    }

    private void updateMarket(Order order) {
        // not yet implemented
    }
}
