package io.nubank.github.authorizer;

import io.nubank.github.authorizer.account.AccountCreationStrategy;
import io.nubank.github.authorizer.account.AccountRepository;
import io.nubank.github.authorizer.operation.OperationRequest;
import io.nubank.github.authorizer.operation.OperationResult;
import io.nubank.github.authorizer.operation.StrategySelector;
import io.nubank.github.authorizer.transaction.TransactionCreationStrategy;

import java.util.List;
import java.util.stream.Collectors;

class AuthorizerProcessor {

    private final StrategySelector strategyFactory;

    AuthorizerProcessor(AccountRepository accountRepository) {
        strategyFactory = new StrategySelector(
                new AccountCreationStrategy(accountRepository),
                new TransactionCreationStrategy(accountRepository)
        );
    }

    List<OperationResult> execute(List<OperationRequest> requests) {
        return requests.stream()
                .map(request -> strategyFactory.getStrategy(request).execute(request))
                .collect(Collectors.toList());
    }
}
