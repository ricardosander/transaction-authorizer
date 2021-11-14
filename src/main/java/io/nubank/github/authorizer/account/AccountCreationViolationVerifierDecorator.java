package io.nubank.github.authorizer.account;

import java.util.ArrayList;
import java.util.List;

class AccountCreationViolationVerifierDecorator implements AccountCreationViolationVerifier {

    private final AccountCreationViolationVerifier wrappee;

    AccountCreationViolationVerifierDecorator(AccountCreationViolationVerifier wrappee) {
        this.wrappee = wrappee;
    }

    @Override
    public List<String> verify(Account existingAccount, AccountCreationRequest request) {
        if (wrappee == null) {
            return new ArrayList<>();
        }
        return wrappee.verify(existingAccount, request);
    }
}
