package org.example.vega.model;

import java.util.Set;

public class CompostFinInstr extends FinInstr {

    private final Set<FinInstr> components;

    public CompostFinInstr(String id, String symbol, Set<FinInstr> components) {
        super(id, symbol, calculateCompositePrice(components));
        this.components = components;
    }

    public static double calculateCompositePrice(Set<FinInstr> components) {
        return components.stream()
            .mapToDouble(FinInstr::getCurrentMarketPrice)
            .average()
            .orElse(0);
    }

    public double calculateCompositePrice() {
        return this.components.stream()
            .mapToDouble(FinInstr::getCurrentMarketPrice)
            .average()
            .orElse(0);
    }

    @Override
    public String toString() {
        return "\n  CompositeFinancialInstrument{" +
            "id=" + getId() +
            ", composite price=" + calculateCompositePrice() +
            ", components=" + components +
            '}';
    }

}
