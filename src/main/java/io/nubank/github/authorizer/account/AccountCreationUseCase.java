package io.nubank.github.authorizer.account;

import io.nubank.github.authorizer.OperationResult;

import java.util.List;

public class AccountCreationUseCase {

    private final AccountRepository repository;
    private final AccountCreationRule rules;

    public AccountCreationUseCase(AccountRepository accountRepository) {
        repository = accountRepository;
        rules = AccountCreationRuleFactory.create();
    }

    public OperationResult execute(AccountCreationRequest request) {

        List<String> violations = rules.handle(repository.getAccount(), request);

        if (violations.isEmpty()) {
            repository.save(new Account(request.isActiveCard(), request.getAvailableLimit()));
        }

        return new OperationResult(AccountResult.createFrom(repository.getAccount()), violations);
    }
}
