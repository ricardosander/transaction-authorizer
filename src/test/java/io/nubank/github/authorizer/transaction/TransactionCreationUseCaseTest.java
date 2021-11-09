package io.nubank.github.authorizer.transaction;

import io.nubank.github.authorizer.OperationResult;
import io.nubank.github.authorizer.account.Account;
import io.nubank.github.authorizer.account.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TransactionCreationUseCaseTest {

    private AccountRepository accountRepository;

    private TransactionCreationUseCase target;

    @BeforeEach
    void setUp() {
        accountRepository = mock(AccountRepository.class);
        target = new TransactionCreationUseCase(accountRepository);
    }

    @Test
    void shouldReturnViolation_whenAccountIsNotCreated() {

        LocalDateTime time = LocalDateTime.parse("2020-12-01T11:07:00.000");
        TransactionCreation transaction = new TransactionCreation("Uber Eats", 25, time);

        OperationResult result = target.execute(transaction);

        assertThat(result).isNotNull();
        assertThat(result.getAccount()).isNull();
        assertThat(result.getViolations()).isNotEmpty();
        assertThat(result.getViolations().size()).isEqualTo(1);
        assertThat(result.getViolations()).contains("account-not-initialized");
    }

    @Test
    void shouldProcessTransaction_whenThereIsNoViolations() {

        Account account = new Account(true, 100);
        when(accountRepository.getAccount()).thenReturn(account);

        LocalDateTime time = LocalDateTime.parse("2019-02-13T11:00:00.000");
        TransactionCreation transaction = new TransactionCreation("Burger King", 20, time);

        OperationResult result = target.execute(transaction);

        assertThat(result).isNotNull();
        assertThat(result.getAccount()).isNotNull();
        assertThat(result.getAccount().isActiveCard()).isTrue();
        assertThat(result.getAccount().getAvailableLimit()).isEqualTo(80);
        assertThat(result.getViolations()).isEmpty();
    }

    @Test
    void shouldReturnViolation_whenAccountCardIsInactive() {

        Account account = new Account(false, 100);
        when(accountRepository.getAccount()).thenReturn(account);

        LocalDateTime time = LocalDateTime.parse("2019-02-13T11:00:00.000");
        TransactionCreation transaction = new TransactionCreation("Burger King", 20, time);

        OperationResult result = target.execute(transaction);

        assertThat(result).isNotNull();
        assertThat(result.getAccount()).isNotNull();
        assertThat(result.getAccount().isActiveCard()).isFalse();
        assertThat(result.getAccount().getAvailableLimit()).isEqualTo(100);
        assertThat(result.getViolations()).isNotEmpty();
        assertThat(result.getViolations().size()).isEqualTo(1);
        assertThat(result.getViolations()).contains("card-not-active");
    }

    @Test
    void shouldReturnViolation_whenAccountHasInsufficientLimit() {

        Account account = new Account(true, 1000);
        when(accountRepository.getAccount()).thenReturn(account);

        LocalDateTime time = LocalDateTime.parse("2019-02-13T11:00:00.000");
        TransactionCreation transaction = new TransactionCreation("Vivara", 1250, time);

        OperationResult result = target.execute(transaction);

        assertThat(result).isNotNull();
        assertThat(result.getAccount()).isNotNull();
        assertThat(result.getAccount().isActiveCard()).isTrue();
        assertThat(result.getAccount().getAvailableLimit()).isEqualTo(1000);
        assertThat(result.getViolations()).isNotEmpty();
        assertThat(result.getViolations().size()).isEqualTo(1);
        assertThat(result.getViolations()).contains("insufficient-limit");
    }

    @Test
    void shouldReturnViolation_whenAccountHasInsufficientLimitOnSecondTransaction() {

        Account account = new Account(true, 1000);
        when(accountRepository.getAccount()).thenReturn(account);

        LocalDateTime time = LocalDateTime.parse("2019-02-13T11:00:00.000");
        TransactionCreation firstTransaction = new TransactionCreation("Vivara", 250, time);

        OperationResult firstTransactionResult = target.execute(firstTransaction);

        assertThat(firstTransactionResult).isNotNull();
        assertThat(firstTransactionResult.getAccount()).isNotNull();
        assertThat(firstTransactionResult.getAccount().isActiveCard()).isTrue();
        assertThat(firstTransactionResult.getAccount().getAvailableLimit()).isEqualTo(750);
        assertThat(firstTransactionResult.getViolations()).isEmpty();

        time = LocalDateTime.parse("2019-02-13T11:00:01.000");
        TransactionCreation secondTransaction = new TransactionCreation("Samsung", 800, time);
        OperationResult secondTransactionResult = target.execute(secondTransaction);

        assertThat(secondTransactionResult).isNotNull();
        assertThat(secondTransactionResult.getAccount()).isNotNull();
        assertThat(secondTransactionResult.getAccount().isActiveCard()).isTrue();
        assertThat(secondTransactionResult.getAccount().getAvailableLimit()).isEqualTo(750);
        assertThat(secondTransactionResult.getViolations()).isNotEmpty();
        assertThat(secondTransactionResult.getViolations().size()).isEqualTo(1);
        assertThat(secondTransactionResult.getViolations()).contains("insufficient-limit");
    }

    @Test
    void shouldReturnViolation_whenAccountHasInsufficientLimitOnThirdTransaction() {

        Account account = new Account(true, 1000);
        when(accountRepository.getAccount()).thenReturn(account);

        LocalDateTime time = LocalDateTime.parse("2019-02-13T11:00:00.000");
        TransactionCreation firstTransaction = new TransactionCreation("Vivara", 250, time);

        OperationResult firstTransactionResult = target.execute(firstTransaction);

        assertThat(firstTransactionResult).isNotNull();
        assertThat(firstTransactionResult.getAccount()).isNotNull();
        assertThat(firstTransactionResult.getAccount().isActiveCard()).isTrue();
        assertThat(firstTransactionResult.getAccount().getAvailableLimit()).isEqualTo(750);
        assertThat(firstTransactionResult.getViolations()).isEmpty();

        time = LocalDateTime.parse("2019-02-13T11:00:01.000");
        TransactionCreation secondTransaction = new TransactionCreation("Samsung", 500, time);
        OperationResult secondTransactionResult = target.execute(secondTransaction);

        assertThat(secondTransactionResult).isNotNull();
        assertThat(secondTransactionResult.getAccount()).isNotNull();
        assertThat(secondTransactionResult.getAccount().isActiveCard()).isTrue();
        assertThat(secondTransactionResult.getAccount().getAvailableLimit()).isEqualTo(250);
        assertThat(secondTransactionResult.getViolations()).isEmpty();

        time = LocalDateTime.parse("2019-02-13T11:01:01.000");
        TransactionCreation thirdTransaction = new TransactionCreation("Nike", 800, time);
        OperationResult thirdTransactionResult = target.execute(thirdTransaction);

        assertThat(thirdTransactionResult).isNotNull();
        assertThat(thirdTransactionResult.getAccount()).isNotNull();
        assertThat(thirdTransactionResult.getAccount().isActiveCard()).isTrue();
        assertThat(thirdTransactionResult.getAccount().getAvailableLimit()).isEqualTo(250);
        assertThat(thirdTransactionResult.getViolations()).isNotEmpty();
        assertThat(thirdTransactionResult.getViolations().size()).isEqualTo(1);
        assertThat(thirdTransactionResult.getViolations()).contains("insufficient-limit");
    }

    @Test
    void shouldReturnViolation_whenHighFrequencySmallIntervalIsDetected() {

        Account account = new Account(true, 100);
        when(accountRepository.getAccount()).thenReturn(account);

        LocalDateTime time = LocalDateTime.parse("2019-02-13T11:00:00.000");
        TransactionCreation firstTransaction = new TransactionCreation("Burger King", 20, time);

        OperationResult firstTransactionResult = target.execute(firstTransaction);

        assertThat(firstTransactionResult).isNotNull();
        assertThat(firstTransactionResult.getAccount()).isNotNull();
        assertThat(firstTransactionResult.getAccount().isActiveCard()).isTrue();
        assertThat(firstTransactionResult.getAccount().getAvailableLimit()).isEqualTo(80);
        assertThat(firstTransactionResult.getViolations()).isEmpty();

        time = LocalDateTime.parse("2019-02-13T11:00:01.000");
        TransactionCreation secondTransaction = new TransactionCreation("Habbib's", 20, time);
        OperationResult secondTransactionResult = target.execute(secondTransaction);

        assertThat(secondTransactionResult).isNotNull();
        assertThat(secondTransactionResult.getAccount()).isNotNull();
        assertThat(secondTransactionResult.getAccount().isActiveCard()).isTrue();
        assertThat(secondTransactionResult.getAccount().getAvailableLimit()).isEqualTo(60);
        assertThat(secondTransactionResult.getViolations()).isEmpty();

        time = LocalDateTime.parse("2019-02-13T11:01:01.000");
        TransactionCreation thirdTransaction = new TransactionCreation("McDonald's", 20, time);
        OperationResult thirdTransactionResult = target.execute(thirdTransaction);

        assertThat(thirdTransactionResult).isNotNull();
        assertThat(thirdTransactionResult.getAccount()).isNotNull();
        assertThat(thirdTransactionResult.getAccount().isActiveCard()).isTrue();
        assertThat(thirdTransactionResult.getAccount().getAvailableLimit()).isEqualTo(40);
        assertThat(thirdTransactionResult.getViolations()).isEmpty();

        time = LocalDateTime.parse("2019-02-13T11:01:31.000");
        TransactionCreation fourthTransaction = new TransactionCreation("Subway", 20, time);
        OperationResult fourthTransactionResult = target.execute(fourthTransaction);

        assertThat(fourthTransactionResult).isNotNull();
        assertThat(fourthTransactionResult.getAccount()).isNotNull();
        assertThat(fourthTransactionResult.getAccount().isActiveCard()).isTrue();
        assertThat(fourthTransactionResult.getAccount().getAvailableLimit()).isEqualTo(40);
        assertThat(fourthTransactionResult.getViolations()).isNotEmpty();
        assertThat(fourthTransactionResult.getViolations().size()).isEqualTo(1);
        assertThat(fourthTransactionResult.getViolations()).contains("high-frequency-small-interval");

        time = LocalDateTime.parse("2019-02-13T12:00:00.000");
        TransactionCreation fifthTransaction = new TransactionCreation("Burger King", 10, time);
        OperationResult fifthTransactionResult = target.execute(fifthTransaction);

        assertThat(fifthTransactionResult).isNotNull();
        assertThat(fifthTransactionResult.getAccount()).isNotNull();
        assertThat(fifthTransactionResult.getAccount().isActiveCard()).isTrue();
        assertThat(fifthTransactionResult.getAccount().getAvailableLimit()).isEqualTo(30);
        assertThat(fifthTransactionResult.getViolations()).isEmpty();
    }

    @Test
    void shouldReturnViolation_whenDoubledTransactionIsDetected() {

        Account account = new Account(true, 100);
        when(accountRepository.getAccount()).thenReturn(account);

        LocalDateTime time = LocalDateTime.parse("2019-02-13T11:00:00.000");
        TransactionCreation firstTransaction = new TransactionCreation("Burger King", 20, time);

        OperationResult firstTransactionResult = target.execute(firstTransaction);

        assertThat(firstTransactionResult).isNotNull();
        assertThat(firstTransactionResult.getAccount()).isNotNull();
        assertThat(firstTransactionResult.getAccount().isActiveCard()).isTrue();
        assertThat(firstTransactionResult.getAccount().getAvailableLimit()).isEqualTo(80);
        assertThat(firstTransactionResult.getViolations()).isEmpty();

        time = LocalDateTime.parse("2019-02-13T11:00:01.000");
        TransactionCreation secondTransaction = new TransactionCreation("McDonald's", 10, time);
        OperationResult secondTransactionResult = target.execute(secondTransaction);

        assertThat(secondTransactionResult).isNotNull();
        assertThat(secondTransactionResult.getAccount()).isNotNull();
        assertThat(secondTransactionResult.getAccount().isActiveCard()).isTrue();
        assertThat(secondTransactionResult.getAccount().getAvailableLimit()).isEqualTo(70);
        assertThat(secondTransactionResult.getViolations()).isEmpty();

        time = LocalDateTime.parse("2019-02-13T11:00:02.000");
        TransactionCreation thirdTransaction = new TransactionCreation("Burger King", 20, time);
        OperationResult thirdTransactionResult = target.execute(thirdTransaction);

        assertThat(thirdTransactionResult).isNotNull();
        assertThat(thirdTransactionResult.getAccount()).isNotNull();
        assertThat(thirdTransactionResult.getAccount().isActiveCard()).isTrue();
        assertThat(thirdTransactionResult.getAccount().getAvailableLimit()).isEqualTo(70);
        assertThat(thirdTransactionResult.getViolations()).isNotEmpty();
        assertThat(thirdTransactionResult.getViolations()).contains("doubled-transaction");

        time = LocalDateTime.parse("2019-02-13T11:00:03.000");
        TransactionCreation fourthTransaction = new TransactionCreation("Burger King", 15, time);
        OperationResult fourthTransactionResult = target.execute(fourthTransaction);

        assertThat(fourthTransactionResult).isNotNull();
        assertThat(fourthTransactionResult.getAccount()).isNotNull();
        assertThat(fourthTransactionResult.getAccount().isActiveCard()).isTrue();
        assertThat(fourthTransactionResult.getAccount().getAvailableLimit()).isEqualTo(55);
        assertThat(fourthTransactionResult.getViolations()).isEmpty();
    }

    @Test
    void shouldReturnViolations_whenMultipleRulesAreViolated() {

        Account account = new Account(true, 100);
        when(accountRepository.getAccount()).thenReturn(account);

        LocalDateTime time = LocalDateTime.parse("2019-02-13T11:00:01.000");
        TransactionCreation transaction = new TransactionCreation("McDonald's", 10, time);
        OperationResult result = target.execute(transaction);

        assertThat(result).isNotNull();
        assertThat(result.getAccount()).isNotNull();
        assertThat(result.getAccount().isActiveCard()).isTrue();
        assertThat(result.getAccount().getAvailableLimit()).isEqualTo(90);
        assertThat(result.getViolations()).isEmpty();

        time = LocalDateTime.parse("2019-02-13T11:00:02.000");
        transaction = new TransactionCreation("Burger King", 20, time);
        result = target.execute(transaction);

        assertThat(result).isNotNull();
        assertThat(result.getAccount()).isNotNull();
        assertThat(result.getAccount().isActiveCard()).isTrue();
        assertThat(result.getAccount().getAvailableLimit()).isEqualTo(70);
        assertThat(result.getViolations()).isEmpty();

        time = LocalDateTime.parse("2019-02-13T11:00:07.000");
        transaction = new TransactionCreation("Burger King", 5, time);
        result = target.execute(transaction);

        assertThat(result).isNotNull();
        assertThat(result.getAccount()).isNotNull();
        assertThat(result.getAccount().isActiveCard()).isTrue();
        assertThat(result.getAccount().getAvailableLimit()).isEqualTo(65);
        assertThat(result.getViolations()).isEmpty();

        time = LocalDateTime.parse("2019-02-13T11:00:08.000");
        transaction = new TransactionCreation("Burger King", 5, time);
        result = target.execute(transaction);

        assertThat(result).isNotNull();
        assertThat(result.getAccount()).isNotNull();
        assertThat(result.getAccount().isActiveCard()).isTrue();
        assertThat(result.getAccount().getAvailableLimit()).isEqualTo(65);
        assertThat(result.getViolations()).isNotEmpty();
        assertThat(result.getViolations().size()).isEqualTo(2);
        assertThat(result.getViolations()).contains("high-frequency-small-interval");
        assertThat(result.getViolations()).contains("doubled-transaction");

        time = LocalDateTime.parse("2019-02-13T11:00:18.000");
        transaction = new TransactionCreation("Burger King", 150, time);
        result = target.execute(transaction);

        assertThat(result).isNotNull();
        assertThat(result.getAccount()).isNotNull();
        assertThat(result.getAccount().isActiveCard()).isTrue();
        assertThat(result.getAccount().getAvailableLimit()).isEqualTo(65);
        assertThat(result.getViolations()).isNotEmpty();
        assertThat(result.getViolations().size()).isEqualTo(2);
        assertThat(result.getViolations()).contains("insufficient-limit");
        assertThat(result.getViolations()).contains("high-frequency-small-interval");

        time = LocalDateTime.parse("2019-02-13T11:00:22.000");
        transaction = new TransactionCreation("Burger King", 190, time);
        result = target.execute(transaction);

        assertThat(result).isNotNull();
        assertThat(result.getAccount()).isNotNull();
        assertThat(result.getAccount().isActiveCard()).isTrue();
        assertThat(result.getAccount().getAvailableLimit()).isEqualTo(65);
        assertThat(result.getViolations()).isNotEmpty();
        assertThat(result.getViolations().size()).isEqualTo(2);
        assertThat(result.getViolations()).contains("insufficient-limit");
        assertThat(result.getViolations()).contains("high-frequency-small-interval");

        time = LocalDateTime.parse("2019-02-13T12:00:27.000");
        transaction = new TransactionCreation("Burger King", 15, time);
        result = target.execute(transaction);

        assertThat(result).isNotNull();
        assertThat(result.getAccount()).isNotNull();
        assertThat(result.getAccount().isActiveCard()).isTrue();
        assertThat(result.getAccount().getAvailableLimit()).isEqualTo(50);
        assertThat(result.getViolations()).isEmpty();
    }
}