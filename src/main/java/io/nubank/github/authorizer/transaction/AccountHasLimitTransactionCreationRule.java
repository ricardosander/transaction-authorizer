package io.nubank.github.authorizer.transaction;

import io.nubank.github.authorizer.account.Account;

import java.util.List;

class AccountHasLimitTransactionCreationRule extends TransactionCreationBaseRule {

    public List<String> handle(Account account, TransactionCreation request) {
        List<String> violations = super.handle(account, request);
        if (account != null && request.getAmount() > account.getAvailableLimit()) {
            violations.add("insufficient-limit");
        }
        return violations;
    }
}
