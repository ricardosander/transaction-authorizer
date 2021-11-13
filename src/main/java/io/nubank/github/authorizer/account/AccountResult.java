package io.nubank.github.authorizer.account;

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
        return new AccountResult(account.isActiveCard(), account.getAvailableLimit());
    }
}
