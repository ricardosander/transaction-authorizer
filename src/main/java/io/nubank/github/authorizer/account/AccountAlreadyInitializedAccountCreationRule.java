package io.nubank.github.authorizer.account;

import java.util.List;

class AccountAlreadyInitializedAccountCreationRule extends AccountCreationBaseRule {

    @Override
    public List<String> handle(Account account, AccountCreationRequest accountCreation) {
        List<String> violations = super.handle(account, accountCreation);
        if (account != null) {
            violations.add("account-already-initialized");
        }
        return violations;
    }
}
