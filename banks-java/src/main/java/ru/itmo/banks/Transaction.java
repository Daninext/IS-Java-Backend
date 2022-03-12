package ru.itmo.banks;

public interface Transaction {
    void cancelTransaction(AccountTransaction transaction);
}
