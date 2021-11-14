package io.nubank.github.authorizer.transaction;

import io.nubank.github.authorizer.account.Account;

import java.util.ArrayList;
import java.util.List;

abstract class TransactionCreationViolationVerifierDecorator implements TransactionCreationViolationVerifier {

    private TransactionCreationViolationVerifier wrappee;

    public TransactionCreationViolationVerifierDecorator(TransactionCreationViolationVerifier next) {
        this.wrappee = next;
    }

    public List<String> verify(Account account, TransactionCreationRequest request) {
        if (wrappee == null) {
            return new ArrayList<>();
        }
        return wrappee.verify(account, request);
    }
}
