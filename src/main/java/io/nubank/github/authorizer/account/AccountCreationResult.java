package io.nubank.github.authorizer.account;

import java.util.Collections;
import java.util.List;

public class AccountCreationResult {

    private final Account state;
    private final List<String> violations;

    AccountCreationResult(Account state, List<String> violations) {
        this.state = state;
        this.violations = Collections.unmodifiableList(violations);
    }

    public Account getState() {
        return state;
    }

    List<String> getViolations() {
        return violations;
    }
}
