package io.nubank.github.authorizer.transaction;

import io.nubank.github.authorizer.OperationResult;
import io.nubank.github.authorizer.account.Account;
import io.nubank.github.authorizer.account.AccountRepository;
import io.nubank.github.authorizer.account.AccountResult;

import java.util.List;

public class TransactionCreationUseCase {

    private final AccountRepository accountRepository;

    private final TransactionCreationRule rules;

    public TransactionCreationUseCase(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
        this.rules = TransactionCreationRulesFactory.create();
    }

    public OperationResult execute(TransactionCreationRequest request) {

        Account account = accountRepository.getAccount();
        List<String> violations = rules.handle(account, request);

        if (violations.isEmpty()) {
            account.transfer(request.toDomain());
        }

        return new OperationResult(AccountResult.createFrom(account), violations);
    }
}
