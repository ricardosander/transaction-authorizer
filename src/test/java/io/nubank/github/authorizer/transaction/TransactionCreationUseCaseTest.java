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
        assertThat(result.getState().get().isActiveCard()).isTrue();
        assertThat(result.getState().get().getAvailableLimit()).isEqualTo(80);
        assertThat(result.getViolations()).isEmpty();
    }

    @Test
    void shouldReturnViolation_whenAccountCardIsInactive() {

        AccountCreation accountCreation = new AccountCreation(false, 100);
        Account account = (new AccountCreationUseCase().execute(accountCreation)).getState();
        TransactionCreationUseCase target = new TransactionCreationUseCase(account);

        LocalDateTime time = LocalDateTime.parse("2019-02-13T11:00:00.000");
        TransactionCreation transaction = new TransactionCreation("Burger King", 20, time);

        TransactionCreationResult result = target.execute(transaction);

        assertThat(result).isNotNull();
        assertThat(result.getState()).isNotEmpty();
        assertThat(result.getState().get().isActiveCard()).isFalse();
        assertThat(result.getState().get().getAvailableLimit()).isEqualTo(100);
        assertThat(result.getViolations()).isNotEmpty();
        assertThat(result.getViolations().size()).isEqualTo(1);
        assertThat(result.getViolations()).contains("card-not-active");
    }

    @Test
    void shouldReturnViolation_whenAccountHasInsufficientLimit() {

        AccountCreation accountCreation = new AccountCreation(true, 1000);
        Account account = (new AccountCreationUseCase().execute(accountCreation)).getState();
        TransactionCreationUseCase target = new TransactionCreationUseCase(account);

        LocalDateTime time = LocalDateTime.parse("2019-02-13T11:00:00.000");
        TransactionCreation transaction = new TransactionCreation("Vivara", 1250, time);

        TransactionCreationResult result = target.execute(transaction);

        assertThat(result).isNotNull();
        assertThat(result.getState()).isNotEmpty();
        assertThat(result.getState().get().isActiveCard()).isTrue();
        assertThat(result.getState().get().getAvailableLimit()).isEqualTo(1000);
        assertThat(result.getViolations()).isNotEmpty();
        assertThat(result.getViolations().size()).isEqualTo(1);
        assertThat(result.getViolations()).contains("insufficient-limit");
    }

    @Test
    void shouldReturnViolation_whenAccountHasInsufficientLimitOnSecondTransaction() {

        AccountCreation accountCreation = new AccountCreation(true, 1000);
        Account account = (new AccountCreationUseCase().execute(accountCreation)).getState();
        TransactionCreationUseCase target = new TransactionCreationUseCase(account);

        LocalDateTime time = LocalDateTime.parse("2019-02-13T11:00:00.000");
        TransactionCreation firstTransaction = new TransactionCreation("Vivara", 250, time);

        TransactionCreationResult firstTransactionResult = target.execute(firstTransaction);

        assertThat(firstTransactionResult).isNotNull();
        assertThat(firstTransactionResult.getState()).isNotEmpty();
        assertThat(firstTransactionResult.getState().get().isActiveCard()).isTrue();
        assertThat(firstTransactionResult.getState().get().getAvailableLimit()).isEqualTo(750);
        assertThat(firstTransactionResult.getViolations()).isEmpty();

        time = LocalDateTime.parse("2019-02-13T11:00:01.000");
        TransactionCreation secondTransaction = new TransactionCreation("Samsung", 800, time);
        TransactionCreationResult secondTransactionResult = target.execute(secondTransaction);

        assertThat(secondTransactionResult).isNotNull();
        assertThat(secondTransactionResult.getState()).isNotEmpty();
        assertThat(secondTransactionResult.getState().get().isActiveCard()).isTrue();
        assertThat(secondTransactionResult.getState().get().getAvailableLimit()).isEqualTo(750);
        assertThat(secondTransactionResult.getViolations()).isNotEmpty();
        assertThat(secondTransactionResult.getViolations().size()).isEqualTo(1);
        assertThat(secondTransactionResult.getViolations()).contains("insufficient-limit");
    }

    @Test
    void shouldReturnViolation_whenAccountHasInsufficientLimitOnThirdTransaction() {

        AccountCreation accountCreation = new AccountCreation(true, 1000);
        Account account = (new AccountCreationUseCase().execute(accountCreation)).getState();
        TransactionCreationUseCase target = new TransactionCreationUseCase(account);

        LocalDateTime time = LocalDateTime.parse("2019-02-13T11:00:00.000");
        TransactionCreation firstTransaction = new TransactionCreation("Vivara", 250, time);

        TransactionCreationResult firstTransactionResult = target.execute(firstTransaction);

        assertThat(firstTransactionResult).isNotNull();
        assertThat(firstTransactionResult.getState()).isNotEmpty();
        assertThat(firstTransactionResult.getState().get().isActiveCard()).isTrue();
        assertThat(firstTransactionResult.getState().get().getAvailableLimit()).isEqualTo(750);
        assertThat(firstTransactionResult.getViolations()).isEmpty();

        time = LocalDateTime.parse("2019-02-13T11:00:01.000");
        TransactionCreation secondTransaction = new TransactionCreation("Samsung", 500, time);
        TransactionCreationResult secondTransactionResult = target.execute(secondTransaction);

        assertThat(secondTransactionResult).isNotNull();
        assertThat(secondTransactionResult.getState()).isNotEmpty();
        assertThat(secondTransactionResult.getState().get().isActiveCard()).isTrue();
        assertThat(secondTransactionResult.getState().get().getAvailableLimit()).isEqualTo(250);
        assertThat(secondTransactionResult.getViolations()).isEmpty();

        time = LocalDateTime.parse("2019-02-13T11:01:01.000");
        TransactionCreation thirdTransaction = new TransactionCreation("Nike", 800, time);
        TransactionCreationResult thirdTransactionResult = target.execute(thirdTransaction);

        assertThat(thirdTransactionResult).isNotNull();
        assertThat(thirdTransactionResult.getState()).isNotEmpty();
        assertThat(thirdTransactionResult.getState().get().isActiveCard()).isTrue();
        assertThat(thirdTransactionResult.getState().get().getAvailableLimit()).isEqualTo(250);
        assertThat(thirdTransactionResult.getViolations()).isNotEmpty();
        assertThat(thirdTransactionResult.getViolations().size()).isEqualTo(1);
        assertThat(thirdTransactionResult.getViolations()).contains("insufficient-limit");
    }
}