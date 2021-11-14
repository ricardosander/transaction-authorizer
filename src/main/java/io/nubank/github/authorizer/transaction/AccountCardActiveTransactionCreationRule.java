package io.nubank.github.authorizer.transaction;

import io.nubank.github.authorizer.account.Account;

import java.util.List;

class AccountCardActiveTransactionCreationRule extends TransactionCreationBaseRule {

    public List<String> handle(Account account, TransactionCreation request) {
        List<String> violations = super.handle(account, request);
        if (account != null && !account.isActiveCard()) {
            violations.add("card-not-active");
        }
        return violations;
    }

}
