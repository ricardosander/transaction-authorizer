package io.nubank.github.authorizer.transaction;

import io.nubank.github.authorizer.account.Account;

import java.time.LocalDateTime;
import java.util.List;

class AccountHighFrequencySmallIntervalDecorator extends TransactionCreationViolationVerifierDecorator {

    public AccountHighFrequencySmallIntervalDecorator(TransactionCreationViolationVerifier next) {
        super(next);
    }

    public List<String> verify(Account account, TransactionCreationRequest request) {
        List<String> violations = super.verify(account, request);
        if (account != null && isHighFrequencySmallInterval(account, request)) {
            violations.add("high-frequency-small-interval");
        }
        return violations;
    }

    private boolean isHighFrequencySmallInterval(Account account, TransactionCreationRequest request) {
        List<Transaction> accountTransactions = account.getTransactions();
        if (accountTransactions.size() < 3) {
            return false;
        }
        LocalDateTime limit = request.getTime().minusMinutes(2);
        return accountTransactions.stream().skip(accountTransactions.size() - 3).allMatch(t -> limit.isBefore(t.getTime()));
    }
}
