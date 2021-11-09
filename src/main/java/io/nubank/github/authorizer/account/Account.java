package io.nubank.github.authorizer.account;

import io.nubank.github.authorizer.transaction.Transaction;

import java.util.Collections;
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

    public boolean add(Transaction transaction) {
        if (transaction.getAmount() > availableLimit) {
            return false;
        }
        transactions.add(transaction);
        availableLimit -= transaction.getAmount();
        return true;
    }

    public List<Transaction> getTransactions() {
        return Collections.unmodifiableList(transactions);
    }
}
