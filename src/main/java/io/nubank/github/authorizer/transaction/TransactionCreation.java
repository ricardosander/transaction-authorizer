package io.nubank.github.authorizer.transaction;

import java.time.LocalDateTime;

class TransactionCreation {

    private final String merchant;
    private final int amount;
    private final LocalDateTime time;

    TransactionCreation(String merchant, int amount, LocalDateTime time) {
        this.merchant = merchant;
        this.amount = amount;
        this.time = time;
    }
}
