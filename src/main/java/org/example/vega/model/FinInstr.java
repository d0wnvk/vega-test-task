package org.example.vega.model;

import java.util.Objects;

public class FinInstr {

    private final String id;
    private final String symbol;
    private double currentMarketPrice;

    public FinInstr(String id, String symbol, double currentMarketPrice) {
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

    // two FI are equals if their Symbols are equal
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        FinInstr that = (FinInstr) o;
        boolean equals = Objects.equals(symbol, that.symbol);
        return equals;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(symbol);
    }

    @Override
    public String toString() {
        return "\n    FinancialInstrument{" +
            "id='" + id + '\'' +
            ", symbol='" + symbol + '\'' +
            ", currentMarketPrice=" + currentMarketPrice +
            '}';
    }
}
