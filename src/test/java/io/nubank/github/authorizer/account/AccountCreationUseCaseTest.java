package io.nubank.github.authorizer.account;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

class AccountCreationUseCaseTest {

    private AccountCreationUseCase target;

    @BeforeEach
    void setUp() {
        target = new AccountCreationUseCase();
    }

    @Test
    void shouldCreateAccount_whenInactiveCardIsGiven() {

        AccountCreation request = new AccountCreation(false, 750);
        AccountCreationResult result = target.execute(request);

        assertThat(result).isNotNull();
        assertThat(result.getAccount().isActiveCard()).isFalse();
        assertThat(result.getAccount().getAvailableLimit()).isEqualTo(750);
        assertThat(result.getViolations()).isEmpty();
    }

    @Test
    void shouldCreateAccount_whenActiveCardIsGiven() {

        AccountCreation request = new AccountCreation(true, 175);
        AccountCreationResult result = target.execute(request);

        assertThat(result).isNotNull();
        assertThat(result.getAccount().isActiveCard()).isTrue();
        assertThat(result.getAccount().getAvailableLimit()).isEqualTo(175);
        assertThat(result.getViolations()).isEmpty();
    }

    @Test
    void shouldReturnViolationWhenCreatingAccount_whenAccountIsAlreadyCreated() {

        target.execute(new AccountCreation(true, 350));

        AccountCreation request = new AccountCreation(true, 175);
        AccountCreationResult result = target.execute(request);

        assertThat(result).isNotNull();
        assertThat(result.getAccount().isActiveCard()).isTrue();
        assertThat(result.getAccount().getAvailableLimit()).isEqualTo(350);
        assertThat(result.getViolations()).isNotEmpty();
        assertThat(result.getViolations().size()).isEqualTo(1);
        assertThat(result.getViolations()).contains("account-already-initialized");
    }

    @Test
    void shouldReturnViolationOnAllCreatingAccount_whenAccountIsAlreadyCreated() {

        target.execute(new AccountCreation(false, 500));

        AccountCreation secondRequest = new AccountCreation(true, 350);
        AccountCreationResult secondResult = target.execute(secondRequest);

        assertThat(secondResult).isNotNull();
        assertThat(secondResult.getAccount().isActiveCard()).isFalse();
        assertThat(secondResult.getAccount().getAvailableLimit()).isEqualTo(500);
        assertThat(secondResult.getViolations()).isNotEmpty();
        assertThat(secondResult.getViolations().size()).isEqualTo(1);
        assertThat(secondResult.getViolations()).contains("account-already-initialized");

        AccountCreation thirdRequest = new AccountCreation(false, 175);
        AccountCreationResult thirdResult = target.execute(thirdRequest);

        assertThat(thirdResult).isNotNull();
        assertThat(thirdResult.getAccount().isActiveCard()).isFalse();
        assertThat(thirdResult.getAccount().getAvailableLimit()).isEqualTo(500);
        assertThat(thirdResult.getViolations()).isNotEmpty();
        assertThat(thirdResult.getViolations().size()).isEqualTo(1);
        assertThat(thirdResult.getViolations()).contains("account-already-initialized");
    }
}