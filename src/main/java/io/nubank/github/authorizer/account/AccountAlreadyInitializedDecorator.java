package io.nubank.github.authorizer.account;

import java.util.List;

class AccountAlreadyInitializedDecorator extends AccountCreationViolationVerifierDecorator {

    private static final String VIOLATION_NAME = "account-already-initialized";

    AccountAlreadyInitializedDecorator(AccountCreationViolationVerifier wrappee) {
        super(wrappee);
    }

    @Override
    public List<String> verify(Account existingAccount, AccountCreationRequest request) {
        List<String> violations = super.verify(existingAccount, request);
        if (existingAccount != null) {
            violations.add(VIOLATION_NAME);
        }
        return violations;
    }
}
