package io.nubank.github.authorizer.transaction;

import io.nubank.github.authorizer.account.Account;

import java.util.ArrayList;

class TransactionCreationUseCase {

    private final Account account;

    TransactionCreationUseCase() {
        this.account = null;
    }

    TransactionCreationResult execute(TransactionCreation request) {
        ArrayList<String> violations = new ArrayList<>();
        if (account == null) {
            violations.add("account-not-initialized");
        }
        return new TransactionCreationResult(account, violations);
    }
}
