package io.nubank.github.authorizer.account;

import java.util.ArrayList;

public class AccountCreationUseCase {

    public AccountCreationResult execute(AccountCreation request) {
        return new AccountCreationResult(request.isActiveCard(), request.getAvailableLimit(), new ArrayList<>());
    }
}
