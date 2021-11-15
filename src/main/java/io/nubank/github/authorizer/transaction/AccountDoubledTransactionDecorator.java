package io.nubank.github.authorizer.transaction;

import io.nubank.github.authorizer.account.Account;

import java.util.List;

class AccountDoubledTransactionDecorator extends TransactionCreationViolationVerifierDecorator {

    public AccountDoubledTransactionDecorator(TransactionCreationViolationVerifier next) {
        super(next);
    }

    public List<String> verify(Account account, TransactionCreationRequest request) {
        List<String> violations = super.verify(account, request);
        if (account != null && account.isDoubledTransaction(request.toDomain())) {
            violations.add("doubled-transaction");
        }
        return violations;
    }
}
