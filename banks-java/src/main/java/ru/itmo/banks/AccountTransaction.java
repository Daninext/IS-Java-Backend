package ru.itmo.banks;

public class AccountTransaction {
    private final Transaction transaction;
    private final Account outAccount;
    private final Account toAccount;

    private final float money;

    public AccountTransaction(Transaction transaction, Account outAccount, Account toAccount, float money) {
        this.transaction = transaction;
        this.outAccount = outAccount;
        this.toAccount = toAccount;
        this.money = money;
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public Account getOutAccount() {
        return outAccount;
    }

    public Account getToAccount() {
        return toAccount;
    }

    public float getMoney() {
        return money;
    }
}
