package io.nubank.github.authorizer.transaction;

import io.nubank.github.authorizer.account.Account;

import java.util.List;

class AccountCreatedDecorator extends TransactionCreationViolationVerifierDecorator {

    public AccountCreatedDecorator(TransactionCreationViolationVerifier next) {
        super(next);
    }

    public List<String> verify(Account account, TransactionCreationRequest request) {
        List<String> violations = super.verify(account, request);
        if (account == null) {
            violations.add("account-not-initialized");
        }
        return violations;
    }
}
