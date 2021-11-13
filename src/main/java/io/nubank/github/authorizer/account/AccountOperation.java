package io.nubank.github.authorizer.account;

import io.nubank.github.authorizer.Operation;
import io.nubank.github.authorizer.OperationRequest;

public class AccountOperation implements Operation {

    private final boolean activeCard;
    private final int availableLimit;

    public AccountOperation(boolean activeCard, int availableLimit) {
        this.activeCard = activeCard;
        this.availableLimit = availableLimit;
    }

    public OperationRequest toDomain() {
        return new AccountCreation(activeCard, availableLimit);
    }
}
