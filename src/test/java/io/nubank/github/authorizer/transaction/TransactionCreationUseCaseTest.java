package io.nubank.github.authorizer.transaction;

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
}