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
    }
}