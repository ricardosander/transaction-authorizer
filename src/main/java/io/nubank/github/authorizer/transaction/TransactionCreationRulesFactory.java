package io.nubank.github.authorizer.transaction;

abstract class TransactionCreationRulesFactory {

    static TransactionCreationRule create() {

        AccountCreatedTransactionCreationRule accountCreatedRule = new AccountCreatedTransactionCreationRule();

        AccountCardActiveTransactionCreationRule accountCardActiveRule = new AccountCardActiveTransactionCreationRule();
        accountCardActiveRule.setNext(accountCreatedRule);

        AccountHasLimitTransactionCreationRule accountHasLimitRule = new AccountHasLimitTransactionCreationRule();
        accountHasLimitRule.setNext(accountCardActiveRule);

        AccountHighFrequencySmallIntervalTransactionCreationRule accountHighFrequencySmallIntervalRule = new AccountHighFrequencySmallIntervalTransactionCreationRule();
        accountHighFrequencySmallIntervalRule.setNext(accountHasLimitRule);

        AccountDoubledTransactionTransactionCreationRule accountDoubledTransaction = new AccountDoubledTransactionTransactionCreationRule();
        accountDoubledTransaction.setNext(accountHighFrequencySmallIntervalRule);

        return accountDoubledTransaction;
    }
}
