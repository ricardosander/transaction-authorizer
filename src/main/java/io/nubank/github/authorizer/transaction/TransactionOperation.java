package io.nubank.github.authorizer.transaction;

import io.nubank.github.authorizer.Operation;
import io.nubank.github.authorizer.OperationRequest;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TransactionOperation implements Operation {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

    private final String merchant;
    private final int amount;
    private final String time;

    public TransactionOperation(String merchant, int amount, String time) {
        this.merchant = merchant;
        this.amount = amount;
        this.time = time;
    }

    public OperationRequest toDomain() {
        return new TransactionCreation(merchant, amount, LocalDateTime.parse(time, FORMATTER));
    }
}
