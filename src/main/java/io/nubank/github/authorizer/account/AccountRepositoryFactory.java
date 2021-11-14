package io.nubank.github.authorizer.account;

public abstract class AccountRepositoryFactory {
    public static AccountRepository create() {
        return new AccountRepositoryImpl();
    }
}
