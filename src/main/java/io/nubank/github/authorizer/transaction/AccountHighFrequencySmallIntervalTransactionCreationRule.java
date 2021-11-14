package io.nubank.github.authorizer.transaction;

import io.nubank.github.authorizer.account.Account;

import java.time.LocalDateTime;
import java.util.List;

class AccountHighFrequencySmallIntervalTransactionCreationRule extends TransactionCreationBaseRule {

    public List<String> handle(Account account, TransactionCreation request) {
        List<String> violations = super.handle(account, request);
        if (account != null && isHighFrequencySmallInterval(account, request)) {
            violations.add("high-frequency-small-interval");
        }
        return violations;
    }

    private boolean isHighFrequencySmallInterval(Account account, TransactionCreation request) {
        List<Transaction> accountTransactions = account.getTransactions();
        if (accountTransactions.size() < 3) {
            return false;
        }
        LocalDateTime limit = request.getTime().minusMinutes(2);
        return accountTransactions.stream().skip(accountTransactions.size() - 3).allMatch(t -> limit.isBefore(t.getTime()));
    }
}
