package org.example.model;

import java.util.List;

public class CompositeFinancialInstrument extends FinancialInstrument {
    private List<FinancialInstrument> components;

    public CompositeFinancialInstrument(String id, String symbol, List<FinancialInstrument> components) {
        super(id, symbol, calculateCompositePrice(components));
        this.components = components;
    }

    // todo is it correct computation of a market price ?
    private static double calculateCompositePrice(List<FinancialInstrument> components) {
        return components.stream()
            .mapToDouble(FinancialInstrument::getCurrentMarketPrice)
            .average()
            .orElse(0);
    }

    public List<FinancialInstrument> getComponents() {
        return components;
    }

    public void updatePrice() {
        setCurrentMarketPrice(calculateCompositePrice(components));
    }
}
