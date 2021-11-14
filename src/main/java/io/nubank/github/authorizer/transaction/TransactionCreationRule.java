package io.nubank.github.authorizer.transaction;

import io.nubank.github.authorizer.account.Account;

import java.util.List;

interface TransactionCreationRule {
    List<String> handle(Account account, TransactionCreationRequest request);
    void setNext(TransactionCreationRule next);
}