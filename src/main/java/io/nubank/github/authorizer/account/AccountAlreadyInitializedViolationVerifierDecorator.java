package io.nubank.github.authorizer.account;

import java.util.List;

class AccountAlreadyInitializedViolationVerifierDecorator extends AccountCreationViolationVerifierDecorator {

    AccountAlreadyInitializedViolationVerifierDecorator(AccountCreationViolationVerifier wrappee) {
        super(wrappee);
    }

    @Override
    public List<String> verify(Account existingAccount, AccountCreationRequest request) {
        List<String> violations = super.verify(existingAccount, request);
        if (existingAccount != null) {
            violations.add("account-already-initialized");
        }
        return violations;
    }
}
