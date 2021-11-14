package io.nubank.github.authorizer.transaction;

import io.nubank.github.authorizer.account.Account;

import java.time.LocalDateTime;
import java.util.List;

class AccountDoubledTransactionTransactionCreationRule extends TransactionCreationBaseRule {

    public List<String> handle(Account account, TransactionCreationRequest request) {
        List<String> violations = super.handle(account, request);
        if (account != null && isDoubledTransaction(account, request)) {
            violations.add("doubled-transaction");
        }
        return violations;
    }

    private boolean isDoubledTransaction(Account account, TransactionCreationRequest request) {
        List<Transaction> accountTransactions = account.getTransactions();
        if (accountTransactions.isEmpty()) {
            return false;
        }
        LocalDateTime limit = request.getTime().minusMinutes(2);
        return accountTransactions.stream()
                .anyMatch(transaction -> isDoubleTransaction(request, limit, transaction));
    }

    private boolean isDoubleTransaction(TransactionCreationRequest request, LocalDateTime limit, Transaction transaction) {
        return transaction.getMerchant().equals(request.getMerchant())
                && transaction.getAmount() == request.getAmount()
                && limit.isBefore(transaction.getTime());
    }
}
