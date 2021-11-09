package io.nubank.github.authorizer.transaction;

import io.nubank.github.authorizer.OperationRequest;

import java.time.LocalDateTime;

public class TransactionCreation implements OperationRequest {

    private final String merchant;
    private final int amount;
    private final LocalDateTime time;

    public TransactionCreation(String merchant, int amount, LocalDateTime time) {
        this.merchant = merchant;
        this.amount = amount;
        this.time = time;
    }

    int getAmount() {
        return amount;
    }

    String getMerchant() {
        return merchant;
    }

    LocalDateTime getTime() {
        return time;
    }
}
