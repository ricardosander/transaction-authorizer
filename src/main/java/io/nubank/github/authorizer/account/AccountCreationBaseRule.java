package io.nubank.github.authorizer.account;

import java.util.ArrayList;
import java.util.List;

class AccountCreationBaseRule implements AccountCreationRule {

    private AccountCreationRule next;

    @Override
    public List<String> handle(Account account, AccountCreation accountCreation) {
        if (next == null) {
            return new ArrayList<>();
        }
        return next.handle(account, accountCreation);
    }

    @Override
    public void setNext(AccountCreationRule next) {
        this.next = next;
    }
}
