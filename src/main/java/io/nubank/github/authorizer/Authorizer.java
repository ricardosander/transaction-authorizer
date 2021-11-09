package io.nubank.github.authorizer;

import io.nubank.github.authorizer.account.AccountCreation;
import io.nubank.github.authorizer.account.AccountCreationResult;
import io.nubank.github.authorizer.account.AccountCreationUseCase;

import java.util.ArrayList;
import java.util.List;

class Authorizer {

    private final AccountCreationUseCase accountCreation;

    Authorizer() {
        accountCreation = new AccountCreationUseCase();
    }

    List<AccountCreationResult> execute(List<AccountCreation> requests) {

        List<AccountCreationResult> results = new ArrayList<>();

        requests.forEach(request -> results.add(accountCreation.execute(request)));

        return results;
    }
}
