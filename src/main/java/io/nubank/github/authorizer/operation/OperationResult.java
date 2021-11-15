package io.nubank.github.authorizer.operation;

import java.util.Collections;
import java.util.List;

public class OperationResult {

    private final AccountResult state;
    private final List<String> violations;

    public OperationResult(AccountResult state, List<String> violations) {
        this.state = state;
        this.violations = Collections.unmodifiableList(violations);
    }

    public AccountResult getAccount() {
        return state;
    }

    public List<String> getViolations() {
        return violations;
    }
}
