package io.nubank.github.authorizer.account;

import java.util.ArrayList;

public class AccountCreationUseCase {

    private Account account;

    public AccountCreationResult execute(AccountCreation request) {

        ArrayList<String> violations = new ArrayList<>();
        if (account == null) {
            account = new Account(request.isActiveCard(), request.getAvailableLimit());
        } else {
            violations.add("account-already-initialized");
        }
        return new AccountCreationResult(request.isActiveCard(), request.getAvailableLimit(), violations);
    }
}
