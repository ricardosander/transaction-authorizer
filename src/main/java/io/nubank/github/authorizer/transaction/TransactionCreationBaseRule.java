package io.nubank.github.authorizer.transaction;

import io.nubank.github.authorizer.account.Account;

import java.util.ArrayList;
import java.util.List;

abstract class TransactionCreationBaseRule implements TransactionCreationRule {

    private TransactionCreationRule next;

    public List<String> handle(Account account, TransactionCreation request) {
        if (next == null) {
            return new ArrayList<>();
        }
        return next.handle(account, request);
    }

    public void setNext(TransactionCreationRule next) {
        this.next = next;
    }
}
