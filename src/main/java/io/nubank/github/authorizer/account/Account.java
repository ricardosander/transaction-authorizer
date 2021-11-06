package io.nubank.github.authorizer.account;

class Account {

    private final boolean activeCard;
    private final int availableLimit;

    public Account(boolean activeCard, int availableLimit) {
        this.activeCard = activeCard;
        this.availableLimit = availableLimit;
    }
}
