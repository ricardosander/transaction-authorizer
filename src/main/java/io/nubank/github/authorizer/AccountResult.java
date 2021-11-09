package io.nubank.github.authorizer;

import io.nubank.github.authorizer.account.Account;

public class AccountResult {

    private final boolean activeCard;
    private final int availableLimit;

    private AccountResult(boolean activeCard, int availableLimit) {
        this.activeCard = activeCard;
        this.availableLimit = availableLimit;
    }

    boolean isActiveCard() {
        return activeCard;
    }

    int getAvailableLimit() {
        return availableLimit;
    }

    public static AccountResult createFrom(Account account) {
        return new AccountResult(account.isActiveCard(), account.getAvailableLimit());
    }
}
