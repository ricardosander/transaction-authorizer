package io.nubank.github.authorizer.account;

class AccountCreation {

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
