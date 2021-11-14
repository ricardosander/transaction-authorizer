package io.nubank.github.authorizer.transaction;

import io.nubank.github.authorizer.account.Account;

import java.util.List;

interface TransactionCreationViolationVerifier {
    List<String> verify(Account account, TransactionCreationRequest request);
}
