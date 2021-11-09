package io.nubank.github.authorizer.account;

import io.nubank.github.authorizer.OperationRequest;

public class AccountCreation implements OperationRequest {

    private final boolean activeCard;
    private final int availableLimit;

    public AccountCreation(boolean activeCard, int availableLimit) {
        this.activeCard = activeCard;
        this.availableLimit = availableLimit;
    }

    boolean isActiveCard() {
        return activeCard;
    }

    int getAvailableLimit() {
        return availableLimit;
    }
}
