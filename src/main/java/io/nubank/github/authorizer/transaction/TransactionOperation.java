package io.nubank.github.authorizer.transaction;

import io.nubank.github.authorizer.operation.Operation;
import io.nubank.github.authorizer.operation.OperationRequest;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TransactionOperation implements Operation {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

    private final String merchant;
    private final int amount;
    private final String time;

    private TransactionOperation(String merchant, int amount, String time) {
        this.merchant = merchant;
        this.amount = amount;
        this.time = time;
    }

    public static Operation create(String merchant, int amount, String time) {
        return new TransactionOperation(merchant, amount, time);
    }

    public OperationRequest toDomain() {
        return new TransactionCreationRequest(merchant, amount, LocalDateTime.parse(time, FORMATTER));
    }
}
