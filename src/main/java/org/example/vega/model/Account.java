package org.example.vega.model;

import java.util.HashMap;
import java.util.Map;

public class Account {

    private final String accountId;
    private double balance;
    private double temporaryBalance;
    private Map<FinInstr, Integer> portfolio = new HashMap<>();
    private Map<FinInstr, Integer> temporaryPortfolio = new HashMap<>();

    public Map<FinInstr, Integer> getPortfolio() {
        return portfolio;
    }

    public void setPortfolio(Map<FinInstr, Integer> portfolio) {
        this.portfolio = portfolio;
        this.temporaryPortfolio = new HashMap<>(portfolio);
    }

    public double getTemporaryBalance() {
        return temporaryBalance;
    }

    public void setTemporaryBalance(double temporaryBalance) {
        this.temporaryBalance = temporaryBalance;
    }

    public Map<FinInstr, Integer> getTemporaryPortfolio() {
        return temporaryPortfolio;
    }

    public void setTemporaryPortfolio(Map<FinInstr, Integer> temporaryPortfolio) {
        this.temporaryPortfolio = temporaryPortfolio;
    }

    public Account(String accountId, double initialBalance) {
        this.accountId = accountId;
        this.balance = initialBalance;
        this.temporaryBalance = initialBalance;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    @Override
    public String toString() {
        return "Account{" +
            "accountId='" + accountId + '\'' +
            ", balance=" + balance +
            ", portfolio=" + portfolio +
            '}';
    }
}
