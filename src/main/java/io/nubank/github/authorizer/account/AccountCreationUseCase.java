package io.nubank.github.authorizer.account;

import io.nubank.github.authorizer.OperationResult;

import java.util.List;

public class AccountCreationUseCase {

    private final AccountRepository accountRepository;
    private final AccountCreationViolationVerifier violationVerifier;

    public AccountCreationUseCase(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
        violationVerifier = AccountCreationViolationVerifierFactory.create();
    }

    public OperationResult execute(AccountCreationRequest request) {

        Account account = accountRepository.getAccount();
        List<String> violations = violationVerifier.verify(account, request);

        if (violations.isEmpty()) {
            account = new Account(request.isActiveCard(), request.getAvailableLimit());
            accountRepository.save(account);
        }

        return new OperationResult(AccountResult.createFrom(account), violations);
    }
}
