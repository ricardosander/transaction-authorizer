package io.nubank.github.authorizer.transaction;

import io.nubank.github.authorizer.account.Account;

import java.util.List;

class AccountHasLimitDecorator extends TransactionCreationViolationVerifierDecorator {

    private static final String VIOLATION_NAME = "insufficient-limit";

    public AccountHasLimitDecorator(TransactionCreationViolationVerifier next) {
        super(next);
    }

    public List<String> verify(Account account, TransactionCreationRequest request) {
        List<String> violations = super.verify(account, request);
        if (account != null && accountDontHaveLimit(account, request)) {
            violations.add(VIOLATION_NAME);
        }
        return violations;
    }

    private boolean accountDontHaveLimit(Account account, TransactionCreationRequest request) {
        return !account.hasLimit(request.getAmount());
    }
}
