package io.nubank.github.authorizer.account;

import java.util.Collections;
import java.util.List;

class AccountCreationResult {

    private final Account account;
    private final List<String> violations;

    AccountCreationResult(Account account, List<String> violations) {
        this.account = account;
        this.violations = Collections.unmodifiableList(violations);
    }

    public Account getAccount() {
        return account;
    }

    public List<String> getViolations() {
        return violations;
    }
}
