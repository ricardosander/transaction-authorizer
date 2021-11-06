package io.nubank.github.authorizer.transaction;

import io.nubank.github.authorizer.account.Account;

import java.util.List;

class TransactionCreationUseCase {

    private final Account account;

    TransactionCreationUseCase() {
        this.account = null;
    }

    public TransactionCreationUseCase(Account account) {
        this.account = account;
    }

    TransactionCreationResult execute(TransactionCreation request) {

        if (account == null) {
            return new TransactionCreationResult(account, List.of("account-not-initialized"));
        }

        if (!account.isActiveCard()) {
            return new TransactionCreationResult(account, List.of("card-not-active"));
        }

        account.withdraw(request.getAmount());

        return new TransactionCreationResult(account);
    }
}
