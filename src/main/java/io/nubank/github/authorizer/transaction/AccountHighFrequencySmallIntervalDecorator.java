package io.nubank.github.authorizer.transaction;

import io.nubank.github.authorizer.account.Account;

import java.util.List;

class AccountHighFrequencySmallIntervalDecorator extends TransactionCreationViolationVerifierDecorator {

    public AccountHighFrequencySmallIntervalDecorator(TransactionCreationViolationVerifier next) {
        super(next);
    }

    public List<String> verify(Account account, TransactionCreationRequest request) {
        List<String> violations = super.verify(account, request);
        if (account != null && account.isHighFrequencySmallInterval(request.toDomain())) {
            violations.add("high-frequency-small-interval");
        }
        return violations;
    }
}
