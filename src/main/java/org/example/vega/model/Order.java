package org.example.vega.model;


public abstract class Order {

    private String orderId;
    private Account account;
    private String traderId;
    private FinInstr finInstr;
    private int quantity;

    public Order(String orderId,
                 Account account,
                 FinInstr cfi,
                 int quantity,
                 String traderId) {
        this.orderId = orderId;
        this.account = account;
        this.finInstr = cfi;
        this.quantity = quantity;
        this.traderId = traderId;
    }

    /**
     * Invocation of this method will not change account's and market's states since affected temporary values only
     */
    public abstract void executeOrder(Order order);

    public abstract void validate(Market order);


    public String getOrderId() {
        return orderId;
    }

    public String getTraderId() {
        return traderId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public void setTraderId(String traderId) {
        this.traderId = traderId;
    }

    public FinInstr getFinInstr() {
        return finInstr;
    }

    public void setFinInstr(CompostFinInstr finInstr) {
        this.finInstr = finInstr;
    }


    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }


    @Override
    public String toString() {
        return "Order{" +
            "orderId='" + orderId + '\'' +
            ", account=" + account +
            ", traderId='" + traderId + '\'' +
            ", finInstr=" + finInstr +
            ", quantity=" + quantity +
            '}';
    }
}
