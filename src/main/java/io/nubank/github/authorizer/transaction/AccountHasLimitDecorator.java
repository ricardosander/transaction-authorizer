package io.nubank.github.authorizer.transaction;

import io.nubank.github.authorizer.account.Account;

import java.util.List;

class AccountHasLimitDecorator extends TransactionCreationViolationVerifierDecorator {

    public AccountHasLimitDecorator(TransactionCreationViolationVerifier next) {
        super(next);
    }

    public List<String> verify(Account account, TransactionCreationRequest request) {
        List<String> violations = super.verify(account, request);
        if (account != null && request.getAmount() > account.getAvailableLimit()) {
            violations.add("insufficient-limit");
        }
        return violations;
    }
}
