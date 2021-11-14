package io.nubank.github.authorizer.account;

import java.util.List;

interface AccountCreationViolationVerifier {
    List<String> verify(Account existingAccount, AccountCreationRequest request);
}
