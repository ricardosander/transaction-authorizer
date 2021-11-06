package io.nubank.github.authorizer.account;

import java.util.Collections;
import java.util.List;

class AccountCreationResult {

    private final boolean activeCard;
    private final int availableLimit;
    private final List<String> violations;

    AccountCreationResult(boolean activeCard, int availableLimit, List<String> violations) {
        this.activeCard = activeCard;
        this.availableLimit = availableLimit;
        this.violations = Collections.unmodifiableList(violations);
    }

    boolean isActiveCard() {
        return activeCard;
    }

    int getAvailableLimit() {
        return availableLimit;
    }

    public List<String> getViolations() {
        return violations;
    }
}
