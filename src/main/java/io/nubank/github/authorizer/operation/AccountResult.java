package io.nubank.github.authorizer.operation;

import io.nubank.github.authorizer.account.Account;

public class AccountResult {

    private final boolean activeCard;
    private final int availableLimit;

    private AccountResult(boolean activeCard, int availableLimit) {
        this.activeCard = activeCard;
        this.availableLimit = availableLimit;
    }

    public boolean isActiveCard() {
        return activeCard;
    }

    public int getAvailableLimit() {
        return availableLimit;
    }

    public static AccountResult createFrom(Account account) {
        if (account == null) {
            return null;
        }
        return new AccountResult(account.isActiveCard(), account.getAvailableLimit());
    }
}
