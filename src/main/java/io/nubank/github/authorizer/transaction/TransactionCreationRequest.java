package io.nubank.github.authorizer.transaction;

import io.nubank.github.authorizer.operation.OperationRequest;

import java.time.LocalDateTime;

public class TransactionCreationRequest implements OperationRequest {

    private final String merchant;
    private final int amount;
    private final LocalDateTime time;

    public TransactionCreationRequest(String merchant, int amount, LocalDateTime time) {
        this.merchant = merchant;
        this.amount = amount;
        this.time = time;
    }

    int getAmount() {
        return amount;
    }

    public Transaction toDomain() {
        return new Transaction(merchant, amount, time);
    }
}
