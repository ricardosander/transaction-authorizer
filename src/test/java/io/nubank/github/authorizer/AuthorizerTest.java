package io.nubank.github.authorizer;

import io.nubank.github.authorizer.account.AccountCreation;
import io.nubank.github.authorizer.account.AccountRepository;
import io.nubank.github.authorizer.transaction.TransactionCreation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class AuthorizerTest {

    private Authorizer authorizer;

    @BeforeEach
    void setUp() {
        authorizer = new Authorizer(new AccountRepository());
    }

    @Test
    void shouldCreateAccountSuccessfully_whenCreateAccountIsTheOnlyOperation() {

        AccountCreation accountCreation = new AccountCreation(false, 750);
        List<OperationRequest> requests = List.of(accountCreation);

        List<OperationResult> results = authorizer.execute(requests);

        assertThat(results).isNotEmpty();
        assertThat(results.size()).isEqualTo(1);
        assertThat(results.get(0)).isNotNull();
        assertThat(results.get(0).getAccount()).isNotNull();
        assertThat(results.get(0).getAccount().isActiveCard()).isFalse();
        assertThat(results.get(0).getAccount().getAvailableLimit()).isEqualTo(750);
        assertThat(results.get(0).getViolations()).isEmpty();
    }

    @Test
    void shouldReturnViolation_whenCreateAccountButAccountAlreadyExists() {

        AccountCreation accountCreation = new AccountCreation(true, 175);
        AccountCreation secondAccountCreation = new AccountCreation(true, 350);
        List<OperationRequest> requests = List.of(accountCreation, secondAccountCreation);

        List<OperationResult> results = authorizer.execute(requests);

        assertThat(results).isNotEmpty();
        assertThat(results.size()).isEqualTo(2);

        assertThat(results.get(0)).isNotNull();
        assertThat(results.get(0).getAccount()).isNotNull();
        assertThat(results.get(0).getAccount().isActiveCard()).isTrue();
        assertThat(results.get(0).getAccount().getAvailableLimit()).isEqualTo(175);
        assertThat(results.get(0).getViolations()).isEmpty();

        assertThat(results.get(1)).isNotNull();
        assertThat(results.get(1).getAccount()).isNotNull();
        assertThat(results.get(1).getAccount().isActiveCard()).isTrue();
        assertThat(results.get(1).getAccount().getAvailableLimit()).isEqualTo(175);
        assertThat(results.get(1).getViolations()).isNotEmpty();
        assertThat(results.get(1).getViolations().size()).isEqualTo(1);
        assertThat(results.get(1).getViolations()).contains("account-already-initialized");
    }

    @Test
    void shouldCreateTransactionSuccessfully_whenAccountIsAlreadyCreated() {

        AccountCreation accountCreation = new AccountCreation(true, 100);

        LocalDateTime time = LocalDateTime.parse("2019-02-13T11:00:00.000");
        TransactionCreation transactionCreation = new TransactionCreation("Burger King", 20, time);

        List<OperationRequest> requests = List.of(accountCreation, transactionCreation);

        List<OperationResult> results = authorizer.execute(requests);

        assertThat(results).isNotEmpty();
        assertThat(results.size()).isEqualTo(2);

        assertThat(results.get(0)).isNotNull();
        assertThat(results.get(0).getAccount()).isNotNull();
        assertThat(results.get(0).getAccount().isActiveCard()).isTrue();
        assertThat(results.get(0).getAccount().getAvailableLimit()).isEqualTo(100);
        assertThat(results.get(0).getViolations()).isEmpty();

        assertThat(results.get(1)).isNotNull();
        assertThat(results.get(1).getAccount()).isNotNull();
        assertThat(results.get(1).getAccount().isActiveCard()).isTrue();
        assertThat(results.get(1).getAccount().getAvailableLimit()).isEqualTo(80);
        assertThat(results.get(1).getViolations()).isEmpty();
    }

    @Test
    void shouldReturnAccountNotInitializedViolation_whenAccountIsNotCreatedBeforeTransactionCreation() {

        LocalDateTime time = LocalDateTime.parse("2020-12-01T11:07:00.000");
        AccountCreation accountCreation = new AccountCreation(true, 225);
        TransactionCreation transaction = new TransactionCreation("Uber Eats", 25, time);

        List<OperationRequest> requests = List.of(transaction, accountCreation, transaction);

        List<OperationResult> results = authorizer.execute(requests);

        assertThat(results).isNotEmpty();
        assertThat(results.size()).isEqualTo(3);

        assertThat(results.get(0)).isNotNull();
        assertThat(results.get(0).getAccount()).isNull();
        assertThat(results.get(0).getViolations()).isNotEmpty();
        assertThat(results.get(0).getViolations()).contains("account-not-initialized");

        assertThat(results.get(1)).isNotNull();
        assertThat(results.get(1).getAccount()).isNotNull();
        assertThat(results.get(1).getAccount().isActiveCard()).isTrue();
        assertThat(results.get(1).getAccount().getAvailableLimit()).isEqualTo(225);
        assertThat(results.get(1).getViolations()).isEmpty();

        assertThat(results.get(2)).isNotNull();
        assertThat(results.get(2).getAccount()).isNotNull();
        assertThat(results.get(2).getAccount().isActiveCard()).isTrue();
        assertThat(results.get(2).getAccount().getAvailableLimit()).isEqualTo(200);
        assertThat(results.get(2).getViolations()).isEmpty();
    }
}