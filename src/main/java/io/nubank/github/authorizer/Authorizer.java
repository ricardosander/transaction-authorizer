package io.nubank.github.authorizer;

import io.nubank.github.authorizer.account.AccountCreationRequest;
import io.nubank.github.authorizer.account.AccountCreationUseCase;
import io.nubank.github.authorizer.account.AccountRepository;
import io.nubank.github.authorizer.transaction.TransactionCreationRequest;
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
            if (request instanceof AccountCreationRequest) {
                results.add(accountCreation.execute((AccountCreationRequest) request));
            }

            if (request instanceof TransactionCreationRequest) {
                results.add(transactionCreation.execute((TransactionCreationRequest) request));
            }
        });

        return results;
    }
}
