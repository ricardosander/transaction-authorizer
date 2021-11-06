package io.nubank.github.authorizer.transaction;

import io.nubank.github.authorizer.account.Account;

import java.util.ArrayList;

class TransactionCreationUseCase {

    private final Account account;

    TransactionCreationUseCase() {
        this.account = null;
    }

    public TransactionCreationUseCase(Account account) {
        this.account = account;
    }

    TransactionCreationResult execute(TransactionCreation request) {
        ArrayList<String> violations = new ArrayList<>();
        if (account == null) {
            violations.add("account-not-initialized");
        } else {
            account.withdraw(request.getAmount());
        }
        return new TransactionCreationResult(account, violations);
    }
}
