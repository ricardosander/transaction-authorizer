package io.nubank.github.authorizer.transaction;

import io.nubank.github.authorizer.account.Account;

import java.time.LocalDateTime;
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

        if (isHighFrequencySmallInterval(request)) {
            return new TransactionCreationResult(account, List.of("high-frequency-small-interval"));
        }

        boolean fail = !account.withdraw(request.getAmount());
        if (fail) {
            return new TransactionCreationResult(account, List.of("insufficient-limit"));
        }

        transactions.add(new Transaction(request.getMerchant(), request.getAmount(), request.getTime()));
        return new TransactionCreationResult(account);
    }

    private boolean isHighFrequencySmallInterval(TransactionCreation request) {
        if (transactions.size() < 3) {
            return false;
        }
        LocalDateTime limit = request.getTime().minusMinutes(2);
        return transactions.stream().skip(transactions.size() - 3).allMatch(t -> limit.isBefore(t.getTime()));
    }
}
