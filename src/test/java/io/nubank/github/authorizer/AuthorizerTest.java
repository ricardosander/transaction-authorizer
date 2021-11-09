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

        AccountCreation account1 = buildAccountCreation(true, 175);
        AccountCreation account2 = buildAccountCreation(true, 350);
        List<OperationRequest> requests = List.of(account1, account2);

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
        TransactionCreation transaction = buildTransactionCreation("Burger King", 20, "2019-02-13T11:00:00");
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
        TransactionCreation transaction = buildTransactionCreation("Uber Eats", 25, "2020-12-01T11:07:00");
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
        TransactionCreation transaction1 = buildTransactionCreation("Burger King", 20, "2019-02-13T11:00:00");
        TransactionCreation transaction2 = buildTransactionCreation("Habbib's", 15, "2019-02-13T11:15:00");
        List<OperationRequest> requests = List.of(account, transaction1, transaction2);

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
        TransactionCreation transaction1 = buildTransactionCreation("Vivara", 1250, "2019-02-13T11:00:00");
        TransactionCreation transaction2 = buildTransactionCreation("Samsung", 2500, "2019-02-13T11:00:01");
        TransactionCreation transaction3 = buildTransactionCreation("Nike", 800, "2019-02-13T11:01:01");
        List<OperationRequest> requests = List.of(account, transaction1, transaction2, transaction3);

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

    @Test
    void shouldReturnHighFrequencySmallIntervalViolation_whenFourTransactionAreMadeInLessThenTowMinutes() {

        AccountCreation account = buildAccountCreation(true, 100);
        TransactionCreation transaction1 = buildTransactionCreation("Burger King", 20, "2019-02-13T11:00:00");
        TransactionCreation transaction2 = buildTransactionCreation("Habbib's", 20, "2019-02-13T11:00:01");
        TransactionCreation transaction3 = buildTransactionCreation("McDonald's", 20, "2019-02-13T11:01:01");
        TransactionCreation transaction4 = buildTransactionCreation("Subway", 20, "2019-02-13T11:01:31");
        TransactionCreation transaction5 = buildTransactionCreation("Burger King", 10, "2019-02-13T12:00:00");
        List<OperationRequest> requests = List.of(account, transaction1, transaction2, transaction3, transaction4, transaction5);

        List<OperationResult> results = authorizer.execute(requests);

        assertThat(results).isNotEmpty();
        assertThat(results.size()).isEqualTo(6);

        assertThat(results.get(0).getAccount()).isNotNull();
        assertThat(results.get(0).getAccount().isActiveCard()).isTrue();
        assertThat(results.get(0).getAccount().getAvailableLimit()).isEqualTo(100);
        assertThat(results.get(0).getViolations()).isEmpty();

        assertThat(results.get(1).getAccount()).isNotNull();
        assertThat(results.get(1).getAccount().isActiveCard()).isTrue();
        assertThat(results.get(1).getAccount().getAvailableLimit()).isEqualTo(80);
        assertThat(results.get(1).getViolations()).isEmpty();

        assertThat(results.get(2).getAccount()).isNotNull();
        assertThat(results.get(2).getAccount().isActiveCard()).isTrue();
        assertThat(results.get(2).getAccount().getAvailableLimit()).isEqualTo(60);
        assertThat(results.get(2).getViolations()).isEmpty();

        assertThat(results.get(3).getAccount()).isNotNull();
        assertThat(results.get(3).getAccount().isActiveCard()).isTrue();
        assertThat(results.get(3).getAccount().getAvailableLimit()).isEqualTo(40);
        assertThat(results.get(3).getViolations()).isEmpty();

        assertThat(results.get(4).getAccount()).isNotNull();
        assertThat(results.get(4).getAccount().isActiveCard()).isTrue();
        assertThat(results.get(4).getAccount().getAvailableLimit()).isEqualTo(40);
        assertThat(results.get(4).getViolations()).isNotEmpty();
        assertThat(results.get(4).getViolations()).contains("high-frequency-small-interval");

        assertThat(results.get(5).getAccount()).isNotNull();
        assertThat(results.get(5).getAccount().isActiveCard()).isTrue();
        assertThat(results.get(5).getAccount().getAvailableLimit()).isEqualTo(30);
        assertThat(results.get(5).getViolations()).isEmpty();
    }

    @Test
    void shouldReturnDoubledTransactionViolation_whenTwoTransactionToSameMerchantAndValueAreMadeInLessThenTwoMinutes() {

        AccountCreation account = buildAccountCreation(true, 100);
        TransactionCreation transaction1 = buildTransactionCreation("Burger King", 20, "2019-02-13T11:00:00");
        TransactionCreation transaction2 = buildTransactionCreation("McDonald's", 10, "2019-02-13T11:00:01");
        TransactionCreation transaction3 = buildTransactionCreation("Burger King", 20, "2019-02-13T11:00:02");
        TransactionCreation transaction4 = buildTransactionCreation("Burger King", 15, "2019-02-13T11:00:03");
        List<OperationRequest> requests = List.of(account, transaction1, transaction2, transaction3, transaction4);

        List<OperationResult> results = authorizer.execute(requests);

        assertThat(results).isNotEmpty();
        assertThat(results.size()).isEqualTo(5);

        assertThat(results.get(0).getAccount()).isNotNull();
        assertThat(results.get(0).getAccount().isActiveCard()).isTrue();
        assertThat(results.get(0).getAccount().getAvailableLimit()).isEqualTo(100);
        assertThat(results.get(0).getViolations()).isEmpty();

        assertThat(results.get(1).getAccount()).isNotNull();
        assertThat(results.get(1).getAccount().isActiveCard()).isTrue();
        assertThat(results.get(1).getAccount().getAvailableLimit()).isEqualTo(80);
        assertThat(results.get(1).getViolations()).isEmpty();

        assertThat(results.get(2).getAccount()).isNotNull();
        assertThat(results.get(2).getAccount().isActiveCard()).isTrue();
        assertThat(results.get(2).getAccount().getAvailableLimit()).isEqualTo(70);
        assertThat(results.get(2).getViolations()).isEmpty();

        assertThat(results.get(3).getAccount()).isNotNull();
        assertThat(results.get(3).getAccount().isActiveCard()).isTrue();
        assertThat(results.get(3).getAccount().getAvailableLimit()).isEqualTo(70);
        assertThat(results.get(3).getViolations()).isNotEmpty();
        assertThat(results.get(3).getViolations()).contains("doubled-transaction");

        assertThat(results.get(4).getAccount()).isNotNull();
        assertThat(results.get(4).getAccount().isActiveCard()).isTrue();
        assertThat(results.get(4).getAccount().getAvailableLimit()).isEqualTo(55);
        assertThat(results.get(4).getViolations()).isEmpty();
    }

    @Test
    void shouldReturnMultipleViolations_whenMultipleViolationsAreFound() {

        AccountCreation account = buildAccountCreation(true, 100);
        TransactionCreation transaction = buildTransactionCreation("McDonald's", 10, "2019-02-13T11:00:01");
        TransactionCreation transaction2 = buildTransactionCreation("Burger King", 20, "2019-02-13T11:00:02");
        TransactionCreation transaction3 = buildTransactionCreation("Burger King", 5, "2019-02-13T11:00:07");
        TransactionCreation transaction4 = buildTransactionCreation("Burger King", 5, "2019-02-13T11:00:08");
        TransactionCreation transaction5 = buildTransactionCreation("Burger King", 150, "2019-02-13T11:00:18");
        TransactionCreation transaction6 = buildTransactionCreation("Burger King", 190, "2019-02-13T11:00:22");
        TransactionCreation transaction7 = buildTransactionCreation("Burger King", 15, "2019-02-13T12:00:27");
        List<OperationRequest> requests = List.of(
               account,
               transaction,
               transaction2,
               transaction3,
               transaction4,
               transaction5,
               transaction6,
               transaction7
        );

        List<OperationResult> results = authorizer.execute(requests);

        assertThat(results).isNotEmpty();
        assertThat(results.size()).isEqualTo(8);

        assertThat(results.get(0).getAccount()).isNotNull();
        assertThat(results.get(0).getAccount().isActiveCard()).isTrue();
        assertThat(results.get(0).getAccount().getAvailableLimit()).isEqualTo(100);
        assertThat(results.get(0).getViolations()).isEmpty();

        assertThat(results.get(1).getAccount()).isNotNull();
        assertThat(results.get(1).getAccount().isActiveCard()).isTrue();
        assertThat(results.get(1).getAccount().getAvailableLimit()).isEqualTo(90);
        assertThat(results.get(1).getViolations()).isEmpty();

        assertThat(results.get(2).getAccount()).isNotNull();
        assertThat(results.get(2).getAccount().isActiveCard()).isTrue();
        assertThat(results.get(2).getAccount().getAvailableLimit()).isEqualTo(70);
        assertThat(results.get(2).getViolations()).isEmpty();

        assertThat(results.get(3).getAccount()).isNotNull();
        assertThat(results.get(3).getAccount().isActiveCard()).isTrue();
        assertThat(results.get(3).getAccount().getAvailableLimit()).isEqualTo(65);
        assertThat(results.get(3).getViolations()).isEmpty();

        assertThat(results.get(4).getAccount()).isNotNull();
        assertThat(results.get(4).getAccount().isActiveCard()).isTrue();
        assertThat(results.get(4).getAccount().getAvailableLimit()).isEqualTo(65);
        assertThat(results.get(4).getViolations()).isNotEmpty();
        assertThat(results.get(4).getViolations()).contains("high-frequency-small-interval");
        assertThat(results.get(4).getViolations()).contains("doubled-transaction");

        assertThat(results.get(5).getAccount()).isNotNull();
        assertThat(results.get(5).getAccount().isActiveCard()).isTrue();
        assertThat(results.get(5).getAccount().getAvailableLimit()).isEqualTo(65);
        assertThat(results.get(5).getViolations()).isNotEmpty();
        assertThat(results.get(5).getViolations()).contains("insufficient-limit");
        assertThat(results.get(5).getViolations()).contains("high-frequency-small-interval");

        assertThat(results.get(6).getAccount()).isNotNull();
        assertThat(results.get(6).getAccount().isActiveCard()).isTrue();
        assertThat(results.get(6).getAccount().getAvailableLimit()).isEqualTo(65);
        assertThat(results.get(6).getViolations()).isNotEmpty();
        assertThat(results.get(6).getViolations()).contains("insufficient-limit");
        assertThat(results.get(6).getViolations()).contains("high-frequency-small-interval");

        assertThat(results.get(7).getAccount()).isNotNull();
        assertThat(results.get(7).getAccount().isActiveCard()).isTrue();
        assertThat(results.get(7).getAccount().getAvailableLimit()).isEqualTo(50);
        assertThat(results.get(7).getViolations()).isEmpty();
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