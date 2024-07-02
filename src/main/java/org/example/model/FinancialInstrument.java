package org.example.model;

public class FinancialInstrument {

    private String id;
    private String symbol;
    private double currentMarketPrice;

    public FinancialInstrument(String id, String symbol, double currentMarketPrice) {
        this.id = id;
        this.symbol = symbol;
        this.currentMarketPrice = currentMarketPrice;
    }

    public String getId() {
        return id;
    }

    public String getSymbol() {
        return symbol;
    }

    public double getCurrentMarketPrice() {
        return currentMarketPrice;
    }

    public void setCurrentMarketPrice(double currentMarketPrice) {
        this.currentMarketPrice = currentMarketPrice;
    }

}
