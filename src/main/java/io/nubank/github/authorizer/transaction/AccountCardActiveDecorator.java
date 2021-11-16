package io.nubank.github.authorizer.transaction;

import io.nubank.github.authorizer.account.Account;

import java.util.List;

class AccountCardActiveDecorator extends TransactionCreationViolationVerifierDecorator {

    private static final String VIOLATION_NAME = "card-not-active";

    public AccountCardActiveDecorator(TransactionCreationViolationVerifier next) {
        super(next);
    }

    public List<String> verify(Account account, TransactionCreationRequest request) {
        List<String> violations = super.verify(account, request);
        if (account != null && !account.isActiveCard()) {
            violations.add(VIOLATION_NAME);

        }
        return violations;

    }

}
