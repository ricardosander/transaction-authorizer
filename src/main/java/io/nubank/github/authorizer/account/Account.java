package io.nubank.github.authorizer.account;

public class Account {

    private final boolean activeCard;
    private int availableLimit;

    public Account(boolean activeCard, int availableLimit) {
        this.activeCard = activeCard;
        this.availableLimit = availableLimit;
    }

    public boolean isActiveCard() {
        return activeCard;
    }

    public int getAvailableLimit() {
        return availableLimit;
    }

    Account getState() {
        return new Account(activeCard, availableLimit);
    }

    public boolean withdraw(int amount) {
        if (amount > availableLimit) {
            return false;
        }
        availableLimit -= amount;
        return true;
    }
}
