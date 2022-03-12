package ru.itmo.banks;

public class DepositTransactionImpl implements Transaction {
    public void cancelTransaction(AccountTransaction transaction) {
        transaction.getOutAccount().getMoney().addMoney(-transaction.getMoney());
    }
}
