package io.nubank.github.authorizer.transaction;

import io.nubank.github.authorizer.account.AccountResult;
import io.nubank.github.authorizer.OperationResult;
import io.nubank.github.authorizer.account.Account;
import io.nubank.github.authorizer.account.AccountRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TransactionCreationUseCase {

    private final AccountRepository accountRepository;

    public TransactionCreationUseCase(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public OperationResult execute(TransactionCreation request) {

        Account account = accountRepository.getAccount();
        if (account == null) {
            return new OperationResult(null, List.of("account-not-initialized"));
        }

        if (!account.isActiveCard()) {
            return new OperationResult(AccountResult.createFrom(account), List.of("card-not-active"));
        }

        List<String> violations = new ArrayList<>();

        if (request.getAmount() > account.getAvailableLimit()) {
            violations.add("insufficient-limit");
        }

        if (isHighFrequencySmallInterval(account, request)) {
            violations.add("high-frequency-small-interval");
        }

        if (isDoubledTransaction(account, request)) {
            violations.add("doubled-transaction");
        }

        if (violations.isEmpty()) {
            account.add(new Transaction(request.getMerchant(), request.getAmount(), request.getTime()));
        }

        return new OperationResult(AccountResult.createFrom(account), violations);
    }

    private boolean isHighFrequencySmallInterval(Account account, TransactionCreation request) {
        List<Transaction> accountTransactions = account.getTransactions();
        if (accountTransactions.size() < 3) {
            return false;
        }
        LocalDateTime limit = request.getTime().minusMinutes(2);
        return accountTransactions.stream().skip(accountTransactions.size() - 3).allMatch(t -> limit.isBefore(t.getTime()));
    }

    private boolean isDoubledTransaction(Account account, TransactionCreation request) {
        List<Transaction> accountTransactions = account.getTransactions();
        if (accountTransactions.isEmpty()) {
            return false;
        }
        LocalDateTime limit = request.getTime().minusMinutes(2);
        return accountTransactions.stream()
                .anyMatch(transaction -> isDoubleTransaction(request, limit, transaction));
    }

    private boolean isDoubleTransaction(TransactionCreation request, LocalDateTime limit, Transaction transaction) {
        return transaction.getMerchant().equals(request.getMerchant())
                && transaction.getAmount() == request.getAmount()
                && limit.isBefore(transaction.getTime());
    }
}
