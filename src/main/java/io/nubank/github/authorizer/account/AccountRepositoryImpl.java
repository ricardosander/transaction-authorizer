package io.nubank.github.authorizer.account;

class AccountRepositoryImpl implements AccountRepository {

    private Account account;

    AccountRepositoryImpl() {
    }

    public Account getAccount() {
        return account;
    }

    public boolean save(Account account) {
        if (this.account != null) {
            return false;
        }
        this.account = account;
        return true;
    }
}
