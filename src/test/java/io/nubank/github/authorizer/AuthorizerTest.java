package io.nubank.github.authorizer;

import io.nubank.github.authorizer.account.AccountCreation;
import io.nubank.github.authorizer.account.AccountCreationResult;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class AuthorizerTest {

    @Test
    void shouldCreateAccountSuccessfully_whenCreateAccountIsTheOnlyOperation() {

        AccountCreation accountCreation = new AccountCreation(false, 750);
        List<AccountCreation> requests = List.of(accountCreation);

        Authorizer target = new Authorizer();
        List<AccountCreationResult> results = target.execute(requests);

        assertThat(results)
                .isNotNull()
                .isNotEmpty();
        assertThat(results.size()).isEqualTo(1);
        assertThat(results.get(0)).isNotNull();
        assertThat(results.get(0).getState()).isNotNull();
        assertThat(results.get(0).getState().isActiveCard()).isFalse();
        assertThat(results.get(0).getState().getAvailableLimit()).isEqualTo(750);
        assertThat(results.get(0).getViolations()).isEmpty();
    }

    @Test
    void shouldReturnViolation_whenCreateAccountButAccountAlreadyExists() {

        Authorizer target = new Authorizer();

        AccountCreation accountCreation = new AccountCreation(true, 175);
        AccountCreation secondAccountCreation = new AccountCreation(true, 350);
        List<AccountCreation> requests = List.of(accountCreation, secondAccountCreation);

        List<AccountCreationResult> results = target.execute(requests);

        assertThat(results)
                .isNotNull()
                .isNotEmpty();
        assertThat(results.size()).isEqualTo(2);

        assertThat(results.get(0)).isNotNull();
        assertThat(results.get(0).getState()).isNotNull();
        assertThat(results.get(0).getState().isActiveCard()).isTrue();
        assertThat(results.get(0).getState().getAvailableLimit()).isEqualTo(175);
        assertThat(results.get(0).getViolations()).isEmpty();

        assertThat(results.get(1)).isNotNull();
        assertThat(results.get(1).getState()).isNotNull();
        assertThat(results.get(1).getState().isActiveCard()).isTrue();
        assertThat(results.get(1).getState().getAvailableLimit()).isEqualTo(175);
        assertThat(results.get(1).getViolations()).isNotEmpty();
        assertThat(results.get(1).getViolations().size()).isEqualTo(1);
        assertThat(results.get(1).getViolations()).contains("account-already-initialized");
    }
}