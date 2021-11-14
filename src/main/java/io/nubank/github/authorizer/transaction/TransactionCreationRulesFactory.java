package io.nubank.github.authorizer.transaction;

abstract class TransactionCreationRulesFactory {

    static TransactionCreationViolationVerifier create() {

        AccountCreatedDecorator createdDecorator = new AccountCreatedDecorator(null);
        AccountCardActiveDecorator accountCardActiveDecorator = new AccountCardActiveDecorator(createdDecorator);
        AccountHasLimitDecorator accountHasLimitDecorator = new AccountHasLimitDecorator(accountCardActiveDecorator);
        AccountHighFrequencySmallIntervalDecorator accountHighFrequencySmallIntervalDecorator = new AccountHighFrequencySmallIntervalDecorator(accountHasLimitDecorator);
        AccountDoubledTransactionDecorator accountDoubledTransactionDecorator = new AccountDoubledTransactionDecorator(accountHighFrequencySmallIntervalDecorator);

        return accountDoubledTransactionDecorator;
    }
}
