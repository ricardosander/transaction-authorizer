package io.nubank.github.authorizer.account;

public class AccountRepository {

    private Account account;

    public Account getAccount() {
        return account;
    }

    void setAccount(Account account) {
        if (this.account == null) {
            this.account = account;
        }
    }
}
