package io.nubank.github.authorizer.transaction;

import io.nubank.github.authorizer.account.Account;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

class TransactionCreationResult {

    private final Account state;
    private final List<String> violations;

    TransactionCreationResult(Account state, List<String> violations) {
        this.state = state;
        this.violations = Collections.unmodifiableList(violations);
    }

    public Optional<Account> getState() {
        return Optional.ofNullable(state);
    }

    public List<String> getViolations() {
        return violations;
    }
}
