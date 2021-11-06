package io.nubank.github.authorizer.account;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

class AccountCreationUseCaseTest {

    @Test
    void shouldCreateAccount_whenInactiveCardIsGiven() {

        AccountCreation request = new AccountCreation(false, 750);
        AccountCreationResult result = new AccountCreationUseCase().execute(request);

        assertThat(result).isNotNull();
        assertThat(result.isActiveCard()).isFalse();
        assertThat(result.getAvailableLimit()).isEqualTo(750);
        assertThat(result.getViolations()).isEmpty();
    }

    @Test
    void shouldCreateAccount_whenActiveCardIsGiven() {

        AccountCreation request = new AccountCreation(true, 175);
        AccountCreationResult result = new AccountCreationUseCase().execute(request);

        assertThat(result).isNotNull();
        assertThat(result.isActiveCard()).isTrue();
        assertThat(result.getAvailableLimit()).isEqualTo(175);
        assertThat(result.getViolations()).isEmpty();
    }

    @Test
    void shouldReturnViolationWhenCreatingAccount_whenAccountIsAlreadyCreated() {

        AccountCreationUseCase accountCreationUseCase = new AccountCreationUseCase();

        AccountCreation firstRequest = new AccountCreation(true, 175);
        accountCreationUseCase.execute(firstRequest);

        AccountCreation secondRequest = new AccountCreation(true, 350);
        AccountCreationResult result = accountCreationUseCase.execute(secondRequest);

        assertThat(result).isNotNull();
        assertThat(result.isActiveCard()).isTrue();
        assertThat(result.getAvailableLimit()).isEqualTo(350);
        assertThat(result.getViolations()).isNotEmpty();
        assertThat(result.getViolations().size()).isEqualTo(1);
        assertThat(result.getViolations()).contains("account-already-initialized");
    }

    @Test
    void shouldReturnViolationOnAllCreatingAccount_whenAccountIsAlreadyCreated() {

        AccountCreationUseCase accountCreationUseCase = new AccountCreationUseCase();

        AccountCreation firstRequest = new AccountCreation(true, 175);
        accountCreationUseCase.execute(firstRequest);

        AccountCreation secondRequest = new AccountCreation(true, 350);
        AccountCreationResult secondResult = accountCreationUseCase.execute(secondRequest);

        assertThat(secondResult).isNotNull();
        assertThat(secondResult.isActiveCard()).isTrue();
        assertThat(secondResult.getAvailableLimit()).isEqualTo(350);
        assertThat(secondResult.getViolations()).isNotEmpty();
        assertThat(secondResult.getViolations().size()).isEqualTo(1);
        assertThat(secondResult.getViolations()).contains("account-already-initialized");

        AccountCreation thirdRequest = new AccountCreation(false, 500);
        AccountCreationResult thirdResult = accountCreationUseCase.execute(thirdRequest);

        assertThat(thirdResult).isNotNull();
        assertThat(thirdResult.isActiveCard()).isFalse();
        assertThat(thirdResult.getAvailableLimit()).isEqualTo(500);
        assertThat(thirdResult.getViolations()).isNotEmpty();
        assertThat(thirdResult.getViolations().size()).isEqualTo(1);
        assertThat(thirdResult.getViolations()).contains("account-already-initialized");
    }
}