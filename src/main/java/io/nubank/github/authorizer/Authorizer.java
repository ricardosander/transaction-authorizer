package io.nubank.github.authorizer;

import io.nubank.github.authorizer.account.AccountCreation;
import io.nubank.github.authorizer.account.AccountCreationUseCase;
import io.nubank.github.authorizer.account.AccountRepository;
import io.nubank.github.authorizer.transaction.TransactionCreation;
import io.nubank.github.authorizer.transaction.TransactionCreationUseCase;

import java.util.ArrayList;
import java.util.List;

class Authorizer {

    private final AccountCreationUseCase accountCreation;
    private final TransactionCreationUseCase transactionCreation;

    Authorizer(AccountRepository accountRepository) {
        accountCreation = new AccountCreationUseCase(accountRepository);
        transactionCreation = new TransactionCreationUseCase(accountRepository);
    }

    List<OperationResult> execute(List<OperationRequest> requests) {

        List<OperationResult> results = new ArrayList<>();

        requests.forEach(request -> {
            if (request instanceof AccountCreation) {
                results.add(accountCreation.execute((AccountCreation) request));
            }

            if (request instanceof TransactionCreation) {
                results.add(transactionCreation.execute((TransactionCreation) request));
            }
        });

        return results;
    }
}
