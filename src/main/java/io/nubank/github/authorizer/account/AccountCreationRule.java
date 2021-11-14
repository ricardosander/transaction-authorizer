package io.nubank.github.authorizer.account;

import java.util.List;

interface AccountCreationRule {
    List<String> handle(Account account, AccountCreation accountCreation);
    void setNext(AccountCreationRule next);
}
