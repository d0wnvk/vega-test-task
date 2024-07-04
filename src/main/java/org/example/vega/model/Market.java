package org.example.vega.model;

import java.util.HashMap;
import java.util.Map;

/**
 * I believe Market is just a Broker in real life and do not have any stocks.
 * Its purpose is to transfer Stocks from one portfolio (account) to another.
 * */
public class Market {

    private Map<FinInstr, Integer> stocks ;

    public Market() {
        this.stocks = new HashMap<>();
    }

    public Map<FinInstr, Integer> getStocks() {
        return stocks;
    }

    public void setStocks(Map<FinInstr, Integer> stocks) {
        this.stocks = stocks;
    }

    public void addStock(FinInstr stock, Integer quantity) {
        this.stocks.put(stock, quantity);
    }

    public boolean hasStock(FinInstr finInstr) {
        return getStocks().containsKey(finInstr);
    }

    public boolean hasNoStock(FinInstr finInstr) {
        return !getStocks().containsKey(finInstr);
    }

}
