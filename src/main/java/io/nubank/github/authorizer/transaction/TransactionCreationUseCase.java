package io.nubank.github.authorizer.transaction;

import io.nubank.github.authorizer.OperationResult;
import io.nubank.github.authorizer.account.Account;
import io.nubank.github.authorizer.account.AccountRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class TransactionCreationUseCase {

    private final AccountRepository accountRepository;
    private final List<Transaction> transactions;

    public TransactionCreationUseCase(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
        this.transactions = new LinkedList<>();
    }

    public OperationResult execute(TransactionCreation request) {

        Account account = accountRepository.getAccount();
        if (account == null) {
            return new OperationResult(null, List.of("account-not-initialized"));
        }

        if (!account.isActiveCard()) {
            return new OperationResult(account.getState(), List.of("card-not-active"));
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

        return new OperationResult(account.getState(), violations);
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
