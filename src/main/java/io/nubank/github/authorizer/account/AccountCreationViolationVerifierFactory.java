package io.nubank.github.authorizer.account;

abstract class AccountCreationViolationVerifierFactory {
    static AccountCreationViolationVerifier create() {
        return new AccountAlreadyInitializedDecorator(null);
    }
}
