package org.manhattan;

public class Account {
    int accountId;
    String accountName;
    int currentBalance;
    String accountPassword;

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public int getCurrentBalance() {
        return currentBalance;
    }

    public void setCurrentBalance(int currentBalance) {
        this.currentBalance = currentBalance;
    }

    public String getAccountPassword() {
        return accountPassword;
    }

    public void setAccountPassword(String accountPassword) {
        this.accountPassword = accountPassword;
    }

    public Account(int accountId, String accountName, int currentBalance, String accountPassword) {
        this.accountId = accountId;
        this.accountName = accountName;
        this.currentBalance = currentBalance;
        this.accountPassword = accountPassword;
    }

    public Account() {
    }

    @Override
    public String toString() {
        return "Account{" +
                "accountId=" + accountId +
                ", accountName='" + accountName + '\'' +
                ", currentBalance=" + currentBalance +
                '}';
    }
}
