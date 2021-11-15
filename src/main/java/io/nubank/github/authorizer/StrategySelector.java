package io.nubank.github.authorizer;

import io.nubank.github.authorizer.account.AccountCreationRequest;
import io.nubank.github.authorizer.account.AccountCreationStrategy;
import io.nubank.github.authorizer.transaction.TransactionCreationRequest;
import io.nubank.github.authorizer.transaction.TransactionCreationStrategy;

public class StrategySelector {

    private final AccountCreationStrategy accountCreationStrategy;
    private final TransactionCreationStrategy transactionCreationStrategy;

    public StrategySelector(AccountCreationStrategy accountCreationStrategy, TransactionCreationStrategy transactionCreationStrategy) {
        this.accountCreationStrategy = accountCreationStrategy;
        this.transactionCreationStrategy = transactionCreationStrategy;
    }

    OperationStrategy getStrategy(OperationRequest request) {

        if (request instanceof AccountCreationRequest) {
            return accountCreationStrategy;
        }

        if (request instanceof TransactionCreationRequest) {
            return transactionCreationStrategy;
        }

        throw new IllegalArgumentException("Unexpected Operation type");
    }
}
