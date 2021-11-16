package io.nubank.github.authorizer.transaction;

import java.time.LocalDateTime;

public class Transaction {

    private final String merchant;
    private final int amount;
    private final LocalDateTime time;

    Transaction(String merchant, int amount, LocalDateTime time) {
        this.merchant = merchant;
        this.amount = amount;
        this.time = time;
    }

    private String getMerchant() {
        return merchant;
    }

    public int getAmount() {
        return amount;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public boolean isDoubleTransaction(Transaction target, LocalDateTime limit) {
        return merchant.equals(target.getMerchant())
                && amount == target.getAmount()
                && limit.isBefore(time);
    }
}
