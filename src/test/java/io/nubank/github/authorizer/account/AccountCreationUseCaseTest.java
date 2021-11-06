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
}