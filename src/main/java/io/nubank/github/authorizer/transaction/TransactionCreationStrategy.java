package io.nubank.github.authorizer.transaction;

import io.nubank.github.authorizer.operation.OperationRequest;
import io.nubank.github.authorizer.operation.OperationResult;
import io.nubank.github.authorizer.operation.OperationStrategy;
import io.nubank.github.authorizer.account.Account;
import io.nubank.github.authorizer.account.AccountRepository;
import io.nubank.github.authorizer.operation.AccountResult;

import java.util.List;

public class TransactionCreationStrategy implements OperationStrategy {

    private final AccountRepository accountRepository;
    private final TransactionCreationViolationVerifier violationVerifier;

    public TransactionCreationStrategy(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
        this.violationVerifier = TransactionCreationRulesFactory.create();
    }

    public OperationResult execute(OperationRequest request) {

        TransactionCreationRequest transactionCreationRequest = (TransactionCreationRequest) request;

        Account account = accountRepository.getAccount();
        List<String> violations = violationVerifier.verify(account, transactionCreationRequest);

        if (violations.isEmpty()) {
            account.transfer(transactionCreationRequest.toDomain());
        }

        return new OperationResult(AccountResult.createFrom(account), violations);
    }
}
