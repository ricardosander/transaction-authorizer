package io.nubank.github.authorizer.account;

abstract class AccountCreationRuleFactory {
    static AccountCreationRule create() {
        return new AccountAlreadyInitializedAccountCreationRule();
    }
}
