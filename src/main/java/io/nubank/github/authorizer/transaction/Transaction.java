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

    String getMerchant() {
        return merchant;
    }

    public int getAmount() {
        return amount;
    }

    LocalDateTime getTime() {
        return time;
    }
}
