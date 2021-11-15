package io.nubank.github.authorizer.account;

import io.nubank.github.authorizer.operation.AccountResult;
import io.nubank.github.authorizer.operation.OperationRequest;
import io.nubank.github.authorizer.operation.OperationResult;
import io.nubank.github.authorizer.operation.OperationStrategy;

import java.util.List;

public class AccountCreationStrategy implements OperationStrategy {

    private final AccountRepository accountRepository;
    private final AccountCreationViolationVerifier violationVerifier;

    public AccountCreationStrategy(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
        violationVerifier = AccountCreationViolationVerifierFactory.create();
    }

    public OperationResult execute(OperationRequest request) {

        AccountCreationRequest accountCreationRequest = (AccountCreationRequest) request;

        Account account = accountRepository.getAccount();
        List<String> violations = violationVerifier.verify(account, accountCreationRequest);

        if (violations.isEmpty()) {
            account = accountCreationRequest.toDomain();
            accountRepository.save(account);
        }

        return new OperationResult(AccountResult.createFrom(account), violations);
    }
}
