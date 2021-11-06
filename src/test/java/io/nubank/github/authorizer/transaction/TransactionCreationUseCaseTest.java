package io.nubank.github.authorizer.transaction;

import io.nubank.github.authorizer.account.Account;
import io.nubank.github.authorizer.account.AccountCreation;
import io.nubank.github.authorizer.account.AccountCreationUseCase;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class TransactionCreationUseCaseTest {

    @Test
    void shouldReturnViolation_whenAccountIsNotCreated() {

        TransactionCreationUseCase target = new TransactionCreationUseCase();

        LocalDateTime time = LocalDateTime.parse("2020-12-01T11:07:00.000");
        TransactionCreation transaction = new TransactionCreation("Uber Eats", 25, time);

        TransactionCreationResult result = target.execute(transaction);

        assertThat(result).isNotNull();
        assertThat(result.getState()).isEmpty();
        assertThat(result.getViolations()).isNotEmpty();
        assertThat(result.getViolations().size()).isEqualTo(1);
        assertThat(result.getViolations()).contains("account-not-initialized");
    }

    @Test
    void shouldProcessTransaction_whenThereIsNoViolations() {

        AccountCreation accountCreation = new AccountCreation(true, 100);
        Account account = (new AccountCreationUseCase().execute(accountCreation)).getState();
        TransactionCreationUseCase target = new TransactionCreationUseCase(account);

        LocalDateTime time = LocalDateTime.parse("2019-02-13T11:00:00.000");
        TransactionCreation transaction = new TransactionCreation("Burger King", 20, time);

        TransactionCreationResult result = target.execute(transaction);

        assertThat(result).isNotNull();
        assertThat(result.getState()).isNotEmpty();
        assertThat(result.getState().get().getAvailableLimit()).isEqualTo(80);
        assertThat(result.getViolations()).isEmpty();
    }
}