package io.nubank.github.authorizer.transaction;

import io.nubank.github.authorizer.account.Account;

import java.util.List;

class AccountCardActiveDecorator extends TransactionCreationViolationVerifierDecorator {

    public AccountCardActiveDecorator(TransactionCreationViolationVerifier next) {
        super(next);
    }

    public List<String> verify(Account account, TransactionCreationRequest request) {
        List<String> violations = super.verify(account, request);
        if (account != null && !account.isActiveCard()) {
            violations.add("card-not-active");
        }
        return violations;
    }

}
