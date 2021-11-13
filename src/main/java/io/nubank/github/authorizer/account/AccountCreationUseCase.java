package io.nubank.github.authorizer.account;

import io.nubank.github.authorizer.OperationResult;

import java.util.ArrayList;

public class AccountCreationUseCase {

    private final AccountRepository repository;

    public AccountCreationUseCase(AccountRepository accountRepository) {
        repository = accountRepository;
    }

    public OperationResult execute(AccountCreation request) {

        ArrayList<String> violations = new ArrayList<>();
        Account account = repository.getAccount();
        if (account == null) {
            account = new Account(request.isActiveCard(), request.getAvailableLimit());
            repository.setAccount(account);
        } else {
            violations.add("account-already-initialized");
        }
        return new OperationResult(AccountResult.createFrom(account), violations);
    }
}
