package io.nubank.github.authorizer.account;

public interface AccountRepository {
    Account getAccount();
    boolean save(Account account);
}
