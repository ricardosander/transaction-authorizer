package io.nubank.github.authorizer;

import java.util.Collections;
import java.util.List;

public class OperationResult {

    private final AccountResult state;
    private final List<String> violations;

    public OperationResult(AccountResult state, List<String> violations) {
        this.state = state;
        this.violations = Collections.unmodifiableList(violations);
    }

    AccountResult getAccount() {
        return state;
    }

    List<String> getViolations() {
        return violations;
    }
}
