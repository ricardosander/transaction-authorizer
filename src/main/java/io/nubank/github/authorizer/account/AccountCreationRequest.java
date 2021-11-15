package io.nubank.github.authorizer.account;

import io.nubank.github.authorizer.operation.OperationRequest;

public class AccountCreationRequest implements OperationRequest {

    private final boolean activeCard;
    private final int availableLimit;

    public AccountCreationRequest(boolean activeCard, int availableLimit) {
        this.activeCard = activeCard;
        this.availableLimit = availableLimit;
    }

    Account toDomain() {
        return new Account(activeCard, availableLimit);
    }
}
