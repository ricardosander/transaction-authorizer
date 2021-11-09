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

        AccountCreation account = buildAccountCreation(false, 750);
        List<OperationRequest> requests = List.of(account);

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

        AccountCreation account = buildAccountCreation(true, 175);
        AccountCreation secondAccount = buildAccountCreation(true, 350);
        List<OperationRequest> requests = List.of(account, secondAccount);

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

        AccountCreation account = buildAccountCreation(true, 100);
        TransactionCreation transaction = buildTransactionCreation("Burger King", 20, "2019-02-13T11:00:00.000");
        List<OperationRequest> requests = List.of(account, transaction);

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

        AccountCreation account = buildAccountCreation(true, 225);
        TransactionCreation transaction = buildTransactionCreation("Uber Eats", 25, "2020-12-01T11:07:00.000");
        List<OperationRequest> requests = List.of(transaction, account, transaction);
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

    @Test
    void shouldReturnCardNotActiveViolation_whenTryToCreateTransactionAndCardAccountIsInactive() {

        AccountCreation account = buildAccountCreation(false, 100);
        TransactionCreation burgerKing = buildTransactionCreation("Burger King", 20, "2019-02-13T11:00:00.000");
        TransactionCreation habbibs = buildTransactionCreation("Habbib's", 15, "2019-02-13T11:15:00.000");
        List<OperationRequest> requests = List.of(account, burgerKing, habbibs);

        List<OperationResult> results = authorizer.execute(requests);

        assertThat(results).isNotEmpty();
        assertThat(results.size()).isEqualTo(3);

        assertThat(results.get(0).getAccount()).isNotNull();
        assertThat(results.get(0).getAccount().isActiveCard()).isFalse();
        assertThat(results.get(0).getAccount().getAvailableLimit()).isEqualTo(100);
        assertThat(results.get(0).getViolations()).isEmpty();

        assertThat(results.get(1).getAccount()).isNotNull();
        assertThat(results.get(1).getAccount().isActiveCard()).isFalse();
        assertThat(results.get(1).getAccount().getAvailableLimit()).isEqualTo(100);
        assertThat(results.get(1).getViolations()).isNotEmpty();
        assertThat(results.get(1).getViolations()).contains("card-not-active");

        assertThat(results.get(2).getAccount()).isNotNull();
        assertThat(results.get(2).getAccount().isActiveCard()).isFalse();
        assertThat(results.get(2).getAccount().getAvailableLimit()).isEqualTo(100);
        assertThat(results.get(2).getViolations()).isNotEmpty();
        assertThat(results.get(2).getViolations()).contains("card-not-active");
    }

    @Test
    void shouldReturnInsufficientLimitViolation_whenTransactionExceedsAccountLimit() {

        AccountCreation account = buildAccountCreation(true, 1000);
        TransactionCreation vivara = buildTransactionCreation("Vivara", 1250, "2019-02-13T11:00:00.000");
        TransactionCreation samsung = buildTransactionCreation("Samsung", 2500, "2019-02-13T11:00:01.000");
        TransactionCreation nike = buildTransactionCreation("Nike", 800, "2019-02-13T11:01:01.000");
        List<OperationRequest> requests = List.of(account, vivara, samsung, nike);

        List<OperationResult> results = authorizer.execute(requests);

        assertThat(results).isNotEmpty();
        assertThat(results.size()).isEqualTo(4);

        assertThat(results.get(0).getAccount()).isNotNull();
        assertThat(results.get(0).getAccount().isActiveCard()).isTrue();
        assertThat(results.get(0).getAccount().getAvailableLimit()).isEqualTo(1000);
        assertThat(results.get(0).getViolations()).isEmpty();

        assertThat(results.get(1).getAccount()).isNotNull();
        assertThat(results.get(1).getAccount().isActiveCard()).isTrue();
        assertThat(results.get(1).getAccount().getAvailableLimit()).isEqualTo(1000);
        assertThat(results.get(1).getViolations()).isNotEmpty();
        assertThat(results.get(1).getViolations()).contains("insufficient-limit");

        assertThat(results.get(2).getAccount()).isNotNull();
        assertThat(results.get(2).getAccount().isActiveCard()).isTrue();
        assertThat(results.get(2).getAccount().getAvailableLimit()).isEqualTo(1000);
        assertThat(results.get(2).getViolations()).isNotEmpty();
        assertThat(results.get(2).getViolations()).contains("insufficient-limit");

        assertThat(results.get(3).getAccount()).isNotNull();
        assertThat(results.get(3).getAccount().isActiveCard()).isTrue();
        assertThat(results.get(3).getAccount().getAvailableLimit()).isEqualTo(200);
        assertThat(results.get(3).getViolations()).isEmpty();
    }

    private AccountCreation buildAccountCreation(boolean isCardActive, int availableLimit) {
        return new AccountCreation(isCardActive, availableLimit);
    }

    private TransactionCreation buildTransactionCreation(String merchant, int amount, String time) {
        return new TransactionCreation(merchant, amount, buildTime(time));
    }

    private LocalDateTime buildTime(String time) {
        return LocalDateTime.parse(time);
    }
}