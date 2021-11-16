package io.nubank.github.authorizer.transaction;

import io.nubank.github.authorizer.account.Account;

import java.util.List;

class AccountCreatedDecorator extends TransactionCreationViolationVerifierDecorator {

    private static final String VIOLATION_NAME = "account-not-initialized";

    public AccountCreatedDecorator(TransactionCreationViolationVerifier next) {
        super(next);
    }

    public List<String> verify(Account account, TransactionCreationRequest request) {
        List<String> violations = super.verify(account, request);
        if (account == null) {
            violations.add(VIOLATION_NAME);
        }
        return violations;
    }
}
