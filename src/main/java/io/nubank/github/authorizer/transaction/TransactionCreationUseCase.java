package io.nubank.github.authorizer.transaction;

import io.nubank.github.authorizer.account.Account;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

class TransactionCreationUseCase {

    private final Account account;
    private final List<Transaction> transactions;

    TransactionCreationUseCase() {
        this.account = null;
        this.transactions = new LinkedList<>();
    }

    public TransactionCreationUseCase(Account account) {
        this.account = account;
        this.transactions = new LinkedList<>();
    }

    TransactionCreationResult execute(TransactionCreation request) {

        if (account == null) {
            return new TransactionCreationResult(account, List.of("account-not-initialized"));
        }

        if (!account.isActiveCard()) {
            return new TransactionCreationResult(account, List.of("card-not-active"));
        }

        List<String> violations = new ArrayList<>();
        if (isHighFrequencySmallInterval(request)) {
            violations.add("high-frequency-small-interval");
        }

        if (isDoubledTransaction(request)) {
            violations.add("doubled-transaction");
        }

        if (request.getAmount() > account.getAvailableLimit()) {
            violations.add("insufficient-limit");
        }

        if (violations.isEmpty()) {
            account.withdraw(request.getAmount());
            transactions.add(new Transaction(request.getMerchant(), request.getAmount(), request.getTime()));
        }

        return new TransactionCreationResult(account, violations);
    }

    private boolean isHighFrequencySmallInterval(TransactionCreation request) {
        if (transactions.size() < 3) {
            return false;
        }
        LocalDateTime limit = request.getTime().minusMinutes(2);
        return transactions.stream().skip(transactions.size() - 3).allMatch(t -> limit.isBefore(t.getTime()));
    }

    private boolean isDoubledTransaction(TransactionCreation request) {
        if (transactions.isEmpty()) {
            return false;
        }
        LocalDateTime limit = request.getTime().minusMinutes(2);
        return transactions.stream()
                .anyMatch(transaction -> isDoubleTransaction(request, limit, transaction));
    }

    private boolean isDoubleTransaction(TransactionCreation request, LocalDateTime limit, Transaction transaction) {
        return transaction.getMerchant().equals(request.getMerchant())
                && transaction.getAmount() == request.getAmount()
                && limit.isBefore(transaction.getTime());
    }
}
