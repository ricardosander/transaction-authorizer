package io.nubank.github.authorizer.transaction;

import java.time.LocalDateTime;

class Transaction {

    private final String merchant;
    private final int amount;
    private final LocalDateTime time;

    public Transaction(String merchant, int amount, LocalDateTime time) {
        this.merchant = merchant;
        this.amount = amount;
        this.time = time;
    }

    public String getMerchant() {
        return merchant;
    }

    public int getAmount() {
        return amount;
    }

    public LocalDateTime getTime() {
        return time;
    }
}
