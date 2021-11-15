package io.nubank.github.authorizer.account;

import io.nubank.github.authorizer.transaction.Transaction;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

public class Account {

    private final boolean activeCard;
    private int availableLimit;
    private final List<Transaction> transactions;

    Account(boolean activeCard, int availableLimit) {
        this.activeCard = activeCard;
        this.availableLimit = availableLimit;
        this.transactions = new LinkedList<>();
    }

    public boolean isActiveCard() {
        return activeCard;
    }

    public int getAvailableLimit() {
        return availableLimit;
    }

    public boolean transfer(Transaction transaction) {
        boolean dontHaveLimit = !hasLimit(transaction.getAmount());
        if (dontHaveLimit) {
            return false;
        }
        transactions.add(transaction);
        availableLimit -= transaction.getAmount();
        return true;
    }

    public boolean hasLimit(int amount) {
        return this.availableLimit >= amount;
    }

    public boolean isHighFrequencySmallInterval(Transaction target) {
        if (this.transactions.size() < 3) {
            return false;
        }
        LocalDateTime limit = target.getTime().minusMinutes(2);
        return transactions.stream().skip(transactions.size() - 3).allMatch(t -> limit.isBefore(t.getTime()));
    }

    public boolean isDoubledTransaction(Transaction target) {
        if (transactions.isEmpty()) {
            return false;
        }
        return transactions.stream().anyMatch(transaction -> transaction.isDoubleTransaction(target));
    }
}
